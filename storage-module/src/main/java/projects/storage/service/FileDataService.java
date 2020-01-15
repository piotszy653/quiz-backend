package projects.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.storage.model.FileData;
import projects.storage.repository.FileDataRepository;
import projects.storage.utils.tika.TikaHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
@Slf4j
public class FileDataService {

    private final FileDataRepository fileDataRepository;

    private final MessageSource messageSource;


    public boolean existsById(Long id) {
        return fileDataRepository.existsById(id);
    }

    public byte[] readFile(String path) {
        File file = new File(path);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("file.io_exception", null, null));
        }
    }

    public byte[] fetchFile(UUID uuid) {
        FileData fileData = fileDataRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("file.not_found.uuid", new Object[]{uuid}, null)));
        return readFile(fileData.getPath());
    }

    public Set<FileData> getAllByIds(Set<Long> ids) {
        return fileDataRepository.findAllById(ids);
    }

    public FileData getById(long id) {
        return fileDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("file.not_found.id", new Object[]{Long.toString(id)}, null)));

    }

    public FileData getByUuid(UUID uuid) {
        return fileDataRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("file.not_found.uuid", new Object[]{uuid}, null)));

    }

    public FileData getImageDataByUuid(String uuid) {
        return uuid != null ? getByUuid(UUID.fromString(uuid)) : null;
    }

    public LinkedHashSet<FileData> getAllImagesByUuids(Collection<String> uuids){
        return uuids.stream().map(this::getImageDataByUuid).collect(Collectors.toCollection(LinkedHashSet::new));
    }


    @Transactional
    public FileData create(String directory, InputStreamSource inputStreamSource) {
        FileData fileData = fileDataFromFile(directory, inputStreamSource);
        return create(fileData, inputStreamSource);
    }

    @Transactional
    public FileData create(FileData fileData, InputStreamSource inputStreamSource) {
        createFileInStorage(fileData, inputStreamSource);
        return create(fileData);
    }

    @Transactional
    public FileData create(FileData fileData) {
        return fileDataRepository.save(fileData);
    }

    @Transactional
    public FileData update(InputStreamSource inputStreamSource, FileData fileData) {
        fileData.setFileExtension(TikaHelper.getExtension(inputStreamSource));
        updateFileInStorage(fileData.getPath(), inputStreamSource);
        return create(fileData);
    }

    @Transactional
    public FileData updateByUuid(InputStreamSource inputStreamSource, UUID uuid) {
        return update(
                inputStreamSource,
                fileDataRepository.findByUuid(uuid)
                        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("file.not_found.uuid", new Object[]{uuid}, null)))
        );
    }

    @Transactional
    public FileData update(InputStreamSource inputStreamSource, long id) {
        return update(
                inputStreamSource,
                fileDataRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("file.not_found.id", new Object[]{Long.toString(id)}, null)))
        );
    }

    @Transactional
    public void deleteAllByIds(Set<Long> ids) {
        Set<FileData> data = getAllByIds(ids);
        deleteAll(data);
    }

    @Transactional
    public void deleteAll(Set<FileData> imagesData) {
        imagesData.forEach(this::delete);
    }

    @Transactional
    public void delete(FileData fileData) {
        deleteFromStorage(fileData);
        fileDataRepository.delete(fileData);
    }

    @Transactional
    public void deleteByUuid(UUID uuid) {
        FileData fileData = getByUuid(uuid);
        delete(fileData);
    }

    @Transactional
    public void delete(long id) {
        FileData fileData = getById(id);
        delete(fileData);
    }


    public void createFileInStorage(FileData fileData, InputStreamSource inputStreamSource) {
        try {
            createDirectories(Paths.get(fileData.getDirectory()));
            File file = new File(fileData.getPath());
            FileUtils.copyInputStreamToFile(inputStreamSource.getInputStream(), file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("file.io_exception", null, null));
        }
    }

    public void updateFileInStorage(String path, InputStreamSource inputStreamSource) {
        try {
            deleteFromStorage(path);
            File file = new File(path);
            FileUtils.copyInputStreamToFile(inputStreamSource.getInputStream(), file);
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage("file.io_exception", null, null));
        }
    }

    public void deleteFromStorage(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public void deleteFromStorage(FileData fileData) {
        deleteFromStorage(fileData.getPath());
    }

    public FileData fileDataFromFile(String directory, InputStreamSource inputStreamSource) {
        return new FileData(
                TikaHelper.getExtension(inputStreamSource),
                directory
        );
    }

    public void createDirectories(Path path) throws IOException {
        Files.createDirectories(path);
    }
}

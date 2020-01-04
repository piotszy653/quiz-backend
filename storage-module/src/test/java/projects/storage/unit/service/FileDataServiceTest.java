package projects.storage.unit.service;

import projects.storage.data.IFileData;
import projects.storage.model.FileData;
import projects.storage.repository.FileDataRepository;
import projects.storage.service.FileDataService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileDataServiceTest implements IFileData {

    @Spy
    @InjectMocks
    FileDataService fileDataService;

    @Mock
    FileDataRepository fileDataRepository;

    @Mock
    MessageSource messageSource;

    private byte[] defaultFileContent;
    private byte[] updatedFileContent;
    private Long defaultId;
    private UUID defaultUuid;
    private String defaultDirectory;
    private FileData defaultFileData;
    private FileData updatedFileData;
    private Set<FileData> defaultFileDataSet;
    private Set<Long> defaultFileDataIds;
    private MultipartFile defaultFile;
    private MultipartFile updatedFile;
    private File tempDirectoryFile;

    @Before
    public void init() {
        defaultFileContent = getDefaultFileContent();
        updatedFileContent = getUpdatedFileContent();
        defaultUuid = generateUuid();
        defaultDirectory = getDefaultDirectory();
        defaultFileData = getDefaultFileData(defaultUuid);
        updatedFileData = getDefaultFileData(UUID.randomUUID());
        updatedFileData.setId(2L);
        defaultFileDataSet = Stream.of(defaultFileData, updatedFileData).collect(Collectors.toSet());
        defaultFileDataIds = defaultFileDataSet.stream().map(FileData::getId).collect(Collectors.toSet());
        defaultId = defaultFileData.getId();
        defaultFile = new MockMultipartFile(
                defaultUuid.toString(),
                defaultUuid.toString() + "." + getDefaultFileExtension(),
                null,
                defaultFileContent
        );
        updatedFile = new MockMultipartFile(
                defaultUuid.toString(),
                defaultUuid.toString() + "." + getDefaultFileExtension(),
                null,
                updatedFileContent
        );
        tempDirectoryFile = new File(defaultDirectory);
        tempDirectoryFile.mkdirs();
    }

    @After
    public void deleteTemporaryDirectories() throws IOException {
        FileUtils.deleteDirectory(tempDirectoryFile);
    }

    private void createDefaultFile() throws IOException {
        defaultFile.transferTo(Paths.get(defaultFileData.getPath()));
    }

    private void createDefaultFiles() throws IOException {
        defaultFile.transferTo(Paths.get(defaultFileData.getPath()));
        defaultFile.transferTo(Paths.get(updatedFileData.getPath()));

    }

    private byte[] readFileBytes(FileData fileData) throws IOException {
        return Files.readAllBytes(Paths.get(fileData.getPath()));
    }


    private void equalsAssertion(Object expected, Object actual) {
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private void fileContentAssertion(byte[] expected, byte[] actual) {
        assertNotNull(actual);
        assertArrayEquals(expected, actual);
    }

    private void saveStub(){
        when(fileDataRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private void findByUuidStub(){
        when(fileDataRepository.findByUuid(defaultUuid)).thenReturn(Optional.of(defaultFileData));
    }

    private void findAllByIdStub(){
        when(fileDataRepository.findAllById(defaultFileDataIds)).thenReturn(defaultFileDataSet);
    }

    private void findByIdStub(){
        when(fileDataRepository.findById(defaultId)).thenReturn(Optional.of(defaultFileData));

    }

    private void verifyDeleteFileDataSet(int times){
        defaultFileDataSet.forEach(fileData -> {
            verifyDeleteFile(times, fileData);
        });
    }

    private void verifyDeleteFile(int times, FileData fileData){
        verify(fileDataService, times(times)).deleteFromStorage(fileData);
        verify(fileDataRepository, times(times)).delete(fileData);
    }

    private void verifyDefaultFilesExistence(boolean exists) {
        verifyFileExistence(defaultFileData.getPath(), exists);
        verifyFileExistence(updatedFileData.getPath(), exists);

    }

    private void verifyDefaultFileExistence(boolean exists){
        verifyFileExistence(defaultFileData.getPath(), exists);
    }

    private void verifyFileExistence(String path, boolean exists){
        assertEquals(exists, new File(path).exists());
    }

    private void verifyUpdatedFile(FileData fileData) throws IOException {
        equalsAssertion(defaultFileData, fileData);
        verifyDefaultFileExistence(true);
        fileContentAssertion(updatedFileContent, readFileBytes(fileData));
    }

    @Test
    public void shouldReturnFileBytes() throws IOException {

        //when
        createDefaultFile();
        byte[] fileContent = fileDataService.readFile(defaultFileData.getPath());

        //then
        fileContentAssertion(defaultFileContent, fileContent);
    }

    @Test
    public void shouldFetchFile() throws IOException {

        //given
        createDefaultFile();
        findByUuidStub();

        //when
        byte[] fileContent = fileDataService.fetchFile(defaultUuid);

        //then
        fileContentAssertion(defaultFileContent, fileContent);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenFetchingNotExistingFile() {
        //when
        fileDataService.fetchFile(defaultUuid);
    }

    @Test
    public void shouldListFileData() throws IOException {

        //given
        createDefaultFiles();
        findAllByIdStub();

        //when
        Set<FileData> fileDataSet = fileDataService.getAllByIds(defaultFileDataIds);
        equalsAssertion(defaultFileDataSet, fileDataSet);

    }

    @Test
    public void shouldReturnFileDataById() throws IOException {
        //given
        createDefaultFile();
        findByIdStub();

        //when
        FileData fileData = fileDataService.getById(defaultId);

        equalsAssertion(defaultFileData, fileData);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenGettingFileDataByWrongId() {
        //when
        fileDataService.getById(defaultId);
    }

    @Test
    public void shouldReturnFileDataByUuid() throws IOException {
        //given
        createDefaultFile();
        findByUuidStub();

        //when
        FileData fileData = fileDataService.getByUuid(defaultUuid);

        equalsAssertion(defaultFileData, fileData);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenGettingFileDataByWrongUuid() {
        //when
        fileDataService.getByUuid(defaultUuid);
    }

    @Test
    public void shouldCreateFile() throws IOException {

        //given
        saveStub();

        //when
        FileData fileData = fileDataService.create(defaultDirectory, defaultFile);

        //then
        assertNotNull(fileData);
        assertEquals(defaultFileData.getDirectory(), fileData.getDirectory());
        verify(fileDataService).createFileInStorage(fileData, defaultFile);
        verifyFileExistence(fileData.getPath(), true);
        fileContentAssertion(defaultFileContent, readFileBytes(fileData));
    }

    @Test
    public void shouldUpdateFileById() throws IOException {
        //given
        createDefaultFile();
        saveStub();
        findByIdStub();

        //when
        FileData fileData = fileDataService.update(updatedFile, defaultId);

        verifyUpdatedFile(fileData);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenUpdatingFileByWrongId() {
        //when
        fileDataService.update(defaultFile, defaultId);
    }

    @Test
    public void shouldUpdateFileByUuid() throws IOException {
        //given
        createDefaultFile();
        saveStub();
        findByUuidStub();

        //when
        FileData fileData = fileDataService.updateByUuid(updatedFile, defaultUuid);

        verifyUpdatedFile(fileData);

    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenUpdatingFileByWrongUuid() {
        //when
        fileDataService.updateByUuid(defaultFile, defaultUuid);
    }

    @Test
    public void shouldDeleteFileSetByIds() throws IOException {
        //given
        createDefaultFiles();
        findAllByIdStub();

        //when
        fileDataService.deleteAllByIds(defaultFileDataIds);

        //then
        verifyDeleteFileDataSet(1);
        verifyDefaultFilesExistence(false);

    }

    @Test
    public void shouldNotDeleteAnyFileByEmptyIdSet() throws IOException {
        //when
        createDefaultFiles();
        fileDataService.deleteAllByIds(new HashSet<>());

        //then
        verifyDeleteFileDataSet(0);
        verifyDefaultFilesExistence(true);
    }

    @Test
    public void shouldDeleteAllGivenFiles() throws IOException {

        //when
        createDefaultFiles();
        fileDataService.deleteAll(defaultFileDataSet);

        //then
        verifyDeleteFileDataSet(1);
        verifyDefaultFilesExistence(false);
    }

    @Test
    public void shouldNotDeleteAnyFilesByEmptyFileDataSet() throws IOException {
        //when
        createDefaultFiles();
        fileDataService.deleteAll(new HashSet<>());

        //then
        verifyDeleteFileDataSet(0);
        verifyDefaultFilesExistence(true);
    }

    @Test
    public void shouldDeleteFile() throws IOException {
        //when
        createDefaultFile();
        fileDataService.delete(defaultFileData);

        //then
        verifyDeleteFile(1, defaultFileData);
        verifyDefaultFileExistence(false);
    }

    @Test
    public void shouldDeleteFileByUuid() throws IOException {
        //given
        createDefaultFile();
        findByUuidStub();

        //when
        fileDataService.deleteByUuid(defaultUuid);

        //then
        verifyDeleteFile(1, defaultFileData);
        verifyDefaultFileExistence(false);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenDeletingFileByWrongUuid(){
        fileDataService.deleteByUuid(defaultUuid);
    }

    @Test
    public void shouldDeleteFileById() throws IOException {
        //given
        createDefaultFile();
        findByIdStub();

        //when
        fileDataService.delete(defaultId);

        //then
        verifyDeleteFile(1, defaultFileData);
        verifyDefaultFileExistence(false);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenDeletingFileByWrongId(){
        fileDataService.delete(defaultId);
    }

    @Test
    public void shouldCreateFileInStorage() throws IOException {

        //when
        fileDataService.createFileInStorage(defaultFileData, defaultFile);

        //then
        verify(fileDataService, times(1)).createDirectories(Paths.get(defaultFileData.getDirectory()));
        verifyDefaultFileExistence(true);
    }

    @Test
    public void shouldUpdateFileInStorage() throws IOException {
        //given
        createDefaultFile();

        //when
        fileDataService.updateFileInStorage(defaultFileData.getPath(), updatedFile);

        //then
        verify(fileDataService, times(1)).deleteFromStorage(defaultFileData.getPath());
        fileContentAssertion(updatedFileContent, readFileBytes(defaultFileData));
    }

    @Test
    public void shouldDeleteFileFromStorage() throws IOException {
        //given
        createDefaultFile();

        //when
        fileDataService.deleteFromStorage(defaultFileData);

        //then
        verifyDefaultFileExistence(false);
    }

    @Test
    public void shouldCreateDirectories() throws IOException {

        //given
        deleteTemporaryDirectories();

        //when
        fileDataService.createDirectories(Paths.get(defaultDirectory));

        verifyFileExistence(defaultDirectory, true);
    }
}

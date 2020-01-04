package projects.storage.service;

import projects.storage.dto.ImageResponseDto;
import projects.storage.model.FileData;
import projects.storage.utils.CmdHelper;
import lombok.RequiredArgsConstructor;
import org.apache.tika.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class PictureService {

    @Value("${images.maxWidth}")
    private int MAX_WIDTH;

    @Value("${images.maxHeight}")
    private int MAX_HEIGHT;

    private final CmdHelper cmdHelper;

    private final MessageSource messageSource;

    public ImageResponseDto createImageResponseDto(FileData fileData) {
        File file = new File(fileData.getPath());
        try {
            BufferedImage image = ImageIO.read(file);

            return new ImageResponseDto(
                    fileData.getUuid().toString(),
                    image.getWidth(),
                    image.getHeight()
            );

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("image.io_exception", null, null));
        }
    }

    public Set<ImageResponseDto> createImageResponseDtoSet(Set<FileData> data) {
        return data.stream().map(this::createImageResponseDto).collect(Collectors.toSet());
    }

    public Process resizeImage(String path) {
        return cmdHelper.execute(createResizeCmd(path, path));
    }

    private String createResizeCmd(String sourcePath, String destPath) {
        String cmd = "convert " + sourcePath + " -resize " + MAX_WIDTH + "x" + MAX_HEIGHT + " " + destPath;
        return SystemUtils.IS_OS_WINDOWS ? "magick " + cmd : cmd;
    }
}

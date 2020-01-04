package projects.storage.unit.service;

import projects.storage.data.IFileData;
import projects.storage.data.IImageData;
import projects.storage.dto.ImageResponseDto;
import projects.storage.model.FileData;
import projects.storage.service.PictureService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ImageIO.class)
public class PictureServiceTest implements IImageData, IFileData {

    @InjectMocks
    PictureService pictureService;

    @Mock
    MessageSource messageSource;

    private FileData defaultFileData;
    private FileData wrongFileData;
    private UUID defaultUuid;
    private ImageResponseDto defaultImageResponseDto;
    private MultipartFile defaultMultipartFile;
    private byte[] defaultFileContent;
    private File tempDirectoryFile;
    private File file;
    private BufferedImage defaultBufferedImage;


    @Before
    public void init() throws IOException {
        defaultFileContent = getDefaultFileContent();
        defaultUuid = generateUuid();
        defaultFileData = getDefaultFileData(defaultUuid);
        wrongFileData = getDefaultFileData(generateUuid());
        defaultImageResponseDto = getDefaultImageResponseDto(defaultUuid.toString());
        defaultMultipartFile = new MockMultipartFile(
                defaultUuid.toString(),
                defaultUuid.toString() + "." + getDefaultFileExtension(),
                null,
                defaultFileContent
        );
        tempDirectoryFile = new File(getDefaultDirectory());
        tempDirectoryFile.mkdirs();
        defaultMultipartFile.transferTo(Paths.get(defaultFileData.getPath()));
        file = new File(defaultFileData.getPath());
        defaultBufferedImage = getDefaultBufferedImage();

    }

    @After
    public void deleteTemporaryDirectories() throws IOException {
        FileUtils.deleteDirectory(tempDirectoryFile);
    }

    @Test
    public void shouldCreateImageResponseDto() throws IOException {

        //given
        PowerMockito.mockStatic(ImageIO.class);
        when(ImageIO.read(file)).thenReturn(defaultBufferedImage);

        //when
        ImageResponseDto dto = pictureService.createImageResponseDto(defaultFileData);

        assertNotNull(dto);
        assertEquals(defaultImageResponseDto, dto);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenReadingNotExistingFile() {

        //when
        pictureService.createImageResponseDto(wrongFileData);
    }

}

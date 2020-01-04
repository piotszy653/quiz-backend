package projects.storage.data;

import projects.storage.dto.ImageResponseDto;

import java.awt.image.BufferedImage;

public interface IImageData {

    default int getDefaultWidth(){
        return 100;
    }

    default int getDefaultHeight(){
        return 200;
    }

    default ImageResponseDto getDefaultImageResponseDto(String uuid) {
        return new ImageResponseDto(uuid, getDefaultWidth(), getDefaultHeight());
    }

    default BufferedImage getDefaultBufferedImage(){
        return new BufferedImage(getDefaultWidth(), getDefaultHeight(), BufferedImage.TYPE_INT_RGB);
    }
}

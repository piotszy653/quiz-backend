package projects.storage.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

@Data
public class ImageResponseDto {

    private String imageUuid;

    private int width;

    private int height;

    public ImageResponseDto(String imageUuid, int width, int height) {
        this.imageUuid = imageUuid;
        this.height = height;
        this.width = width;
    }

}

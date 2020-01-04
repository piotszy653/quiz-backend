package projects.core.controller.file;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projects.storage.service.FileDataService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/files")
@Validated
public class FileController {

    private FileDataService fileDataService;

    @GetMapping(value = "/{uuid}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> fetchFile(@Valid @PathVariable @NotBlank(message = "{imageUuid.not_blank}") String uuid) {
        return new ResponseEntity<>(fileDataService.fetchFile(UUID.fromString(uuid)), HttpStatus.OK);
    }
}

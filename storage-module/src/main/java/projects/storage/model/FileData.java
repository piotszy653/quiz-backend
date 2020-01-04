package projects.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.nio.file.Paths;
import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class FileData extends AbstractBaseEntity<Long> {

    @JsonIgnore
    @Column(nullable = false)
    @NotNull(message = "{uuid.not_null}")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @NonNull
    @Column(nullable = false)
    @Size(max = 255, message = "file_extension.max:255")
    @NotBlank(message = "{file_extension.not_blank}")
    private String fileExtension;

    @JsonIgnore
    @NonNull
    @Column(nullable = false)
    @Size(max = 255, message = "directory.max:255")
    @NotBlank(message = "{directory.not_blank}")
    public String directory;


    public FileData() {
        setUuid();
    }

    public FileData(String fileExtension, String directory) {
        this.fileExtension = fileExtension;
        this.directory = directory;
        setUuid();
    }

    public FileData(UUID uuid, String fileExtension, String directory) {
        this.uuid = uuid;
        this.fileExtension = fileExtension;
        this.directory = directory;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private void setUuid() {
        uuid = UUID.randomUUID();
    }

    @JsonIgnore
    public String getPath() {
        return Paths.get(directory, uuid + "." + fileExtension).toString();
    }

}

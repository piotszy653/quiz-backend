package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
public class Answer extends AbstractBaseEntity<Long>{

    @Column(nullable = false)
    @NotBlank(message = "{answer.not_blank}")
    @Size(max = 255, message = "name.max:255")
    private String answer;

    @NotNull(message = "{owner_uuid.not_null}")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID ownerUuid;

    @ManyToOne(fetch = EAGER)
    FileData imageData;
}

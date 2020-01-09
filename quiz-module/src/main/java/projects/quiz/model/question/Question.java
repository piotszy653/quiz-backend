package projects.quiz.model.question;

import lombok.*;
import org.hibernate.annotations.Type;
import projects.quiz.model.AbstractBaseEntity;
import projects.quiz.model.Assessment;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Question extends AbstractBaseEntity<Long> {

    @NotBlank(message = "{question.not_blank}")
    @Size(max = 1000, message = "question.max_length:1000")
    @Column(nullable = false, length = 1000)
    private String question;

    @NotNull(message = "{owner_uuid.not_null}")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID ownerUuid;

    @ManyToOne(fetch = EAGER)
    FileData imageData;

    @NotNull(message = "{question_type.not_null}")
    @Column(nullable = false)
    @Enumerated(value = STRING)
    @Setter(AccessLevel.NONE)
    QuestionType type;

    @NotNull(message = "{assessment.not_null}")
    @ManyToOne(fetch = EAGER)
    Assessment assessment;

}

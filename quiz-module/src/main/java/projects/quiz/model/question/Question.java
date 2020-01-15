package projects.quiz.model.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.aspectj.weaver.ast.Test;
import org.hibernate.annotations.Type;
import projects.quiz.model.AbstractBaseEntity;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static projects.quiz.utils.enums.QuestionType.TEST;
import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Question extends AbstractBaseEntity<Long> {

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

    public void removeImage(){
        imageData = null;
    }

    @JsonIgnore
    public TrueFalseQuestion getIfTrueFalseQuestion(){
        return type.equals(TRUE_FALSE) ? (TrueFalseQuestion) this : null;
    }

    @JsonIgnore
    public TestQuestion getIfTestQuestion(){
        return type.equals(TEST) ? (TestQuestion) this : null;
    }
}

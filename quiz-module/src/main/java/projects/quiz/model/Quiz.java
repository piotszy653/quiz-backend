package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import projects.quiz.model.question.Question;
import projects.quiz.utils.enums.PrivacyPolicy;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.question.QuestionTypesAssessmentsMatch;
import projects.storage.model.FileData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@QuestionTypesAssessmentsMatch
public class Quiz extends AbstractBaseEntity<Long> {

    @Column(nullable = false)
    @NotNull(message = "name.not_null")
    @Size(max = 255, message = "name.max:255")
    private String name;

    @NotNull(message = "{owner_uuid.not_null}")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID ownerUuid;

    @Column(nullable = false)
    @NotNull(message = "owner_username.not_null")
    @Size(max = 255, message = "owner_username.max:255")
    private String ownerUsername;

    @ManyToOne(fetch = EAGER)
    private FileData imageData;

    @NotNull(message = "{privacy_policy.not_null}")
    @Column(nullable = false)
    @Enumerated(value = STRING)
    PrivacyPolicy privacyPolicy;

    @ManyToMany(fetch = EAGER)
    @NotNull(message = "{questions.not_null}")
    private Set<Question> questions;

    @ManyToMany
    @JoinColumn(name = "quiz_id")
    private Map<QuestionType, Assessment> assessments;

    @NotNull(message = "editors.not_null")
    @Column(nullable = false)
    private LinkedHashSet<UUID> editors = new LinkedHashSet<>();

    @NotNull(message = "tags.not_null")
    @Column(nullable = false)
    private LinkedHashSet<String> tags = new LinkedHashSet<>();


    public void removeImage() {
        imageData = null;
    }

    public Set<QuestionType> getQuestionTypes() {

        Set<QuestionType> types = Stream.of(QuestionType.values()).map(type -> {
            if (questions.stream().anyMatch(question -> question.getType().equals(type)))
                return type;
            return null;
        }).collect(Collectors.toSet());
        types.remove(null);

        return types;
    }

    public boolean isEditor(UUID userUuid){
        return ownerUuid.equals(userUuid) || editors.contains(userUuid);
    }

}

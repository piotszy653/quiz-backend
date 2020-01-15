package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.validation.annotation.Validated;
import projects.quiz.model.Answer;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Validated
@NoArgsConstructor
public class TestQuestion extends Question {

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @NotNull(message = "{answers.not_null}")
    private Set<Answer> answers;

    private HashMap<UUID, Boolean> answersCorrectness;

    private boolean isMultipleChoice;


    public TestQuestion(String question, UUID ownerUuid, FileData imageData, LinkedHashSet<Answer> answers, HashMap<UUID, Boolean> answersCorrectness, boolean isMultipleChoice) {
        super(question, ownerUuid, imageData, QuestionType.TEST);
        this.answers = answers;
        this.answersCorrectness = answersCorrectness;
        this.isMultipleChoice = isMultipleChoice;
    }

}

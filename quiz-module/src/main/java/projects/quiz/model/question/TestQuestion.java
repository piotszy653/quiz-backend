package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.model.Answer;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Validated
public class TestQuestion extends Question {

    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull(message = "{answers.not_null}")
    @Size(min = 2, message = "answers.min:2")
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

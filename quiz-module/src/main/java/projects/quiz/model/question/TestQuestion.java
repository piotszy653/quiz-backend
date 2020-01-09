package projects.quiz.model.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import projects.quiz.model.Answer;
import projects.quiz.model.Assessment;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.question.TestQuestionType;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Validated
public class TestQuestion extends Question {

    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull(message = "{answers.not_null}")
    @Size(min = 2, message = "answers.min:2")
    private Set<Answer> answers;

    public TestQuestion(String question, UUID ownerUuid, FileData imageData, @Valid @TestQuestionType QuestionType type, Assessment assessment, LinkedHashSet<Answer> answers){
        super(question, ownerUuid, imageData, type, assessment);
        this.answers = answers;
    }

}

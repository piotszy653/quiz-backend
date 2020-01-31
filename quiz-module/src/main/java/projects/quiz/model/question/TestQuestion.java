package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.model.Answer;
import projects.storage.model.FileData;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.QuestionType.TEST;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Validated
@NoArgsConstructor
public class TestQuestion extends Question {

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @NotNull(message = "{answers.not_null}")
    private Set<Answer> answers;


    private boolean isMultipleChoice;


    public TestQuestion(TestQuestionCreateDto dto, UUID ownerUuid, FileData imageData, LinkedHashSet<Answer> answers) {
        super(dto.getQuestion(), ownerUuid, imageData, TEST, dto.getTags().stream().map(tag -> tag.trim().toLowerCase()).collect(Collectors.toCollection(LinkedHashSet::new)));
        this.answers = answers;
        this.isMultipleChoice = dto.getMultipleChoice();
    }

}

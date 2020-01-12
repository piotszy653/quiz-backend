package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.dto.question.QuestionUpdateDto;
import projects.quiz.utils.validator.answer.AnswersExist;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionUpdateDto extends QuestionUpdateDto {

    @AnswersExist
    private LinkedHashSet<String> answersUuids;

    @AnswersExist
    private LinkedHashSet<String> removedAnswersUuids;

    private Boolean isMultipleChoice;

    public TestQuestionUpdateDto(String question, String imageUuid, LinkedHashSet<String> answersUuids, LinkedHashSet<String> removedAnswersUuids, Boolean isMultipleChoice) {
        super(question, imageUuid);
        this.answersUuids = answersUuids;
        this.removedAnswersUuids = removedAnswersUuids;
        this.isMultipleChoice = isMultipleChoice;
    }
}

package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.TestAnswerDto;
import projects.quiz.dto.question.QuestionCreateDto;
import projects.quiz.utils.validator.answer.AnswersExist;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionCreateDto extends QuestionCreateDto {

    private LinkedHashSet<TestAnswerDto> newAnswers;

    private LinkedList<Boolean> newAnswersCorrectness;

    @AnswersExist
    private HashMap<String, Boolean> answers;

    @NotNull(message = "{is_multiple_choice.not_null}")
    private Boolean isMultipleChoice;

    public TestQuestionCreateDto(String question, String imageUuid, LinkedHashSet<TestAnswerDto> newAnswers, HashMap<String, Boolean> answers, LinkedList<Boolean> newAnswersCorrectness, boolean isMultipleChoice) {
        super(question, imageUuid);
        this.newAnswers = newAnswers;
        this.newAnswersCorrectness = newAnswersCorrectness;
        this.answers = answers;
        this.isMultipleChoice = isMultipleChoice;
    }
}

package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.TestAnswerDto;
import projects.quiz.dto.question.QuestionCreateDto;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionCreateDto extends QuestionCreateDto {

    private LinkedHashSet<TestAnswerDto> answers = new LinkedHashSet<>();

    @NotNull(message = "{is_multiple_choice.not_null}")
    private Boolean isMultipleChoice;

}

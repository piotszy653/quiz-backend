package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.dto.question.QuestionCreateDto;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionCreateDto extends QuestionCreateDto {

    private LinkedHashSet<AnswerDto> answers = new LinkedHashSet<>();

    @NotNull(message = "{multiple_choice.not_null}")
    private Boolean multipleChoice;

}

package projects.quiz.dto.question.trueFalseQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.QuestionCreateDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TrueFalseQuestionCreateDto extends QuestionCreateDto {

    @NotNull(message = "{answer.not_null}")
    private Boolean answer;

}

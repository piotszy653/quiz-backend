package projects.quiz.dto.question.openQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.QuestionCreateDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OpenQuestionCreateDto extends QuestionCreateDto {

    @NotBlank(message = "{answer.not_blank}")
    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

}

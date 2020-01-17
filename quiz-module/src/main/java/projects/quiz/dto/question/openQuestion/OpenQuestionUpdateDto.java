package projects.quiz.dto.question.openQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.QuestionCreateDto;
import projects.quiz.dto.question.QuestionUpdateDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OpenQuestionUpdateDto extends QuestionUpdateDto {

    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

}

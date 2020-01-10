package projects.quiz.dto.answer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AnswerDto {

    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

    private String imageUuid;

}

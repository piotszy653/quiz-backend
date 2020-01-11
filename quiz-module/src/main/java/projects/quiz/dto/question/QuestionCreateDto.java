package projects.quiz.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionCreateDto {

    @NotBlank(message = "{question.not_blank}")
    @Size(max = 1000, message = "question.max_length:1000")
    private String question;

    private String imageUuid;
}

package projects.quiz.dto.answer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AnswerDto {

    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

    private boolean correct;

    private UUID imageUuid;
}

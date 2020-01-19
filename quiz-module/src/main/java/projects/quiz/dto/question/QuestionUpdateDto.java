package projects.quiz.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionUpdateDto {

    @Size(max = 1000, message = "question.max_length:1000")
    private String question;

    private UUID imageUuid;

    private LinkedHashSet<String> tags;

}


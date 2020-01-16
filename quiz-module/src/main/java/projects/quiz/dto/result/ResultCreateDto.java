package projects.quiz.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.utils.validator.quiz.QuizExists;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultCreateDto {

    @NotNull(message = "{quiz_uuid.not_null}")
    @QuizExists
    private String quizUuid;

    private HashMap<String, Boolean> trueFalseAnswers = new HashMap<>();

    private HashMap<String, Set<String>> testAnswers = new HashMap<>();
}

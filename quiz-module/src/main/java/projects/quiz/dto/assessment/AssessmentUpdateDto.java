package projects.quiz.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.utils.validator.assessment.AssessmentCreate;
import projects.quiz.utils.validator.assessment.AssessmentUpdate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentUpdateDto {

    @Size(max = 255, message = "name.max:255")
    private String name;

    @Min(value = 0 ,message = "correct_rate.min:0")
    private Float correctRate;

    private Float incorrectRate;

    private Float minPoints;

    @Min(value = 0 ,message = "max_points.min:0")
    private Float maxPoints;

}

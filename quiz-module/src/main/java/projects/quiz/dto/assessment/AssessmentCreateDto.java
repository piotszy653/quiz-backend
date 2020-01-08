package projects.quiz.dto.assessment;

import lombok.Data;
import projects.quiz.utils.validator.assessment.AssessmentCreate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AssessmentCreate
public class AssessmentCreateDto implements AssessmentDto{

    @Size(max = 255, message = "name.max:255")
    private String name;

    @NotNull(message = "{correct_rate.not_null}")
    @Min(value = 0 ,message = "correct_rate.min:0")
    private Float correctRate;

    @NotNull(message = "{incorrect_rate.not_null}")
    private Float incorrectRate;

    private Float minPoints;

    @Min(value = 0 ,message = "max_points.min:0")
    private Float maxPoints;

}

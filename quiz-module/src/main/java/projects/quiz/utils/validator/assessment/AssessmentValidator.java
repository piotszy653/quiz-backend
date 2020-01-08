package projects.quiz.utils.validator.assessment;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import projects.quiz.dto.assessment.AssessmentDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class AssessmentValidator implements ConstraintValidator<AssessmentCreate, AssessmentDto> {

    private MessageSource messageSource;

    @Override
    public boolean isValid(AssessmentDto dto, ConstraintValidatorContext constraintValidatorContext) {

        Float maxPoints = dto.getMaxPoints();
        Float minPoints = dto.getMinPoints();
        if (maxPoints != null && minPoints != null && maxPoints <= minPoints)
                throw new IllegalArgumentException(messageSource.getMessage("max_points.lower_than_min_points", null, null));

        return true;
    }
}

package projects.quiz.utils.validator.assessment;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import projects.quiz.dto.assessment.AssessmentUpdateDto;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.UUID;

@AllArgsConstructor
@SupportedValidationTarget(ValidationTarget.PARAMETERS)

public class AssessmentUpdateValidator implements ConstraintValidator<AssessmentCreate, Object[]> {

    private MessageSource messageSource;

    private AssessmentService assessmentService;

    private AssessmentValidator assessmentValidator;

    @Override
    public boolean isValid(Object[] args, ConstraintValidatorContext constraintValidatorContext) {

        if (!(args[0] instanceof AssessmentUpdateDto)
                || !(args[1] instanceof String)) {
            throw new IllegalArgumentException(messageSource.getMessage("assessment_update.invalid_method_parameters", null, null));
        }

       AssessmentUpdateDto updateDto = (AssessmentUpdateDto) args[0];
        UUID assessmentUuid = UUID.fromString((String) args[1]);
        Assessment assessment = assessmentService.assessmentFromUpdateDto(updateDto, assessmentUuid);

        return assessmentValidator.isValid(assessment, constraintValidatorContext);
    }
}

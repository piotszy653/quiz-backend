package projects.core.utils.validator.assessment;

import lombok.AllArgsConstructor;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class AssessmentOwnerValidator implements ConstraintValidator<AssessmentOwner, UUID> {

    private UserService userService;

    private AssessmentService assessmentService;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {

        Assessment assessment = assessmentService.getByUuid(uuid);
        return assessment.getOwnerUuid().equals(userService.getCurrentUserUuid());
    }
}

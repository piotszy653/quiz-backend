package projects.quiz.utils.validator.assessment;

import lombok.AllArgsConstructor;
import projects.quiz.service.AssessmentService;
import projects.quiz.utils.enums.QuestionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
public class AssessmentsExistValidator implements ConstraintValidator<AssessmentsExist, HashMap<QuestionType, UUID>> {

    private AssessmentService assessmentService;

    @Override
    public boolean isValid(HashMap<QuestionType, UUID> uuids, ConstraintValidatorContext constraintValidatorContext) {

        if (uuids == null)
            return true;

        uuids.values().forEach(uuid -> {
            if (uuid != null)
                assessmentService.getByUuid(uuid);
        });

        return true;
    }
}

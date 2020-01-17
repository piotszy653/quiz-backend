package projects.quiz.utils.validator.answer;

import lombok.AllArgsConstructor;
import projects.quiz.service.AnswerService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
public class AnswersExistValidator implements ConstraintValidator<AnswersExist, HashMap<UUID, Boolean>> {

    private AnswerService answerService;

    @Override
    public boolean isValid(HashMap<UUID, Boolean> uuids, ConstraintValidatorContext constraintValidatorContext) {

        if (uuids == null)
            return true;

        uuids.keySet().forEach(uuid -> answerService.getByUuid(uuid));

        return true;
    }
}

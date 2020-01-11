package projects.quiz.utils.validator.answer;

import lombok.AllArgsConstructor;
import projects.quiz.service.AnswerService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

@AllArgsConstructor
public class AnswersExistValidator implements ConstraintValidator<AnswersExist, HashMap<String, Boolean>> {

    private AnswerService answerService;

    @Override
    public boolean isValid(HashMap<String, Boolean> uuids, ConstraintValidatorContext constraintValidatorContext) {

        uuids.keySet().forEach(uuid -> answerService.getByUuid(UUID.fromString(uuid)));

        return true;
    }
}

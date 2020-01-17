package projects.quiz.utils.validator.question;

import lombok.AllArgsConstructor;
import projects.quiz.service.QuestionService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class QuestionsExistValidator implements ConstraintValidator<QuestionsExist, Set<UUID>> {

    private QuestionService questionService;

    @Override
    public boolean isValid(Set<UUID> uuids, ConstraintValidatorContext constraintValidatorContext) {

        if (uuids == null)
            return true;

        uuids.forEach(uuid -> questionService.getByUuid(uuid));

        return true;
    }
}

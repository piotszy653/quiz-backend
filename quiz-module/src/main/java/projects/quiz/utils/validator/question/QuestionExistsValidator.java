package projects.quiz.utils.validator.question;

import lombok.AllArgsConstructor;
import projects.quiz.service.QuestionService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class QuestionExistsValidator implements ConstraintValidator<QuestionExists, UUID> {

    private QuestionService questionService;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {

        if(uuid == null)
            return true;

        questionService.getByUuid(uuid);
        return true;
    }
}

package projects.quiz.utils.validator.quiz;

import lombok.AllArgsConstructor;
import projects.quiz.service.QuizService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class QuizExistsValidator implements ConstraintValidator<QuizExists, String> {

    private QuizService quizService;

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext constraintValidatorContext) {

        if(uuid == null)
            return true;

        quizService.getByUuid(UUID.fromString(uuid));
        return true;
    }
}

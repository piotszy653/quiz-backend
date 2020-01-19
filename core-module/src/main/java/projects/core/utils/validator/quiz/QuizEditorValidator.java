package projects.core.utils.validator.quiz;

import lombok.AllArgsConstructor;
import projects.quiz.service.QuizService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class QuizEditorValidator implements ConstraintValidator<QuizEditor, UUID> {

    private QuizService quizService;

    private UserService userService;

    @Override
    public boolean isValid(UUID quizUuid, ConstraintValidatorContext constraintValidatorContext) {
        return quizUuid == null || quizService.getByUuid(quizUuid).isEditor(userService.getCurrentUserUuid());
    }
}

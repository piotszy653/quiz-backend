package projects.core.utils.validator.quiz;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.Question;
import projects.quiz.service.QuizService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
public class QuizOwnerValidator implements ConstraintValidator<QuizOwner, String> {

    private MessageSource messageSource;

    private UserService userService;

    private QuizService quizService;

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext constraintValidatorContext) {

        Quiz quiz = quizService.getByUuid(UUID.fromString(uuid));
        if(!quiz.getOwnerUuid().equals(userService.getCurrentUserUuid()))
            throw new NoSuchElementException(messageSource.getMessage("quiz.not_found.uuid", new Object[]{uuid}, null));
        return true;
    }
}

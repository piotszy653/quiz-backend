package projects.core.utils.validator.question;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import projects.quiz.model.question.Question;
import projects.quiz.service.QuestionService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
public class QuestionOwnerValidator implements ConstraintValidator<QuestionOwner, UUID> {

    private MessageSource messageSource;

    private UserService userService;

    private QuestionService questionService;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {

        Question question = questionService.getByUuid(uuid);
        if (!question.getOwnerUuid().equals(userService.getCurrentUserUuid()))
            throw new NoSuchElementException(messageSource.getMessage("question.not_found.uuid", new Object[]{uuid}, null));
        return true;
    }
}

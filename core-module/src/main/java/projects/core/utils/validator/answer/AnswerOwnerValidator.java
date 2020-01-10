package projects.core.utils.validator.answer;

import lombok.AllArgsConstructor;
import projects.quiz.model.Answer;
import projects.quiz.service.AnswerService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class AnswerOwnerValidator implements ConstraintValidator<AnswerOwner, String> {

    private UserService userService;

    private AnswerService answerService;

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext constraintValidatorContext) {

        Answer answer = answerService.getByUuid(UUID.fromString(uuid));
        return answer.getOwnerUuid().equals(userService.getCurrentUserUuid());
    }
}

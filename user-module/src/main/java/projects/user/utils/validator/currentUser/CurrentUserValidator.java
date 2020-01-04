package projects.user.utils.validator.currentUser;

import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class CurrentUserValidator implements ConstraintValidator<CurrentUser, Long> {

    private UserService userService;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        return userService.isCurrentUsersId(userId);
    }
}

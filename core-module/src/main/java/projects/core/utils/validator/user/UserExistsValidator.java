package projects.core.utils.validator.user;

import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UserExistsValidator implements ConstraintValidator<UserExists, Long> {

    private UserService userService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return (id == null) || userService.existsById(id);
    }
}

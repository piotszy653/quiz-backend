package projects.user.utils.validator;

import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class UserExistsValidator implements ConstraintValidator<UserExists, UUID> {

    private UserService userService;
    
    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext context) {
        return uuid == null || userService.existsByUuid(uuid);
    }
}

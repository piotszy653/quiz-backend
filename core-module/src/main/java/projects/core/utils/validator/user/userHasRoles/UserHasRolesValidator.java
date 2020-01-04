package projects.core.utils.validator.user.userHasRoles;

import projects.core.config.enums.roles.RolesEnum;
import projects.user.model.user.User;
import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class UserHasRolesValidator implements ConstraintValidator<UserHasRoles, Long> {

    private UserService userService;

    private List<RolesEnum> roles;


    @Override
    public void initialize(UserHasRoles constraintAnnotation) {
        roles = Arrays.asList(constraintAnnotation.roles());
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        if (userId == null)
            return true;
        User user = userService.findOne(userId);
        return roles.contains(RolesEnum.valueOf(user.getRoleGroup().getName()));
    }
}

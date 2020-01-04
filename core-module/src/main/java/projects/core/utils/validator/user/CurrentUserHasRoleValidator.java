package projects.core.utils.validator.user;

import projects.core.config.enums.roles.RolesEnum;
import projects.user.model.user.User;
import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CurrentUserHasRoleValidator implements ConstraintValidator<CurrentUserHasRole, Object> {

    private UserService userService;

    private List<RolesEnum> roles;


    @Override
    public void initialize(CurrentUserHasRole constraintAnnotation) {
        roles = Arrays.asList(constraintAnnotation.roles());
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null)
            return true;
        User user = userService.getCurrentUser();
        return roles.contains(RolesEnum.valueOf(user.getRoleGroup().getName()));
    }
}

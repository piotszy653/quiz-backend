package projects.user.utils.validator.groupAllowed;

import projects.user.service.UserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class GroupAllowedValidator implements ConstraintValidator<GroupAllowed, String> {

    private UserService userService;

    @Override
    public boolean isValid(String roleGroupName, ConstraintValidatorContext constraintValidatorContext) {
        if (roleGroupName == null) {
            return true;
        } else {
            switch (roleGroupName) {
                case "ADMIN":
                    return checkForAdminCreation();
                case "MANAGER":
                    return checkForManagerCreation();
                case "BARTENDER":
                    return checkForBartenderCreation();
                case "USER":
                    return checkForUserCreation();
            }
            return false;
        }
    }

    private boolean checkForAdminCreation() {
        return userService.getCurrentUser().isAdmin();
    }

    private boolean checkForManagerCreation() {
        return userService.getCurrentUser().isAdmin() || userService.getCurrentUser().isManager();
    }

    private boolean checkForBartenderCreation() {
        return userService.getCurrentUser().isAdmin() || userService.getCurrentUser().isManager();
    }

    private boolean checkForUserCreation() {
        return userService.getCurrentUser().isAdmin();
    }
}

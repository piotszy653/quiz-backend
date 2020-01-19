package projects.user.utils.validator.invitation;

import lombok.AllArgsConstructor;
import projects.user.service.InvitationService;
import projects.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class SelfInvitationValidator implements ConstraintValidator<SelfInvitation, UUID> {


    private UserService userService;
    
    @Override
    public boolean isValid(UUID invitedUserUuid, ConstraintValidatorContext context) {
        return invitedUserUuid == null || !userService.getCurrentUserUuid().equals(invitedUserUuid);
    }
}

package projects.user.utils.validator.invitation;

import lombok.AllArgsConstructor;
import projects.user.service.InvitationService;
import projects.user.service.UserService;
import projects.user.utils.validator.UserExists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@AllArgsConstructor
public class UniqueInvitationValidator implements ConstraintValidator<UniqueInvitation, UUID> {

    private InvitationService invitationService;

    private UserService userService;
    
    @Override
    public boolean isValid(UUID invitedUserUuid, ConstraintValidatorContext context) {
        return invitedUserUuid == null || !invitationService.existsByUsers(userService.getCurrentUserUuid(), invitedUserUuid);
    }
}

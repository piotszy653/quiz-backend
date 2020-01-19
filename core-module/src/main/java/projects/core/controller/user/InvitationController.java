package projects.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.user.model.user.Invitation;
import projects.user.service.InvitationService;
import projects.user.utils.validator.invitation.SelfInvitation;
import projects.user.utils.validator.invitation.UniqueInvitation;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/invitations")
public class InvitationController {


    private final InvitationService invitationService;

    @GetMapping("/{uuid}")
    public Invitation getByUuid(@PathVariable UUID uuid) {
        return invitationService.getByUuid(uuid);
    }

    @PostMapping("/{invitedUserUuid}")
    public Invitation create(@Valid @SelfInvitation @UniqueInvitation @PathVariable UUID invitedUserUuid) {
        return invitationService.create(invitedUserUuid);
    }

    @PutMapping("/accept/{invitingUserUuid}")
    public void acceptInvitation(@PathVariable UUID invitingUserUuid) {
        invitationService.acceptInvitation(invitingUserUuid);
    }

    @DeleteMapping("/decline/{invitingUserUuid}")
    public void declineInvitation(@PathVariable UUID invitingUserUuid) {
        invitationService.declineInvitation(invitingUserUuid);
    }

    @DeleteMapping("/{invitedUserUuid}")
    public void delete(@PathVariable UUID invitedUserUuid) {
        invitationService.delete(invitedUserUuid);
    }
}

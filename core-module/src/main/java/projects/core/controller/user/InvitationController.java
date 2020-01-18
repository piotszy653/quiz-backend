package projects.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import projects.user.model.user.Invitation;
import projects.user.service.InvitationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class InvitationController {


    private final InvitationService invitationService;

    @GetMapping("/{uuid}")
    public Invitation getByUuid(@PathVariable UUID uuid){
        return invitationService.getByUuid(uuid);
    }

    @PostMapping("/{invitedUserUuid}")
    public Invitation create(@PathVariable UUID invitedUserUuid){
        return invitationService.create(invitedUserUuid);
    }

    @PutMapping("/accept/{invitingUserUuid}")
    public void acceptInvitation(@PathVariable UUID invitingUserUuid){
        invitationService.acceptInvitation(invitingUserUuid);
    }

    @DeleteMapping("/decline/{invitingUserUuid}")
    public void declineInvitation(@PathVariable UUID invitingUserUuid){
        invitationService.declineInvitation(invitingUserUuid);
    }

    @DeleteMapping("/{invitedUserUuid}")
    public void delete(@PathVariable UUID invitedUserUuid){
        invitationService.delete(invitedUserUuid);
    }
}

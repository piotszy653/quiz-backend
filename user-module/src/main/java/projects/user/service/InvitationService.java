package projects.user.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.user.model.user.Invitation;
import projects.user.model.user.User;
import projects.user.repository.user.InvitationRepository;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Validated
public class InvitationService {

    private final MessageSource messageSource;

    private final InvitationRepository invitationRepository;

    private final UserService userService;

    public Set<User> getInvitedUsers(){
        return getAllByInvitingUser(userService.getCurrentUserUuid()).stream().map(Invitation::getInvitedUser).collect(Collectors.toSet());
    }

    public Set<User> getInvitingUsers(){
        return getAllByInvitedUser(userService.getCurrentUserUuid()).stream().map(Invitation::getInvitingUser).collect(Collectors.toSet());
    }

    public Invitation getByUuid(UUID uuid){
        return invitationRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("invitation.not_found.uuid", new Object[]{uuid}, null)));
    }

    public Invitation getByUsers(UUID invitingUserUuid, UUID invitedUserUuid){
        return invitationRepository.findFirstByInvitingUser_UuidAndInvitedUser_Uuid(invitingUserUuid, invitedUserUuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("invitation.not_found", null, null)));
    }

    public LinkedHashSet<Invitation> getAllByInvitingUser(UUID uuid){
        return invitationRepository.findAllByInvitingUser_Uuid(uuid);
    }

    public LinkedHashSet<Invitation> getAllByInvitedUser(UUID uuid){
        return invitationRepository.findAllByInvitedUser_Uuid(uuid);
    }

    @Transactional
    public Invitation create(UUID invitedUserUuid){
        return save(
                new Invitation(
                        userService.getCurrentUser(),
                        userService.findByUuid(invitedUserUuid)
                )
        );
    }

    @Transactional
    public Invitation save(@Valid Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    @Transactional
    public void acceptInvitation(UUID invitingUserUuid){

        Invitation invitation = getByUsers(invitingUserUuid, userService.getCurrentUserUuid());
        User invitingUser = invitation.getInvitingUser();
        User invitedUser = invitation.getInvitedUser();

        invitingUser.getProfile().getFriends().add(invitedUser);
        invitedUser.getProfile().getFriends().add(invitingUser);

        userService.saveAll(invitingUser, invitedUser);
        delete(invitation);
    }

    @Transactional
    public void declineInvitation(UUID invitingUserUuid) {
        delete(getByUsers(invitingUserUuid, userService.getCurrentUserUuid()));
    }

    @Transactional
    public void deleteByUsers(UUID invitingUserUuid, UUID invitedUserUuid){
        delete(getByUsers(invitingUserUuid, invitedUserUuid));
    }

    @Transactional
    public void delete(UUID invitedUserUuid) {
        delete(getByUsers(userService.getCurrentUserUuid(), invitedUserUuid));
    }

    @Transactional
    public void delete(Invitation invitation){
        invitationRepository.delete(invitation);
    }
}

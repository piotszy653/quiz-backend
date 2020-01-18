package projects.user.repository.user;

import org.springframework.data.repository.CrudRepository;
import projects.user.model.user.Invitation;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Optional<Invitation> findByUuid(UUID uuid);

    Optional<Invitation> findFirstByInvitingUser_UuidAndInvitedUser_Uuid(UUID invitingUserUuid, UUID invitedUserUuid);

    LinkedHashSet<Invitation> findAllByInvitingUser_Uuid(UUID uuid);

    LinkedHashSet<Invitation> findAllByInvitedUser_Uuid(UUID uuid);
}

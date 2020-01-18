package projects.user.repository.user;

import org.springframework.data.repository.CrudRepository;
import projects.user.model.user.UserProfile;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
}

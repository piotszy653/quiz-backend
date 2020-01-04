package projects.user.repository;


import projects.user.model.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {

    Optional<ResetPasswordToken> findByUuid(String uuid);
}

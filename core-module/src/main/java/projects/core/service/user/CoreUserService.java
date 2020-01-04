package projects.core.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.user.service.UserService;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreUserService {

    UserService userService;

    @Transactional
    public void delete(long id) {
        userService.delete(id);
    }
}

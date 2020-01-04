package projects.core.service.init;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projects.core.config.enums.bootstrap.userModule.DefaultUsersEnum;
import projects.core.config.init.BootstrapPartName;
import projects.user.model.user.User;
import projects.user.repository.roles.RoleGroupRepository;
import projects.user.repository.roles.RoleRepository;
import projects.user.repository.user.UserRepository;


@Slf4j
@Profile("dev")
@Service
@EqualsAndHashCode(callSuper = true)
public class DevelopmentBootstrapService extends BootstrapService {

    @Builder

    public DevelopmentBootstrapService(RoleGroupRepository roleGroupRepository, RoleRepository roleRepository, UserRepository userRepository,
                                       BootstrapPartService bootstrapPartService
    ) {
        super(roleGroupRepository, roleRepository, userRepository, bootstrapPartService);

    }

    @Override
    public void setup() {
        log.info("Bootstrap development mode init");
        boot();
    }

    public void boot() {
        super.boot();
        bootstrapPartService.create(BootstrapPartName.DEFAULT_USERS, this::createDefaultUsers);
    }


    private void createDefaultUsers() {
        for (DefaultUsersEnum value : DefaultUsersEnum.values()) {

            User user = new User(
                    value.username,
                    new BCryptPasswordEncoder().encode(value.password),
                    roleGroupRepository.findByName(value.rolesEnum.name())
            );
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}

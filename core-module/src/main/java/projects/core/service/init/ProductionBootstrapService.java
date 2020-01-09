package projects.core.service.init;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projects.core.config.enums.bootstrap.userModule.DefaultAdminEnum;
import projects.quiz.repository.AssessmentRepository;
import projects.quiz.service.AssessmentService;
import projects.user.model.user.User;
import projects.user.repository.roles.RoleGroupRepository;
import projects.user.repository.roles.RoleRepository;
import projects.user.repository.user.UserRepository;

@Slf4j
@Profile("prod")
@Service
@EqualsAndHashCode(callSuper = true)
public class ProductionBootstrapService extends BootstrapService {

    @Builder
    public ProductionBootstrapService(RoleGroupRepository roleGroupRepository, RoleRepository roleRepository, UserRepository userRepository,
                                      BootstrapPartService bootstrapPartService, AssessmentRepository assessmentRepository
    ) {
        super(roleGroupRepository, roleRepository, userRepository, bootstrapPartService, assessmentRepository);
    }

    @Override
    public void setup() {
        log.info("Bootstrap production mode init");
        boot();
    }

    public void boot() {
        super.boot();
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        User user = new User(
                DefaultAdminEnum.ADMIN.username,
                new BCryptPasswordEncoder().encode(DefaultAdminEnum.ADMIN.password),
                roleGroupRepository.findByName(DefaultAdminEnum.ADMIN.rolesEnum.name())
        );
        user.setEnabled(true);
        userRepository.save(user);
    }
}

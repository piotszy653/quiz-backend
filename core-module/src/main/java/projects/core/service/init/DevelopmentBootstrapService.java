package projects.core.service.init;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projects.core.config.enums.bootstrap.quizModule.DefaultAssessments;
import projects.core.config.enums.bootstrap.userModule.DefaultUsersEnum;
import projects.core.config.init.BootstrapPartName;
import projects.quiz.model.Assessment;
import projects.quiz.repository.AssessmentRepository;
import projects.user.model.user.User;
import projects.user.repository.roles.RoleGroupRepository;
import projects.user.repository.roles.RoleRepository;
import projects.user.repository.user.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@Profile("dev")
@Service
@EqualsAndHashCode(callSuper = true)
public class DevelopmentBootstrapService extends BootstrapService {

    @Builder

    public DevelopmentBootstrapService(RoleGroupRepository roleGroupRepository, RoleRepository roleRepository, UserRepository userRepository,
                                       BootstrapPartService bootstrapPartService, AssessmentRepository assessmentRepository
    ) {
        super(roleGroupRepository, roleRepository, userRepository, bootstrapPartService, assessmentRepository);

    }

    @Override
    public void setup() {
        log.info("Bootstrap development mode init");
        boot();
    }

    public void boot() {
        super.boot();
        bootstrapPartService.create(BootstrapPartName.DEFAULT_USERS, this::createDefaultUsers);
        bootstrapPartService.create(BootstrapPartName.DEFAULT_ASSESSMENTS, this::createDefaultAssessments);
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

    private void createDefaultAssessments() {
        Optional<User> user = userRepository.findById(2L);
        Stream.of(DefaultAssessments.values()).forEach(value ->
                assessmentRepository.save(new Assessment(
                        value.name,
                        user.get().getUuid(),
                        value.correctRate != null ? new BigDecimal(value.correctRate) : null,
                        value.incorrectRate != null ? new BigDecimal(value.incorrectRate) : null,
                        value.minPoints != null ? new BigDecimal(value.minPoints) : null,
                        value.maxPoints != null ? new BigDecimal(value.maxPoints) : null
                ))
        );
    }
}

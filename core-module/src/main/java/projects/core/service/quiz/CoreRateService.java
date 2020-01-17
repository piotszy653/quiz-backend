package projects.core.service.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.rate.RateDto;
import projects.quiz.model.Rate;
import projects.quiz.service.RateService;
import projects.quiz.utils.enums.ObjectType;
import projects.user.service.UserService;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreRateService {

    private final RateService rateService;

    private final UserService userService;

    public Set<Rate> getAllByUser() {
        return rateService.getAllByUserUuid(userService.getCurrentUserUuid());
    }

    public Set<Rate> getAllByUserAndObject(UUID objectUuid) {
        return rateService.getAllByUserAndObject(userService.getCurrentUserUuid(), objectUuid);
    }

    @Transactional
    public Rate create(RateDto dto, UUID objectUuid, ObjectType type) {
        return rateService.create(dto, objectUuid, type, userService.getCurrentUserUuid());
    }

}

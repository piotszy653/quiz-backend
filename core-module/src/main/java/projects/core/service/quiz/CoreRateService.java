package projects.core.service.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.rate.RateCreateDto;
import projects.quiz.model.Rate;
import projects.quiz.service.RateService;
import projects.quiz.utils.enums.ObjectType;
import projects.user.service.UserService;

import java.util.Set;

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

    public Rate create(RateCreateDto dto, ObjectType type) {
        return rateService.create(dto, type, userService.getCurrentUserUuid());
    }
}

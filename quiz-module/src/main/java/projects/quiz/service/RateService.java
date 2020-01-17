package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.rate.RateDto;
import projects.quiz.model.Rate;
import projects.quiz.repository.RateRepository;
import projects.quiz.utils.enums.ObjectType;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class RateService {

    private final MessageSource messageSource;

    private final RateRepository rateRepository;

    public Rate getByUuid(UUID uuid) {
        return rateRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("rate.not_found.uuid", new Object[]{uuid}, null)));
    }

    public Rate getByUuid(String uuid) {
        return getByUuid(UUID.fromString(uuid));
    }

    public LinkedHashSet<Rate> getAllByUserUuid(String userUuid) {
        return getAllByUserUuid(UUID.fromString(userUuid));
    }

    public LinkedHashSet<Rate> getAllByUserUuid(UUID userUuid) {
        return rateRepository.findAllByUserUuid(userUuid);
    }

    public LinkedHashSet<Rate> getAllByObjectUuid(String ratedObjectUuid) {
        return getAllByObjectUuid(UUID.fromString(ratedObjectUuid));
    }

    public LinkedHashSet<Rate> getAllByObjectUuid(UUID objectUuid) {
        return rateRepository.findAllByRatedObjectUuid(objectUuid);
    }

    public Set<Rate> getAllByUserAndObject(UUID userUuid, UUID quizUuid) {
        return  rateRepository.findAllByUserUuidAndRatedObjectUuid(userUuid, quizUuid);
    }

    @Transactional
    public Rate create(RateDto dto, UUID objectUuid, ObjectType type, UUID userUuid) {
        return save(new Rate(dto, objectUuid, type, userUuid));
    }

    @Transactional
    public Rate update(RateDto dto, UUID uuid) {
        Rate rate = getByUuid(uuid);
        updateRate(rate, dto);
        return save(rate);
    }

    @Transactional
    public Rate save(@Valid Rate rate) {
        return rateRepository.save(rate);
    }

    @Transactional
    public void delete(String uuid) {
        delete(UUID.fromString(uuid));
    }

    @Transactional
    public void delete(UUID uuid) {
        rateRepository.delete(getByUuid(uuid));
    }

    private void updateRate(Rate rate, RateDto dto) {
        rate.setRate(dto.getRate() != null ? dto.getRate() : rate.getRate());
        rate.setOpinion(dto.getOpinion() != null ? dto.getOpinion() : rate.getOpinion());
    }
}

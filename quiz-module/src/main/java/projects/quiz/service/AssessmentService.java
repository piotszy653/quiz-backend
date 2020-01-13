package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.assessment.AssessmentCreateDto;
import projects.quiz.dto.assessment.AssessmentUpdateDto;
import projects.quiz.model.Assessment;
import projects.quiz.repository.AssessmentRepository;

import javax.validation.Valid;
import java.util.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class AssessmentService {

    private AssessmentRepository assessmentRepository;

    private MessageSource messageSource;

    public Assessment getById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("assessment.not_found.id", new Object[]{id}, null)));
    }

    public Assessment getByUuid(UUID uuid) {
        return assessmentRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("assessment.not_found.uuid", new Object[]{uuid}, null)));
    }

    public Set<Assessment> getAllByUuids(Collection<UUID> uuids) {
        return assessmentRepository.findAllByUuid(uuids);
    }

    public LinkedHashSet<Assessment> getByOwnerUuid(UUID ownerUuid) {
        return assessmentRepository.findAllByOwnerUuid(ownerUuid);
    }

    @Transactional
    public Assessment create(@Valid AssessmentCreateDto assessmentCreateDto, UUID ownerUuid) {
        return save(new Assessment(assessmentCreateDto, ownerUuid));
    }

    @Transactional
    public Assessment save(@Valid Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @Transactional
    public Assessment update(@Valid AssessmentUpdateDto dto, UUID uuid) {
        return save(assessmentFromUpdateDto(dto, uuid));
    }

    @Transactional
    public void delete(UUID uuid) {
        assessmentRepository.delete(getByUuid(uuid));
    }

    @Transactional
    public void deleteAll(Collection<Assessment> assessments) {
        assessmentRepository.deleteAll(assessments);
    }

    @Transactional
    public void deleteAllByOwnerUuid(UUID ownerUuid) {
        assessmentRepository.deleteAllByOwnerUuid(ownerUuid);
    }

    public Assessment assessmentFromUpdateDto(AssessmentUpdateDto dto, UUID uuid) {
        Assessment assessment = getByUuid(uuid);

        assessment.setName(dto.getName() != null ? dto.getName() : assessment.getName());
        assessment.setCorrectRate(dto.getCorrectRate() != null ? dto.getCorrectRate() : assessment.getCorrectRate());
        assessment.setIncorrectRate(dto.getIncorrectRate() != null ? dto.getIncorrectRate() : assessment.getIncorrectRate());
        assessment.setMinPoints(dto.getMinPoints() != null ? dto.getMinPoints() : assessment.getMinPoints());
        assessment.setMaxPoints(dto.getMaxPoints() != null ? dto.getMaxPoints() : assessment.getMaxPoints());

        return assessment;
    }
}

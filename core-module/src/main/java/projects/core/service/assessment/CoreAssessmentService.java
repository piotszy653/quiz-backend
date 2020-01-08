package projects.core.service.assessment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.assessment.AssessmentCreateDto;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;
import projects.user.service.UserService;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreAssessmentService {

    private UserService userService;

    private AssessmentService assessmentService;

    public Set<Assessment> getByOwner() {
        return assessmentService.getByOwnerUuid(userService.getCurrentUserUuid());
    }

    @Transactional
    public Assessment create(AssessmentCreateDto assessmentCreateDto) {
        return assessmentService.create(assessmentCreateDto, userService.getCurrentUserUuid());
    }
}

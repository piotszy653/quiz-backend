package projects.core.service.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.assessment.AssessmentCreateDto;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;
import projects.user.service.UserService;

import java.util.LinkedHashSet;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreAssessmentService {

    private UserService userService;

    private AssessmentService assessmentService;

    public LinkedHashSet<Assessment> getByOwner() {
        return assessmentService.getByOwnerUuid(userService.getCurrentUserUuid());
    }

    @Transactional
    public Assessment create(AssessmentCreateDto assessmentCreateDto) {
        return assessmentService.create(assessmentCreateDto, userService.getCurrentUserUuid());
    }
}

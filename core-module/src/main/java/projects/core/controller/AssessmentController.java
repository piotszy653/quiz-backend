package projects.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.assessment.CoreAssessmentService;
import projects.core.utils.validator.assessment.AssessmentOwner;
import projects.quiz.dto.assessment.AssessmentCreateDto;
import projects.quiz.dto.assessment.AssessmentUpdateDto;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final CoreAssessmentService coreAssessmentService;

    private final AssessmentService assessmentService;

    @Secured("ROLE_ASSESSMENT_READ")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Assessment getByUuid(@Valid @AssessmentOwner @PathVariable String uuid) {
        return assessmentService.getByUuid(UUID.fromString(uuid));
    }

    @Secured("ROLE_ASSESSMENT_READ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkedHashSet<Assessment> getByOwner() {
        return coreAssessmentService.getByOwner();
    }

    @Secured("ROLE_ASSESSMENT_CREATE")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Assessment create(@Valid @RequestBody AssessmentCreateDto assessmentCreateDto) {
        return coreAssessmentService.create(assessmentCreateDto);
    }

    @Secured("ROLE_ASSESSMENT_UPDATE")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Assessment update(@Valid @RequestBody AssessmentUpdateDto assessmentUpdateDto, @Valid @AssessmentOwner @PathVariable String uuid) {
        return assessmentService.update(assessmentUpdateDto, UUID.fromString(uuid));
    }

    @Secured("ROLE_ASSESSMENT_DELETE")
    @DeleteMapping(value = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @AssessmentOwner @PathVariable String uuid) {
        assessmentService.delete(UUID.fromString(uuid));
    }

}
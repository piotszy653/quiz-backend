package projects.core.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.quiz.CoreQuizService;
import projects.core.service.quiz.CoreRateService;
import projects.core.service.quiz.CoreResultService;
import projects.core.utils.validator.quiz.QuizOwner;
import projects.quiz.dto.quiz.QuizCreateDto;
import projects.quiz.dto.quiz.QuizUpdateDto;
import projects.quiz.model.Quiz;
import projects.quiz.model.Result;
import projects.quiz.service.QuizService;
import projects.quiz.service.ResultService;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/quizzes")
public class QuizController {

    private final CoreQuizService coreQuizService;

    private final QuizService quizService;

    private final ResultService resultService;

    private final CoreResultService coreResultService;

    private final CoreRateService coreRateService;

    @Secured("ROLE_QUIZ_READ")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Quiz getByUuid(@Valid @QuizOwner @PathVariable String uuid) {
        return quizService.getByUuid(UUID.fromString(uuid));
    }

    @Secured("ROLE_QUIZ_READ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkedHashSet<Quiz> getByOwner() {
        return coreQuizService.getByOwner();
    }

    @Secured("ROLE_QUIZ_CREATE")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Quiz create(@Valid @RequestBody QuizCreateDto quizCreateDto) {
        return coreQuizService.create(quizCreateDto);
    }

    @Secured("ROLE_QUIZ_UPDATE")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Quiz update(@Valid @RequestBody QuizUpdateDto quizUpdateDto, @Valid @QuizOwner @PathVariable String uuid) {
        return coreQuizService.update(quizUpdateDto, uuid);
    }

    @Secured("ROLE_QUIZ_UPDATE")
    @PutMapping("/remove-image/{quizUuid}")
    @ResponseStatus(HttpStatus.OK)
    public void removeImage(@Valid @QuizOwner @PathVariable String quizUuid) {
        quizService.removeImage(UUID.fromString(quizUuid));
    }

    @Secured("ROLE_QUIZ_DELETE")
    @DeleteMapping(value = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @QuizOwner @PathVariable String uuid) {
        quizService.delete(UUID.fromString(uuid));
    }

    @GetMapping("/results/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Result> getResults(@PathVariable String uuid) {
        resultService.deleteByQuizUuid(uuid);
        return resultService.getByQuizUuid(uuid);
    }

    @GetMapping("/results-by-user/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Result> getResultsByUser(@PathVariable String uuid) {
        return coreResultService.getByQuizAndUser(uuid);
    }
}
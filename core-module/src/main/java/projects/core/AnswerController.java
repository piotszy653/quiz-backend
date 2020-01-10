package projects.core;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.answer.CoreAnswerService;
import projects.core.utils.validator.answer.AnswerOwner;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.model.Answer;
import projects.quiz.service.AnswerService;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/answers")
public class AnswerController {

    private final CoreAnswerService coreAnswerService;

    private final AnswerService answerService;

    @Secured("ROLE_ANSWER_READ")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Answer getByUuid(@Valid @AnswerOwner @PathVariable String uuid) {
        return answerService.getByUuid(UUID.fromString(uuid));
    }

    @Secured("ROLE_ANSWER_READ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkedHashSet<Answer> getByOwner() {
        return coreAnswerService.getByOwner();
    }

    @Secured("ROLE_ANSWER_CREATE")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Answer create(@Valid @RequestBody AnswerDto answerDto) {
        return coreAnswerService.create(answerDto);
    }

    @Secured("ROLE_ANSWER_UPDATE")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Answer update(@Valid @RequestBody AnswerDto answerDto, @Valid @AnswerOwner @PathVariable String uuid) {
        return coreAnswerService.update(answerDto, UUID.fromString(uuid));
    }

    @Secured("ROLE_ANSWER_DELETE")
    @DeleteMapping(value = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @AnswerOwner @PathVariable String uuid) {
        answerService.delete(UUID.fromString(uuid));
    }

}
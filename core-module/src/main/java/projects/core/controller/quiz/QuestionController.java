package projects.core.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.quiz.CoreQuestionService;
import projects.core.utils.validator.question.QuestionOwner;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.quiz.dto.question.openQuestion.OpenQuestionUpdateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionUpdateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionUpdateDto;
import projects.quiz.model.question.OpenQuestion;
import projects.quiz.model.question.Question;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;
import projects.quiz.service.QuestionService;
import projects.quiz.utils.enums.QuestionType;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/questions")
public class QuestionController {

    private final CoreQuestionService coreQuestionService;

    private final QuestionService questionService;

    @Secured("ROLE_QUESTION_READ")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Question getByUuid(@Valid @QuestionOwner @PathVariable UUID uuid) {
        return questionService.getByUuid(uuid);
    }

    @Secured("ROLE_QUESTION_READ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkedHashSet<Question> getByOwner() {
        return coreQuestionService.getByOwner();
    }

    @Secured("ROLE_QUESTION_CREATE")
    @PostMapping("/open")
    @ResponseStatus(HttpStatus.CREATED)
    public OpenQuestion createOpenQuestion(@Valid @RequestBody OpenQuestionCreateDto openQuestionCreateDto) {
        return coreQuestionService.createOpenQuestion(openQuestionCreateDto);
    }

    @Secured("ROLE_QUESTION_CREATE")
    @PostMapping("/true-false")
    @ResponseStatus(HttpStatus.CREATED)
    public TrueFalseQuestion createTrueFalseQuestion(@Valid @RequestBody TrueFalseQuestionCreateDto trueFalseQuestionCreateDto) {
        return coreQuestionService.createTrueFalseQuestion(trueFalseQuestionCreateDto);
    }

    @Secured("ROLE_QUESTION_CREATE")
    @PostMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    public TestQuestion createTestQuestion(@Valid @RequestBody TestQuestionCreateDto testQuestionCreateDto) {
        return coreQuestionService.createTestQuestion(testQuestionCreateDto);
    }

    @Secured("ROLE_QUESTION_UPDATE")
    @PutMapping("/open/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public OpenQuestion updateOpenQuestion(@Valid @RequestBody OpenQuestionUpdateDto openQuestionUpdateDto, @Valid @QuestionOwner @PathVariable UUID uuid) {
        return coreQuestionService.updateOpenQuestion(openQuestionUpdateDto, uuid);
    }

    @Secured("ROLE_QUESTION_UPDATE")
    @PutMapping("/true-false/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public TrueFalseQuestion updateTrueFalseQuestion(@Valid @RequestBody TrueFalseQuestionUpdateDto trueFalseQuestionUpdateDto, @Valid @QuestionOwner @PathVariable UUID uuid) {
        return coreQuestionService.updateTrueFalseQuestion(trueFalseQuestionUpdateDto, uuid);
    }

    @PutMapping("/test/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public TestQuestion updateTestQuestion(@Valid @RequestBody TestQuestionUpdateDto testQuestionUpdateDto, @Valid @QuestionOwner @PathVariable UUID uuid) {
        return coreQuestionService.updateTestQuestion(testQuestionUpdateDto, uuid);
    }

    @Secured("ROLE_QUESTION_UPDATE")
    @PutMapping("/remove-image/{questionUuid}/{questionType}")
    @ResponseStatus(HttpStatus.OK)
    public void removeImage(@Valid @QuestionOwner @PathVariable UUID questionUuid, @PathVariable String questionType) {
        questionService.removeImage(
                QuestionType.getType(
                        questionType,
                        questionService.unsupportedTypeException(questionType)
                ),
                questionUuid
        );
    }

    @Secured("ROLE_QUESTION_DELETE")
    @DeleteMapping(value = "/{questionType}/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @QuestionOwner @PathVariable UUID uuid, @PathVariable String questionType) {
        questionService.delete(uuid, questionType.toUpperCase().replaceAll("-", "_"));
    }

}
package projects.core.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.quiz.CoreRateService;
import projects.quiz.dto.rate.RateDto;
import projects.quiz.model.Rate;
import projects.quiz.service.RateService;
import projects.quiz.utils.validator.question.QuestionExists;
import projects.quiz.utils.validator.quiz.QuizExists;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static projects.quiz.utils.enums.ObjectType.QUESTION;
import static projects.quiz.utils.enums.ObjectType.QUIZ;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/rates")
public class RateController {

    private final CoreRateService coreRateService;

    private final RateService rateService;

    @GetMapping
    public Set<Rate> getByObject(@RequestParam(value = "objectUuid") UUID uuid) {
        return rateService.getAllByObjectUuid(uuid);
    }

    @PostMapping("/quiz/{quizUuid}")
    @ResponseStatus(CREATED)
    public Rate createQuizRate(@Valid @RequestBody RateDto rateDto, @Valid @PathVariable @QuizExists UUID quizUuid) {
        return coreRateService.create(rateDto, quizUuid, QUIZ);
    }

    @PostMapping("/question/{questionUuid}")
    @ResponseStatus(CREATED)
    public Rate createQuestionRate(@Valid @RequestBody RateDto rateCreateDto, @Valid @PathVariable @QuestionExists UUID questionUuid) {
        return coreRateService.create(rateCreateDto, questionUuid, QUESTION);
    }

    @PutMapping("/{uuid}")
    public Rate updateRate(@Valid @RequestBody RateDto rateDto, @PathVariable UUID uuid) {
        return rateService.update(rateDto, uuid);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID uuid) {
        rateService.delete(uuid);
    }

}

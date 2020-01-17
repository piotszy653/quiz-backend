package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.result.ResultCreateDto;
import projects.quiz.model.Result;
import projects.quiz.repository.ResultRepository;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class ResultService {

    private final MessageSource messageSource;

    private final ResultRepository resultRepository;

    private final QuizService quizService;

    public Result getByUuid(UUID uuid) {
        return resultRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("result.not_found.uuid", new Object[]{uuid}, null)));
    }

    public LinkedHashSet<Result> getByUserUuid(UUID userUuid) {
        return resultRepository.findAllByUserUuid(userUuid);
    }

    public LinkedHashSet<Result> getByQuizUuid(UUID quizUuid) {
        return resultRepository.findAllByQuiz_Uuid(quizUuid);
    }

    public LinkedHashSet<Result> getUsersResultsInQuiz(UUID userUuid, UUID quizUuid) {
        return resultRepository.findAllByUserUuidAndQuiz_Uuid(userUuid, quizUuid);
    }

    @Transactional
    public Result create(ResultCreateDto dto, UUID userUuid) {
        return save(new Result(dto, userUuid, quizService.getByUuid(dto.getQuizUuid())));
    }

    @Transactional
    public Result save(@Valid Result result) {
        return resultRepository.save(result);
    }

    public void delete(UUID uuid) {
        delete(getByUuid(uuid));
    }

    @Transactional
    public void delete(Result result) {
        resultRepository.delete(result);
    }

    @Transactional
    public void deleteByQuizUuid(UUID uuid) {
        resultRepository.deleteAll(getByQuizUuid(uuid));
    }
}

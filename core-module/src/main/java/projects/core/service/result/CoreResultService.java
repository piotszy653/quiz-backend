package projects.core.service.result;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.result.ResultCreateDto;
import projects.quiz.model.Result;
import projects.quiz.service.ResultService;
import projects.user.service.UserService;

import java.util.Set;
import java.util.UUID;

@Service
@Validated
@AllArgsConstructor
@Transactional(readOnly = true)
public class CoreResultService{

    private final ResultService resultService;

    private final UserService userService;

    public Set<Result> getByQuizAndUser(String quizUuid){
        return resultService.getUsersResultsInQuiz(userService.getCurrentUserUuid(), UUID.fromString(quizUuid));
    }

    public Set<Result> getByUser(){
        return resultService.getByUserUuid(userService.getCurrentUserUuid());
    }

    @Transactional
    public Result create(ResultCreateDto dto){
        return resultService.create(dto, userService.getCurrentUserUuid());
    }
}

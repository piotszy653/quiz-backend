package projects.core.service.answer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.model.Answer;
import projects.quiz.service.AnswerService;
import projects.storage.model.FileData;
import projects.storage.service.FileDataService;
import projects.user.service.UserService;

import java.util.LinkedHashSet;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreAnswerService {

    private UserService userService;

    private AnswerService answerService;

    private FileDataService fileDataService;

    public LinkedHashSet<Answer> getByOwner() {
        return answerService.getByOwnerUuid(userService.getCurrentUserUuid());
    }

    @Transactional
    public Answer create(AnswerDto dto) {
        return answerService.create(
                dto,
                imageDataFromDto(dto)
        );
    }

    @Transactional
    public Answer update(AnswerDto dto, UUID uuid){
        return answerService.update(
                uuid,
                dto,
                imageDataFromDto(dto)
                );
    }

    private FileData imageDataFromDto(AnswerDto dto){
        return dto.getImageUuid() != null ? fileDataService.getByUuid(UUID.fromString(dto.getImageUuid())) : null;
    }
}

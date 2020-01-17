package projects.core.service.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.quiz.QuizCreateDto;
import projects.quiz.dto.quiz.QuizUpdateDto;
import projects.quiz.model.Quiz;
import projects.quiz.service.QuizService;
import projects.storage.model.FileData;
import projects.storage.service.FileDataService;
import projects.user.service.UserService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreQuizService {

    private final QuizService quizService;

    private final CoreQuestionService coreQuestionService;

    private final UserService userService;

    private final FileDataService fileDataService;


    public LinkedHashSet<Quiz> getByOwner(){
        return quizService.getAllByOwner(userService.getCurrentUserUuid());
    }

    @Transactional
    public Quiz create(QuizCreateDto dto) {

        LinkedList<FileData> openQuestionsImages = dto.getCreatedOpenQuestions().stream()
                .map(openQuestionDto -> fileDataService.getImageDataByUuid(openQuestionDto.getImageUuid()))
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<FileData> trueFalseQuestionsImages = dto.getCreatedTrueFalseQuestions().stream()
                .map(trueFalseQuestionDto -> fileDataService.getImageDataByUuid(trueFalseQuestionDto.getImageUuid()))
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<FileData> testQuestionsImages = dto.getCreatedTestQuestions().stream()
                .map(testQuestionDto -> fileDataService.getImageDataByUuid(testQuestionDto.getImageUuid()))
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<HashMap<String, FileData>> testAnswersImageDataMaps = dto.getCreatedTestQuestions().stream()
                .map(testQuestionDto ->  coreQuestionService.getAnswersImageUuidDataMap(testQuestionDto.getAnswers()))
                .collect(Collectors.toCollection(LinkedList::new));

        return quizService.create(
                dto,
                userService.getCurrentUserUuid(),
                fileDataService.getImageDataByUuid(dto.getImageUuid()),
                openQuestionsImages,
                trueFalseQuestionsImages,
                testQuestionsImages,
                testAnswersImageDataMaps
                );
    }

    @Transactional
    public Quiz update(QuizUpdateDto dto, String uuid){


        return quizService.update(
                dto,
                UUID.fromString(uuid),
                dto.getImageUuid() != null ? fileDataService.getImageDataByUuid(dto.getImageUuid()) : null
        );
    }
}

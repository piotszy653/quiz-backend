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
import projects.user.model.user.User;
import projects.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.PrivacyPolicy.FRIENDS;
import static projects.quiz.utils.enums.PrivacyPolicy.PUBLIC;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreQuizService {

    private final QuizService quizService;

    private final CoreQuestionService coreQuestionService;

    private final UserService userService;

    private final FileDataService fileDataService;


    public LinkedHashSet<Quiz> getAvailable() {
        User user = userService.getCurrentUser();
        LinkedHashSet<Quiz> quizzes = quizService.getAllByOwner(user.getUuid());
        quizzes.addAll(quizService.getAllFriends(user.getProfile().getFriends()));
        quizzes.addAll(quizService.getAllPublic());
        return quizzes;
    }

    public LinkedHashSet<Quiz> getAllByCurrentUser() {
        return quizService.getAllByOwner(userService.getCurrentUserUuid());
    }

    public Set<Quiz> getAvailableByOwnerUuid(UUID uuid) {
        Set<Quiz> quizzes = quizService.getAllByPrivacyAndOwner(PUBLIC, uuid);
        if(userService.findByUuid(uuid).getProfile().getFriends().contains(userService.getCurrentUserUuid())){
            quizzes.addAll(quizService.getAllByPrivacyAndOwner(FRIENDS, uuid));
        }
        return quizzes;
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

        LinkedList<HashMap<UUID, FileData>> testAnswersImageDataMaps = dto.getCreatedTestQuestions().stream()
                .map(testQuestionDto -> coreQuestionService.getAnswersImageUuidDataMap(testQuestionDto.getAnswers()))
                .collect(Collectors.toCollection(LinkedList::new));

        return quizService.create(
                dto,
                userService.getCurrentUserUuid(),
                userService.getCurrentUser().getUsername(),
                fileDataService.getImageDataByUuid(dto.getImageUuid()),
                openQuestionsImages,
                trueFalseQuestionsImages,
                testQuestionsImages,
                testAnswersImageDataMaps
        );
    }

    @Transactional
    public Quiz update(QuizUpdateDto dto, UUID uuid) {


        return quizService.update(
                dto,
                uuid,
                dto.getImageUuid() != null ? fileDataService.getImageDataByUuid(dto.getImageUuid()) : null
        );
    }
}

package projects.core.service.quiz;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.answer.AnswerDto;
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
import projects.storage.model.FileData;
import projects.storage.service.FileDataService;
import projects.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreQuestionService {

    private final QuestionService questionService;

    private final UserService userService;

    private final FileDataService fileDataService;

    public LinkedHashSet<Question> getByOwner() {
        return questionService.getByOwner(userService.getCurrentUserUuid());
    }

    @Transactional
    public OpenQuestion createOpenQuestion(OpenQuestionCreateDto dto) {

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());

        return questionService.createOpenQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight());
    }

    @Transactional
    public TrueFalseQuestion createTrueFalseQuestion(TrueFalseQuestionCreateDto dto) {

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());

        return questionService.createTrueFalseQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight());
    }

    @Transactional
    public TestQuestion createTestQuestion(TestQuestionCreateDto dto) {

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());

        HashMap<UUID, FileData> imageUuidDataMap = getAnswersImageUuidDataMap(dto.getAnswers());

        return questionService.createTestQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight(), imageUuidDataMap);
    }

    @Transactional
    public OpenQuestion updateOpenQuestion(OpenQuestionUpdateDto dto, UUID uuid) {
        return questionService.updateOpenQuestion(dto, uuid, fileDataService.getImageDataByUuid(dto.getImageUuid()));
    }

    @Transactional
    public TrueFalseQuestion updateTrueFalseQuestion(TrueFalseQuestionUpdateDto dto, UUID uuid) {
        return questionService.updateTrueFalseQuestion(dto, uuid, fileDataService.getImageDataByUuid(dto.getImageUuid()));
    }

    @Transactional
    public TestQuestion updateTestQuestion(TestQuestionUpdateDto dto, UUID uuid) {
        return questionService.updateTestQuestion(
                dto,
                uuid,
                fileDataService.getImageDataByUuid(dto.getImageUuid()),
                getAnswersImageUuidDataMap(dto.getAnswers())
        );
    }

    private Pair<UUID, FileData> getOwnerUuidAndImage(UUID imageUuid) {
        return Pair.of(
                userService.getCurrentUserUuid(),
                fileDataService.getImageDataByUuid(imageUuid)
        );
    }

    public HashMap<UUID, FileData> getAnswersImageUuidDataMap(LinkedHashSet<AnswerDto> answers){
        Set<UUID> imageUuids = answers.stream()
                .map(AnswerDto::getImageUuid)
                .collect(Collectors.toSet());
        imageUuids.remove(null);
        HashMap<UUID, FileData> imageUuidDataMap = new HashMap<>();

        imageUuids.forEach(uuid ->
                imageUuidDataMap.put(uuid, fileDataService.getByUuid(uuid))
        );
        return imageUuidDataMap;
    }
}

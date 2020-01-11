package projects.core.service.question;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.answer.TestAnswerDto;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.quiz.model.question.OpenQuestion;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;
import projects.quiz.service.QuestionService;
import projects.storage.model.FileData;
import projects.storage.service.FileDataService;
import projects.user.service.UserService;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class CoreQuestionService {

    private final QuestionService questionService;

    private final UserService userService;

    private final FileDataService fileDataService;

    @Transactional
    public OpenQuestion createOpenQuestion(OpenQuestionCreateDto dto){

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());

        return  questionService.createOpenQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight());
    }

    @Transactional
    public TrueFalseQuestion createTrueFalseQuestion(TrueFalseQuestionCreateDto dto){

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());

        return  questionService.createTrueFalseQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight());
    }

    @Transactional
    public TestQuestion createTestQuestion(TestQuestionCreateDto dto){

        Pair<UUID, FileData> ownerUuidAndImage = getOwnerUuidAndImage(dto.getImageUuid());
        Set<String> imageUuids = dto.getNewAnswers().stream().map(testAnswerDto -> testAnswerDto.getAnswerDto().getImageUuid()).collect(Collectors.toSet());
        HashMap<String, FileData> imageUuidDataMap = new HashMap<>();

        imageUuids.forEach(uuidString ->
                imageUuidDataMap.put(uuidString, fileDataService.getByUuid(UUID.fromString(uuidString)))
        );

        return questionService.createTestQuestion(dto, ownerUuidAndImage.getLeft(), ownerUuidAndImage.getRight(), imageUuidDataMap);

    }

    private Pair<UUID, FileData> getOwnerUuidAndImage(String imageUuid){
        return Pair.of(
                userService.getCurrentUserUuid(),
                fileDataService.getByUuid(UUID.fromString(imageUuid))
        );
    }
}

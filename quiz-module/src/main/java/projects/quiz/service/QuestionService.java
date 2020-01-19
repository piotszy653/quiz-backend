package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.answer.TestAnswerDto;
import projects.quiz.dto.question.QuestionUpdateDto;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.quiz.dto.question.openQuestion.OpenQuestionUpdateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionUpdateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionUpdateDto;
import projects.quiz.model.Answer;
import projects.quiz.model.question.OpenQuestion;
import projects.quiz.model.question.Question;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;
import projects.quiz.repository.question.OpenQuestionRepository;
import projects.quiz.repository.question.TestQuestionRepository;
import projects.quiz.repository.question.TrueFalseQuestionRepository;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class QuestionService {

    private final MessageSource messageSource;

    private final OpenQuestionRepository openQuestionRepository;

    private final TestQuestionRepository testQuestionRepository;

    private final TrueFalseQuestionRepository trueFalseQuestionRepository;

    private final AnswerService answerService;

    private OpenQuestion getOpenQuestionByUuid(UUID uuid) {
        return openQuestionRepository.findByUuid(uuid)
                .orElseThrow(() -> noSuchElementExceptionByUuid(uuid));
    }

    private TrueFalseQuestion getTrueFalseQuestionByUuid(UUID uuid) {
        return trueFalseQuestionRepository.findByUuid(uuid)
                .orElseThrow(() -> noSuchElementExceptionByUuid(uuid));
    }

    private TestQuestion getTestQuestionByUuid(UUID uuid) {
        return testQuestionRepository.findByUuid(uuid)
                .orElseThrow(() -> noSuchElementExceptionByUuid(uuid));
    }

    public Question getByUuid(UUID uuid) {

        Optional<OpenQuestion> openQuestion = openQuestionRepository.findByUuid(uuid);
        if (openQuestion.isPresent())
            return openQuestion.get();

        Optional<TrueFalseQuestion> trueFalseQuestion = trueFalseQuestionRepository.findByUuid(uuid);
        if (trueFalseQuestion.isPresent())
            return trueFalseQuestion.get();

        return testQuestionRepository.findByUuid(uuid)
                .orElseThrow(() -> noSuchElementExceptionByUuid(uuid));
    }

    public LinkedHashSet<Question> getAllByUuids(LinkedHashSet<UUID> uuids) {
        return uuids.stream().map(this::getByUuid).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public LinkedHashSet<Question> getByOwner(UUID ownerUuid) {

        LinkedHashSet<Question> questions = new LinkedHashSet<>();

        questions.addAll(openQuestionRepository.findAllByOwnerUuid(ownerUuid));
        questions.addAll(trueFalseQuestionRepository.findAllByOwnerUuid(ownerUuid));
        questions.addAll(testQuestionRepository.findAllByOwnerUuid(ownerUuid));

        return questions;
    }

    @Transactional
    public OpenQuestion createOpenQuestion(OpenQuestionCreateDto dto, UUID ownerUuid, FileData imageData) {
        return saveOpenQuestion(new OpenQuestion(dto, ownerUuid, imageData));
    }

    @Transactional
    public TrueFalseQuestion createTrueFalseQuestion(TrueFalseQuestionCreateDto dto, UUID ownerUuid, FileData imageData) {
        return saveTrueFalseQuestion(new TrueFalseQuestion(dto, ownerUuid, imageData));
    }

    @Transactional
    public TestQuestion createTestQuestion(TestQuestionCreateDto dto, UUID ownerUuid, FileData imageData, HashMap<UUID, FileData> answerImageUuidDataMap) {

        HashMap<UUID, Boolean> answersCorrectness = new HashMap<>();

        LinkedHashSet<Answer> answers = createAnswers(dto.getAnswers(), answerImageUuidDataMap, answersCorrectness);

        return saveTestQuestion(new TestQuestion(dto, ownerUuid, imageData, answers, answersCorrectness));
    }

    @Transactional
    public OpenQuestion saveOpenQuestion(@Valid OpenQuestion openQuestion) {
        return openQuestionRepository.save(openQuestion);
    }

    @Transactional
    public TrueFalseQuestion saveTrueFalseQuestion(@Valid TrueFalseQuestion trueFalseQuestion) {
        return trueFalseQuestionRepository.save(trueFalseQuestion);
    }

    @Transactional
    public TestQuestion saveTestQuestion(@Valid TestQuestion testQuestion) {
        return testQuestionRepository.save(testQuestion);
    }

    @Transactional
    public OpenQuestion updateOpenQuestion(OpenQuestionUpdateDto dto, UUID uuid, FileData imageData) {
        OpenQuestion question = getOpenQuestionByUuid(uuid);
        updateQuestion(question, dto, imageData);
        question.setAnswer(dto.getAnswer() != null ? dto.getAnswer() : question.getAnswer());
        return saveOpenQuestion(question);
    }

    @Transactional
    public TrueFalseQuestion updateTrueFalseQuestion(TrueFalseQuestionUpdateDto dto, UUID uuid, FileData imageData) {
        TrueFalseQuestion question = getTrueFalseQuestionByUuid(uuid);
        updateQuestion(question, dto, imageData);
        question.setAnswer(dto.getAnswer() != null ? dto.getAnswer() : question.getAnswer());
        return saveTrueFalseQuestion(question);
    }

    @Transactional
    public TestQuestion updateTestQuestion(TestQuestionUpdateDto dto, UUID uuid, FileData imageData, HashMap<UUID, FileData> answerImageUuidDataMap) {

        TestQuestion question = getTestQuestionByUuid(uuid);
        updateQuestion(question, dto, imageData);

        HashMap<UUID, Boolean> newAnswersCorrectness = new HashMap<>();
        LinkedHashSet<Answer> newAnswers = createAnswers(dto.getAnswers(), answerImageUuidDataMap, newAnswersCorrectness);
        question.getAnswers().addAll(newAnswers);
        question.getAnswersCorrectness().putAll(newAnswersCorrectness);

        removeAnswers(question, dto);
        updateAnswersCorrectness(question, dto);

        question.setMultipleChoice(dto.getIsMultipleChoice() != null ? dto.getIsMultipleChoice() : question.isMultipleChoice());

        return saveTestQuestion(question);
    }

    private void updateQuestion(Question question, QuestionUpdateDto dto, FileData imageData) {
        question.setQuestion(dto.getQuestion() != null ? dto.getQuestion() : question.getQuestion());
        question.setImageData(imageData != null ? imageData : question.getImageData());
        question.setTags(dto.getTags() != null ? dto.getTags() : question.getTags());
    }

    @Transactional
    public void removeImage(QuestionType questionType, UUID questionUuid) {

        switch (questionType) {
            case OPEN:
                OpenQuestion openQuestion = getOpenQuestionByUuid(questionUuid);
                openQuestion.removeImage();
                saveOpenQuestion(openQuestion);
                break;
            case TRUE_FALSE:
                TrueFalseQuestion trueFalseQuestion = getTrueFalseQuestionByUuid(questionUuid);
                trueFalseQuestion.removeImage();
                saveTrueFalseQuestion(trueFalseQuestion);
            case TEST:
                TestQuestion testQuestion = getTestQuestionByUuid(questionUuid);
                testQuestion.removeImage();
                saveTestQuestion(testQuestion);
                break;
            default:
                throwUnsupportedType(questionType.toString());
        }
    }

    @Transactional
    public void delete(UUID uuid, String questionType) {
        try {
            delete(uuid, QuestionType.valueOf(questionType));
        } catch (IllegalArgumentException e) {
            throwUnsupportedType(questionType);
        }
    }

    @Transactional
    public void delete(UUID uuid, QuestionType type) {

        switch (type) {
            case OPEN:
                openQuestionRepository.delete(getOpenQuestionByUuid(uuid));
                break;
            case TRUE_FALSE:
                trueFalseQuestionRepository.delete(getTrueFalseQuestionByUuid(uuid));
                break;
            case TEST:
                testQuestionRepository.delete(getTestQuestionByUuid(uuid));
                break;
            default:
                throwUnsupportedType(type.toString());
        }
    }

    private void throwUnsupportedType(String questionType) {
        throw unsupportedTypeException(questionType);
    }

    public IllegalArgumentException unsupportedTypeException(String questionType) {
        return new IllegalArgumentException(messageSource.getMessage("question_type.unsupported.type", new Object[]{questionType}, null));
    }

    private NoSuchElementException noSuchElementExceptionByUuid(UUID uuid) {
        return new NoSuchElementException(messageSource.getMessage("question.not_found.uuid", new Object[]{uuid}, null));
    }

    private HashMap<UUID, Boolean> getAnswersCorrectness(HashMap<String, Boolean> answers){

        HashMap<UUID, Boolean> answersCorrectness = new HashMap<>();
        answers.keySet().forEach(uuid -> answersCorrectness.put(UUID.fromString(uuid), answers.get(uuid)));
        return answersCorrectness;
    }

    private LinkedHashSet<Answer> createAnswers(LinkedHashSet<TestAnswerDto> testAnswerDtoSet, HashMap<UUID, FileData> imageUuidDataMap, HashMap<UUID, Boolean> answersCorrectness) {
        return testAnswerDtoSet.stream().map(testAnswerDto -> {
            Answer answer = answerService.create(
                    testAnswerDto.getAnswerDto(),
                    imageUuidDataMap.get(testAnswerDto.getAnswerDto().getImageUuid())
            );
            answersCorrectness.put(answer.getUuid(), testAnswerDto.getIsCorrect());
            return answer;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void removeAnswers(TestQuestion question, TestQuestionUpdateDto dto) {

        LinkedHashSet<Answer> answersToRemove = answerService.getAllByUuids(dto.getRemovedAnswersUuids());
        question.getAnswers().removeAll(answersToRemove);
        answerService.deleteAll(answersToRemove);
        answersToRemove.forEach(answer -> question.getAnswersCorrectness().remove(answer.getUuid()));
    }

    private void updateAnswersCorrectness(TestQuestion question, TestQuestionUpdateDto dto){
        dto.getUpdatedAnswersCorrectness().keySet().forEach(uuid ->
                question.getAnswersCorrectness().replace(
                        uuid,
                        dto.getUpdatedAnswersCorrectness().get(uuid)
                )
        );

    }
}

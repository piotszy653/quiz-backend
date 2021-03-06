package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.quiz.QuizCreateDto;
import projects.quiz.dto.quiz.QuizUpdateDto;
import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.Question;
import projects.quiz.repository.QuizRepository;
import projects.quiz.utils.enums.PrivacyPolicy;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.question.QuestionsTypesAssessmentsMatchValidator;
import projects.storage.model.FileData;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.PrivacyPolicy.FRIENDS;
import static projects.quiz.utils.enums.PrivacyPolicy.PUBLIC;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class QuizService {

    private final MessageSource messageSource;

    private final QuizRepository quizRepository;

    private final AssessmentService assessmentService;

    private final QuestionService questionService;

    public Quiz getByUuid(UUID uuid) {
        return quizRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("quiz.not_found.uuid", new Object[]{uuid}, null)));
    }

    public LinkedHashSet<Quiz> getAllByOwner(UUID ownerUuid) {
        return quizRepository.findAllByOwnerUuid(ownerUuid);
    }

    public LinkedHashSet<Quiz> getAllPublic(){
        return quizRepository.findAllByPrivacyPolicy(PUBLIC);
    }

    public Set<Quiz> getAllByPrivacyAndOwner(PrivacyPolicy policy, UUID uuid) {
        return quizRepository.findAllByPrivacyPolicyAndOwnerUuid(policy, uuid);
    }

    public LinkedHashSet<Quiz> getAllFriends(Set<UUID> friendsUuidSet){
        return quizRepository.findAllByOwnerUuidInAndPrivacyPolicy(friendsUuidSet, FRIENDS);
    }

    @Transactional
    public Quiz create(QuizCreateDto quizCreateDto, UUID ownerUuid, String ownerUsername, FileData imageData,
                       LinkedList<FileData> openQuestionsImages, LinkedList<FileData> trueFalseQuestionsImages,
                       LinkedList<FileData> testQuestionsImages, LinkedList<HashMap<UUID, FileData>> testAnswersImageUuidDataMap) {


        LinkedHashSet<Question> questions = createQuestions(
                quizCreateDto,
                ownerUuid,
                openQuestionsImages,
                trueFalseQuestionsImages,
                testQuestionsImages,
                testAnswersImageUuidDataMap
        );

        questions.addAll(questionService.getAllByUuids(quizCreateDto.getAddedQuestionsUuids()));

        HashMap<QuestionType, Assessment> assessments = getAssessments(quizCreateDto.getAssessmentsUuids());

        return save(new Quiz(
                quizCreateDto.getName(),
                ownerUuid,
                ownerUsername,
                imageData,
                quizCreateDto.getPrivacyPolicy(),
                questions,
                assessments,
                new LinkedHashSet<>(),
                quizCreateDto.getTags().stream().map(tag -> tag.trim().toLowerCase()).collect(Collectors.toCollection(LinkedHashSet::new))
        ));

    }

    @Transactional
    public Quiz save(@Valid Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz update(QuizUpdateDto dto, UUID uuid, FileData imageData) {

        Quiz quiz = getByUuid(uuid);

        quiz.setName(dto.getName() != null ? dto.getName() : quiz.getName());
        quiz.setImageData(imageData != null ? imageData : quiz.getImageData());
        quiz.setPrivacyPolicy(dto.getPrivacyPolicy() != null ? dto.getPrivacyPolicy() : quiz.getPrivacyPolicy());
        quiz.setTags(dto.getTags() != null ? dto.getTags().stream().map(tag -> tag.trim().toLowerCase()).collect(Collectors.toCollection(LinkedHashSet::new)) : quiz.getTags());
        quiz.getQuestions().removeAll(questionService.getAllByUuids(dto.getRemovedQuestionsUuids()));
        quiz.getQuestions().addAll(questionService.getAllByUuids(dto.getAddedQuestionsUuids()));

        HashMap<QuestionType, UUID> assessmentsUuids = dto.getReplacedAssessmentsUuids();

        assessmentsUuids.keySet()
                .forEach(questionType -> {
                    UUID assessmentUuid = assessmentsUuids.get(questionType);
                    if (assessmentUuid == null)
                        quiz.getAssessments().remove(questionType);
                    else
                        quiz.getAssessments()
                                .put(
                                        questionType,
                                        assessmentService.getByUuid(assessmentUuid)
                                );
                });

        QuestionsTypesAssessmentsMatchValidator.isValid(quiz);
        return save(quiz);
    }

    @Transactional
    public void removeImage(UUID uuid) {

        Quiz quiz = getByUuid(uuid);
        quiz.removeImage(); // todo remove Image from db and storage
        save(quiz);
    }

    @Transactional
    public void delete(UUID uuid) {
        quizRepository.deleteByUuid(uuid);
    }

    @Transactional
    public LinkedHashSet<Question> createQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid,
                                                   LinkedList<FileData> openQuestionsImages, LinkedList<FileData> trueFalseQuestionsImages,
                                                   LinkedList<FileData> testQuestionsImages, LinkedList<HashMap<UUID, FileData>> testAnswersImages) {

        LinkedHashSet<Question> questions = createAllOpenQuestions(quizCreateDto, ownerUuid, openQuestionsImages);
        questions.addAll(createAllTrueFalseQuestions(quizCreateDto, ownerUuid, trueFalseQuestionsImages));
        questions.addAll(createAllTestQuestions(quizCreateDto, ownerUuid, testQuestionsImages, testAnswersImages));
        return questions;
    }

    @Transactional
    public LinkedHashSet<Question> createAllOpenQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> openQuestionImages) {
        return quizCreateDto.getCreatedOpenQuestions().stream()
                .map(openQuestionCreateDto -> questionService.createOpenQuestion(
                        openQuestionCreateDto,
                        ownerUuid,
                        openQuestionImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public LinkedHashSet<Question> createAllTrueFalseQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> trueFalseQuestionImages) {
        return quizCreateDto.getCreatedTrueFalseQuestions().stream()
                .map(trueFalseQuestionCreateDto -> questionService.createTrueFalseQuestion(
                        trueFalseQuestionCreateDto,
                        ownerUuid,
                        trueFalseQuestionImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public LinkedHashSet<Question> createAllTestQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> testQuestionImages, LinkedList<HashMap<UUID, FileData>> testAnswersImages) {
        return quizCreateDto.getCreatedTestQuestions().stream()
                .map(testQuestionCreateDto -> questionService.createTestQuestion(
                        testQuestionCreateDto,
                        ownerUuid,
                        testQuestionImages.removeFirst(),
                        testAnswersImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private HashMap<QuestionType, Assessment> getAssessments(Map<QuestionType, UUID> assessmentsUuids) {

        HashMap<QuestionType, Assessment> assessments = new HashMap<>();

        assessmentsUuids.keySet()
                .forEach(questionType ->
                        assessments.put(
                                questionType,
                                assessmentService.getByUuid(assessmentsUuids.get(questionType))
                        )
                );

        return assessments;
    }

    @Transactional
    public Quiz addEditors(UUID uuid, Set<UUID> editors) {
        Quiz quiz = getByUuid(uuid);
        quiz.getEditors().addAll(editors);
        return save(quiz);
    }

    @Transactional
    public Quiz removeEditors(UUID uuid, Set<UUID> editors) {
        Quiz quiz = getByUuid(uuid);
        quiz.getEditors().removeAll(editors);
        return save(quiz);
    }
}

package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.QuizCreateDto;
import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.Question;
import projects.quiz.repository.QuizRepository;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public Quiz create(QuizCreateDto quizCreateDto, UUID ownerUuid, FileData imageData,
                       LinkedList<FileData> openQuestionsImages, LinkedList<FileData> trueFalseQuestionsImages,
                       LinkedList<FileData> testQuestionsImages, LinkedList<HashMap<String, FileData>> testAnswersImageUuidDataMap) {


        LinkedHashSet<Question> questions = createQuestions(
                quizCreateDto,
                ownerUuid,
                openQuestionsImages,
                trueFalseQuestionsImages,
                testQuestionsImages,
                testAnswersImageUuidDataMap
        );

        questions.addAll(questionService.getAllByUuids(quizCreateDto.getAddedQuestionsUuuids()));

        HashMap<QuestionType, Assessment> assessments = getAssessments(quizCreateDto.getAssessmentsUuids());

        return save(new Quiz(
                ownerUuid,
                imageData,
                questions,
                assessments
        ));

    }

    @Transactional
    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Transactional
    public LinkedHashSet<Question> createQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid,
                                                   LinkedList<FileData> openQuestionsImages, LinkedList<FileData> trueFalseQuestionsImages,
                                                   LinkedList<FileData> testQuestionsImages, LinkedList<HashMap<String, FileData>> testAnswersImages) {

        LinkedHashSet<Question> questions = createAllOpenQuestions(quizCreateDto, ownerUuid, openQuestionsImages);
        questions.addAll(createAllTrueFalseQuestions(quizCreateDto, ownerUuid, trueFalseQuestionsImages));
        questions.addAll(createAllTestQuestions(quizCreateDto, ownerUuid, testQuestionsImages, testAnswersImages));
        return questions;
    }

    @Transactional
    public LinkedHashSet<Question> createAllOpenQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> openQuestionImages){
        return quizCreateDto.getCreatedOpenQuestions().stream()
                .map(openQuestionCreateDto -> questionService.createOpenQuestion(
                        openQuestionCreateDto,
                        ownerUuid,
                        openQuestionImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public LinkedHashSet<Question> createAllTrueFalseQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> trueFalseQuestionImages){
        return quizCreateDto.getCreatedTrueFalseQuestions().stream()
                .map(trueFalseQuestionCreateDto -> questionService.createTrueFalseQuestion(
                        trueFalseQuestionCreateDto,
                        ownerUuid,
                        trueFalseQuestionImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public LinkedHashSet<Question> createAllTestQuestions(QuizCreateDto quizCreateDto, UUID ownerUuid, LinkedList<FileData> testQuestionImages, LinkedList<HashMap<String, FileData>> testAnswersImages){
        return quizCreateDto.getCreatedTestQuestions().stream()
                .map(testQuestionCreateDto -> questionService.createTestQuestion(
                        testQuestionCreateDto,
                        ownerUuid,
                        testQuestionImages.removeFirst(),
                        testAnswersImages.removeFirst()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private HashMap<QuestionType, Assessment> getAssessments(Map<QuestionType, String> assessmentsUuids){

        HashMap<QuestionType, Assessment> assessments = new HashMap<>();

        assessmentsUuids.keySet()
                .forEach(questionType ->
                        assessments.put(
                                questionType,
                                assessmentService.getByUuid(UUID.fromString(assessmentsUuids.get(questionType)))
                        )
                );

        return assessments;

    }
}

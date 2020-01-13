package projects.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.assessment.AssessmentsExist;
import projects.quiz.utils.validator.question.QuestionsExist;

import java.util.LinkedHashSet;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDto {

    private String imageUuid;

    private LinkedHashSet<OpenQuestionCreateDto> createdOpenQuestions = new LinkedHashSet<>();
    private LinkedHashSet<TrueFalseQuestionCreateDto> createdTrueFalseQuestions = new LinkedHashSet<>();
    private LinkedHashSet<TestQuestionCreateDto> createdTestQuestions = new LinkedHashSet<>();

    @QuestionsExist
    private LinkedHashSet<String> addedQuestionsUuuids = new LinkedHashSet<>();

    @AssessmentsExist //todo check question type validation
    private Map<QuestionType, String> assessmentsUuids;
}

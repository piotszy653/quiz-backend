package projects.quiz.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.quiz.dto.question.testQuestion.TestQuestionCreateDto;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.assessment.AssessmentsExist;
import projects.quiz.utils.validator.question.QuestionsExist;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDto {

    private UUID imageUuid;

    private LinkedHashSet<@Valid OpenQuestionCreateDto> createdOpenQuestions = new LinkedHashSet<>();
    private LinkedHashSet<@Valid TrueFalseQuestionCreateDto> createdTrueFalseQuestions = new LinkedHashSet<>();
    private LinkedHashSet<@Valid TestQuestionCreateDto> createdTestQuestions = new LinkedHashSet<>();

    @QuestionsExist
    private LinkedHashSet<UUID> addedQuestionsUuids = new LinkedHashSet<>();

    @AssessmentsExist
    private HashMap<QuestionType, UUID> assessmentsUuids = new HashMap<>();

}

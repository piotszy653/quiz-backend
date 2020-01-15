package projects.quiz.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.assessment.AssessmentsExist;
import projects.quiz.utils.validator.question.QuestionsExist;

import java.util.LinkedHashSet;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizUpdateDto {

    private String imageUuid;

    @QuestionsExist
    private LinkedHashSet<String> addedQuestionsUuuids = new LinkedHashSet<>();

    private LinkedHashSet<String> removedQuestionsUuids = new LinkedHashSet<>();

    @AssessmentsExist //todo check question type validation
    private Map<QuestionType, String> replacedAssessmentsUuids;
}

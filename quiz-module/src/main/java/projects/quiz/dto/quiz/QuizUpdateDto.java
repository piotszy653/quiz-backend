package projects.quiz.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.quiz.utils.enums.PrivacyPolicy;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.assessment.AssessmentsExist;
import projects.quiz.utils.validator.question.QuestionsExist;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizUpdateDto {

    private UUID imageUuid;

    private PrivacyPolicy privacyPolicy;

    @QuestionsExist
    private LinkedHashSet<UUID> addedQuestionsUuids = new LinkedHashSet<>();

    private LinkedHashSet<UUID> removedQuestionsUuids = new LinkedHashSet<>();

    @AssessmentsExist
    private HashMap<QuestionType, UUID> replacedAssessmentsUuids = new HashMap<>();
}

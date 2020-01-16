package projects.quiz.utils;

import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.QuestionType.TEST;
import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

public class ResultHelper {

    private static void addPoints(BigDecimal points, Float questionPoints, Assessment assessment) {
        if (assessment.getMaxPoints() != null)
            questionPoints = questionPoints > assessment.getMaxPoints() ? assessment.getMaxPoints() : questionPoints;
        if (assessment.getMinPoints() != null)
            questionPoints = questionPoints < assessment.getMinPoints() ? assessment.getMinPoints() : questionPoints;
        points = points.add(new BigDecimal(questionPoints));
    }

    public static Float calcTrueFalsePoints(Quiz quiz, Set<TrueFalseQuestion> trueFalseQuestions, Map<UUID, Boolean> trueFalseAnswers) {
        BigDecimal points = new BigDecimal(0.0);
        Assessment tfAssessment = quiz.getAssessments().get(TRUE_FALSE);
        trueFalseQuestions.forEach(question -> {
            Float tfPoints = 0.0f;
            tfPoints += Boolean.valueOf(question.getAnswer()).equals(trueFalseAnswers.get(question.getUuid())) ? tfAssessment.getCorrectRate() : tfAssessment.getIncorrectRate();
            addPoints(points, tfPoints, tfAssessment);
        });
        return points.floatValue();
    }

    public static Float calcTestPoints(Quiz quiz, Set<TestQuestion> testQuestions, Map<UUID, Set<UUID>> testAnswers) {
        BigDecimal points = new BigDecimal(0.0);
        Assessment testAssessment = quiz.getAssessments().get(TEST);
        testQuestions.forEach(question -> {
            Set<UUID> pickedAnswers = testAnswers.get(question.getUuid());
            if (pickedAnswers != null) {
                Set<Float> answersPoints = pickedAnswers.stream().map(answerUuid ->
                        question.getAnswersCorrectness().get(answerUuid) ? testAssessment.getCorrectRate() : testAssessment.getIncorrectRate()
                ).collect(Collectors.toSet());
                Float testPoints = 0.0f;
                for (Float answerPoints : answersPoints) {
                    testPoints += answerPoints;
                }
                addPoints(points, testPoints, testAssessment);
            }
        });
        return points.floatValue();
    }
}

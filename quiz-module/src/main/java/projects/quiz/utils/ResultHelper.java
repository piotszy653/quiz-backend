package projects.quiz.utils;

import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.QuestionType.TEST;
import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

public class ResultHelper {

    private static void addPoints(Float points, Float questionPoints, Assessment assessment){
        questionPoints = questionPoints > assessment.getMaxPoints() ? assessment.getMaxPoints() : questionPoints;
        questionPoints = questionPoints < assessment.getMinPoints() ? assessment.getMinPoints() : questionPoints;
        points += questionPoints;
    }

    public static Float calcTrueFalsePoints(Quiz quiz, Set<TrueFalseQuestion> trueFalseQuestions, Map<UUID, Boolean> trueFalseAnswers){
        Float points = 0.0f;
        Assessment tfAssessment = quiz.getAssessments().get(TRUE_FALSE);
        trueFalseQuestions.forEach(question -> {
            Float tfPoints = 0.0f;
            tfPoints += trueFalseAnswers.get(question.getUuid()).equals(question.getAnswer()) ? tfAssessment.getCorrectRate() : tfAssessment.getIncorrectRate();
            addPoints(points, tfPoints, tfAssessment);
        });
        return points;
    }

    public static Float calcTestPoints(Quiz quiz, Set<TestQuestion> testQuestions, Map<UUID, Set<UUID>> testAnswers) {
        Float points = 0.0f;
        Assessment testAssessment = quiz.getAssessments().get(TEST);
        testQuestions.forEach(question -> {
            Set<UUID> pickedAnswers = testAnswers.get(question.getUuid());
            Set<Float> answersPoints = pickedAnswers.stream().map(answerUuid ->
                    question.getAnswersCorrectness().get(answerUuid) ? testAssessment.getCorrectRate() : testAssessment.getIncorrectRate()
            ).collect(Collectors.toSet());
            Float testPoints = 0.0f;
            for(Float answerPoints: answersPoints){
                testPoints += answerPoints;
            }
            addPoints(points, testPoints, testAssessment);
        });
        return points;
    }
}

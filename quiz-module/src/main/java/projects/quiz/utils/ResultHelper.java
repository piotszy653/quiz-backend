package projects.quiz.utils;

import projects.quiz.model.Answer;
import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;

import java.math.BigDecimal;
import java.util.*;

import static projects.quiz.utils.enums.QuestionType.TEST;
import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

public class ResultHelper {

    private static BigDecimal cutPoints(Float questionPoints, Assessment assessment) {
        if (assessment.getMaxPoints() != null)
            questionPoints = questionPoints > assessment.getMaxPoints() ? assessment.getMaxPoints() : questionPoints;
        if (assessment.getMinPoints() != null)
            questionPoints = questionPoints < assessment.getMinPoints() ? assessment.getMinPoints() : questionPoints;
        return new BigDecimal(questionPoints);
    }

    public static Float calcTrueFalsePoints(Quiz quiz, Set<TrueFalseQuestion> trueFalseQuestions, Map<UUID, Boolean> trueFalseAnswers) {
        BigDecimal points = new BigDecimal(0.0);
        Assessment tfAssessment = quiz.getAssessments().get(TRUE_FALSE);
        for (UUID answeredQuestion : trueFalseAnswers.keySet()) {
            Float tfPoints = 0.0f;
            Optional<TrueFalseQuestion> question = trueFalseQuestions.stream().filter(trueFalseQuestion -> trueFalseQuestion.getUuid().equals(answeredQuestion)).findFirst();
            if (question.isPresent()) {
                Boolean answer = trueFalseAnswers.get(question.get().getUuid());
                if (answer != null) {
                    tfPoints += Boolean.valueOf(question.get().getAnswer()).equals(trueFalseAnswers.get(question.get().getUuid())) ? tfAssessment.getCorrectRate() : tfAssessment.getIncorrectRate();
                    points = points.add(cutPoints(tfPoints, tfAssessment));
                }
            }
        }
        return points.floatValue();
    }

    public static Float calcTestPoints(Quiz quiz, Set<TestQuestion> testQuestions, Map<UUID, Set<UUID>> testAnswers) {
        BigDecimal points = new BigDecimal(0.0);
        Assessment testAssessment = quiz.getAssessments().get(TEST);
        for (UUID answeredQuestion : testAnswers.keySet()) {
            Set<UUID> pickedAnswers = testAnswers.get(answeredQuestion);
            Optional<TestQuestion> question = testQuestions.stream().filter(testQuestion -> testQuestion.getUuid().equals(answeredQuestion)).findFirst();
            if (question.isPresent() && pickedAnswers != null) {
                Float testPoints = 0.0f;
                for (UUID answerUuid : pickedAnswers)
                    testPoints += question.get().getAnswers()
                            .stream()
                            .filter(answer -> answer.getUuid().equals(answerUuid))
                            .findFirst()
                            .get().isCorrect() ? testAssessment.getCorrectRate() : testAssessment.getIncorrectRate();
                points = points.add(cutPoints(testPoints, testAssessment));
            }
        }
        return points.floatValue();
    }
}

package projects.quiz.utils;

import projects.quiz.model.Answer;
import projects.quiz.model.Assessment;
import projects.quiz.model.Quiz;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static Float calcMaxPoints(Quiz quiz) {
        return calcMaxTrueFalseQuestionsPoints(quiz) + calcMaxTestQuestionsPoints(quiz);
    }

    public static Float calcMaxTestQuestionsPoints(Quiz quiz) {

        Set<TestQuestion> questions = quiz.getQuestions().stream()
                .filter(question -> question.getType().equals(TEST))
                .map(question -> (TestQuestion) question)
                .collect(Collectors.toSet());

        Float points = 0.0f;
        Assessment testAssessment = quiz.getAssessments().get(TEST);

        for(TestQuestion question: questions){
            points += calcMaxTestQuestionPoints(question, testAssessment);
        }
        return points;
    }

    public static Float calcMaxTrueFalseQuestionsPoints(Quiz quiz) {
        Set<TrueFalseQuestion> questions = quiz.getQuestions().stream()
                .filter(question -> question.getType().equals(TRUE_FALSE))
                .map(question -> (TrueFalseQuestion) question)
                .collect(Collectors.toSet());

        Assessment trueFalseAssessment = quiz.getAssessments().get(TRUE_FALSE);

        return trueFalseAssessment.getCorrectRate() * questions.size();
    }

    private static Float calcMaxTestQuestionPoints(TestQuestion question, Assessment testAssessment) {

        Set<Answer> correctAnswers = question.getAnswers()
                .stream()
                .filter(Answer::isCorrect)
                .collect(Collectors.toSet());
        Float points = testAssessment.getCorrectRate() * correctAnswers.size();
        Float maxPoints = testAssessment.getMaxPoints();

        return maxPoints != null && maxPoints < points ? maxPoints : points;
    }

}

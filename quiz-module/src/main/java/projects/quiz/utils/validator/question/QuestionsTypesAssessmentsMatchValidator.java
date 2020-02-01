package projects.quiz.utils.validator.question;

import projects.quiz.model.Quiz;
import projects.quiz.utils.enums.QuestionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class QuestionsTypesAssessmentsMatchValidator implements ConstraintValidator<QuestionTypesAssessmentsMatch, Quiz> {

    @Override
    public boolean isValid(Quiz quiz, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(quiz);
    }

    public static boolean isValid(Quiz quiz){
        Set<QuestionType> types = quiz.getQuestionTypes();
        types.remove(QuestionType.OPEN);

        if (!quiz.getAssessments().keySet().containsAll(types))
            throw new IllegalArgumentException("assessments.not_match_question_types");
        return true;
    }
}

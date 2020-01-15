package projects.quiz.utils.validator.question;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuestionsTypesAssessmentsMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuestionTypesAssessmentsMatch {

    String message() default "{assessments.not_match_question_types}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

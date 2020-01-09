package projects.quiz.utils.validator.question;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = TestQuestionTypeValidator.class)
@Target({PARAMETER, FIELD, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestQuestionType {

    String message() default "{question_type.test_question_required}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
package projects.quiz.utils.validator.question;

import projects.quiz.utils.validator.quiz.QuizExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuestionExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuestionExists {

    String message() default "{question.not_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

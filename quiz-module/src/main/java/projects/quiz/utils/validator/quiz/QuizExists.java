package projects.quiz.utils.validator.quiz;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuizExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuizExists {

    String message() default "{quiz.not_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

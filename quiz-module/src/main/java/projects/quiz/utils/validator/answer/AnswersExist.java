package projects.quiz.utils.validator.answer;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AnswersExistValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnswersExist {

    String message() default "{answers.not_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

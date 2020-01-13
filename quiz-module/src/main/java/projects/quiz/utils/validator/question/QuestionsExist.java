package projects.quiz.utils.validator.question;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuestionsExistValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuestionsExist {

    String message() default "{questions.not_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

package projects.core.utils.validator.quiz;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuizEditorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuizEditor {

    String message() default "{forbidden}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

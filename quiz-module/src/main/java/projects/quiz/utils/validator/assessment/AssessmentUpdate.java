package projects.quiz.utils.validator.assessment;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AssessmentUpdateValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssessmentUpdate {

    String message() default "{assessment.wrong_values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

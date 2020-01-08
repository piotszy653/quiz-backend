package projects.quiz.utils.validator.assessment;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AssessmentValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssessmentCreate {

    String message() default "{assessment.wrong_values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

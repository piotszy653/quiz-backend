package projects.quiz.utils.validator.assessment;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AssessmentsExistValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssessmentsExist {

    String message() default "{assessments.not_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

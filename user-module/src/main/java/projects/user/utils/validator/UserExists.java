package projects.user.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExists {

    String message() default "{user.not_found}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

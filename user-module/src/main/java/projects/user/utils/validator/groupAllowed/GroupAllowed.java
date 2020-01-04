package projects.user.utils.validator.groupAllowed;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = GroupAllowedValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupAllowed {

    String message() default "FORBIDDEN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

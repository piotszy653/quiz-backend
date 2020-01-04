package projects.user.utils.validator.currentUser;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrentUserValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

    String message() default "FORBIDDEN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

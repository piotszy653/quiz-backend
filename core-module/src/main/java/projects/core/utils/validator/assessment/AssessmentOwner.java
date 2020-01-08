package projects.core.utils.validator.assessment;


import projects.core.config.enums.roles.RolesEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AssessmentOwnerValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssessmentOwner {
    RolesEnum[] roles() default {};

    String message() default "FORBIDDEN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


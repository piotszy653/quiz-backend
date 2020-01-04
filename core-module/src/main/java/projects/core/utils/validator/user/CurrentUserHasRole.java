package projects.core.utils.validator.user;

import projects.core.config.enums.roles.RolesEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrentUserHasRoleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserHasRole {
    RolesEnum[] roles() default {};

    String message() default "FORBIDDEN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

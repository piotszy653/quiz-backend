package projects.core.utils.validator.quiz;


import projects.core.config.enums.roles.RolesEnum;
import projects.core.utils.validator.question.QuestionOwnerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = QuizOwnerValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuizOwner {
    RolesEnum[] roles() default {};

    String message() default "{forbidden}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


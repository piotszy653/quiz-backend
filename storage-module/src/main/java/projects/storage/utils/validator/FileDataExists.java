package projects.storage.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileDataExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileDataExists {
    String message() default "{file_data.not_found}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package projects.storage.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileExtension {

    String[] extensions() default {};

    String message() default "{file.wrong_extension}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

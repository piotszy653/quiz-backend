package projects.user.utils.validator.invitation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueInvitationValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueInvitation {

    String message() default "{invitation.already_exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

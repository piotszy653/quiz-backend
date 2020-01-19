package projects.user.utils.validator.invitation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SelfInvitationValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelfInvitation {

    String message() default "{invitation.self_invitation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

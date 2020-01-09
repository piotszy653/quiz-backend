package projects.quiz.utils.validator.question;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import projects.quiz.dto.assessment.AssessmentUpdateDto;
import projects.quiz.model.Assessment;
import projects.quiz.service.AssessmentService;
import projects.quiz.utils.enums.QuestionType;
import projects.quiz.utils.validator.assessment.AssessmentCreate;
import projects.quiz.utils.validator.assessment.AssessmentValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.UUID;

@AllArgsConstructor
@SupportedValidationTarget(ValidationTarget.PARAMETERS)

public class TestQuestionTypeValidator implements ConstraintValidator<TestQuestionType, QuestionType> {


    @Override
    public boolean isValid(QuestionType type, ConstraintValidatorContext constraintValidatorContext) {
        return type.isTestType();
    }
}

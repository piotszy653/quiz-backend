package projects.quiz.dto.question.trueFalseQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.QuestionCreateDto;
import projects.quiz.dto.question.QuestionUpdateDto;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TrueFalseQuestionUpdateDto extends QuestionUpdateDto {

    private Boolean answer;

    public TrueFalseQuestionUpdateDto(String question, boolean answer){
        super(question);
        this.answer = answer;
    }
}

package projects.quiz.dto.answer;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TestAnswerDto {

    @NotNull(message = "{answer_dto.not_null}")
    private AnswerDto answerDto;

    @NotNull(message = "{is_correct.not_null}")
    private Boolean isCorrect;

}

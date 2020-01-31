package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.dto.question.QuestionUpdateDto;

import java.util.LinkedHashSet;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionUpdateDto extends QuestionUpdateDto {

    private LinkedHashSet<AnswerDto> answers = new LinkedHashSet<>();

    private LinkedHashSet<UUID> removedAnswersUuids = new LinkedHashSet<>();

    private Boolean isMultipleChoice;

}

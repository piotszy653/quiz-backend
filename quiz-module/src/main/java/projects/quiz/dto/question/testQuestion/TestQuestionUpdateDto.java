package projects.quiz.dto.question.testQuestion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.answer.TestAnswerDto;
import projects.quiz.dto.question.QuestionUpdateDto;
import projects.quiz.utils.validator.answer.AnswersExist;

import java.util.HashMap;
import java.util.LinkedHashSet;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestQuestionUpdateDto extends QuestionUpdateDto {

    private LinkedHashSet<TestAnswerDto> answers = new LinkedHashSet<>();

    private LinkedHashSet<String> removedAnswersUuids = new LinkedHashSet<>();

    private HashMap<String, Boolean> updatedAnswersCorrectness = new HashMap<>();

    private Boolean isMultipleChoice;

}

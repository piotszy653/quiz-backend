package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.storage.model.FileData;

import javax.persistence.Entity;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class TrueFalseQuestion extends Question {

    private boolean answer;

    public TrueFalseQuestion(TrueFalseQuestionCreateDto dto, UUID ownerUuid, FileData imageData) {
        super(dto.getQuestion(), ownerUuid, imageData, TRUE_FALSE, dto.getTags().stream().map(tag -> tag.trim().toLowerCase()).collect(Collectors.toCollection(LinkedHashSet::new)));
        this.answer = dto.getAnswer();
    }

    public boolean getAnswer() {
        return this.answer;
    }

}

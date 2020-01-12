package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.quiz.dto.question.trueFalseQuestion.TrueFalseQuestionCreateDto;
import projects.storage.model.FileData;

import javax.persistence.Entity;
import java.util.UUID;

import static projects.quiz.utils.enums.QuestionType.TRUE_FALSE;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class TrueFalseQuestion extends Question {

    private boolean answer;

    public TrueFalseQuestion(String question, UUID ownerUuid, FileData imageData, boolean answer){
      super(question, ownerUuid, imageData, TRUE_FALSE);
      this.answer = answer;
    }

    public TrueFalseQuestion(TrueFalseQuestionCreateDto dto, UUID ownerUuid, FileData imageData){
        super(dto.getQuestion(), ownerUuid, imageData, TRUE_FALSE);
        this.answer = dto.getAnswer();
    }

    public boolean getAnswer(){
        return this.answer;
    }

}

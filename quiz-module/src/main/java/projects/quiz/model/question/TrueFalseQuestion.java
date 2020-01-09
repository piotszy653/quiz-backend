package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import projects.quiz.model.Assessment;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.Entity;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TrueFalseQuestion extends Question {

    private boolean answer;

    public TrueFalseQuestion(String question, UUID ownerUuid, FileData imageData, Assessment assessment, boolean answer){
      super(question, ownerUuid, imageData, QuestionType.TRUE_FALSE, assessment);
      this.answer = answer;
    }

}

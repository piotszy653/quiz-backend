package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import projects.quiz.model.Assessment;
import projects.quiz.utils.enums.QuestionType;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OpenQuestion extends Question {

    @Column(nullable = false)
    @NotBlank(message = "{answer.not_blank}")
    @Size(max = 255, message = "name.max:255")
    private String answer;

    public OpenQuestion(String question, UUID ownerUuid, FileData imageData, Assessment assessment, String answer){
        super(question, ownerUuid, imageData, QuestionType.OPEN, assessment);
        this.answer = answer;
    }

}

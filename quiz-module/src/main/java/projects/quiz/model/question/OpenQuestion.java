package projects.quiz.model.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import projects.quiz.dto.question.openQuestion.OpenQuestionCreateDto;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

import static projects.quiz.utils.enums.QuestionType.OPEN;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OpenQuestion extends Question {

    @Column(nullable = false)
    @NotBlank(message = "{answer.not_blank}")
    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

    public OpenQuestion(String question, UUID ownerUuid, FileData imageData, String answer){
        super(question, ownerUuid, imageData, OPEN);
        this.answer = answer;
    }

    public OpenQuestion(OpenQuestionCreateDto dto, UUID ownerUuid, FileData imageData){
        super(dto.getQuestion(), ownerUuid, imageData, OPEN);
        this.answer = dto.getAnswer();
    }

}

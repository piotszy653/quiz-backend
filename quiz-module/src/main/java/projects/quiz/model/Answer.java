package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import projects.quiz.dto.answer.AnswerDto;
import projects.storage.model.FileData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer extends AbstractBaseEntity<Long>{

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "{answer.not_blank}")
    @Size(max = 1000, message = "answer.max:1000")
    private String answer;

    @Column(nullable = false)
    private boolean correct;

    @ManyToOne(fetch = EAGER)
    FileData imageData;

    public Answer(AnswerDto dto, FileData imageData){
        this.answer = dto.getAnswer();
        this.correct = dto.isCorrect();
        this.imageData = imageData;
    }
}

package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import projects.quiz.dto.result.ResultCreateDto;
import projects.quiz.model.question.Question;
import projects.quiz.model.question.TestQuestion;
import projects.quiz.model.question.TrueFalseQuestion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static projects.quiz.utils.ResultHelper.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Result extends AbstractBaseEntity<Long> {

    @NotNull(message = "{user_uuid.not_null}")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID userUuid;

    @NotNull(message = "{quiz.not_null}")
    @ManyToOne(fetch = FetchType.EAGER)
    private Quiz quiz;

    private HashMap<UUID, Boolean> trueFalseAnswers = new HashMap<>();

    private HashMap<UUID, Set<UUID>> testAnswers = new HashMap<>();

    public Result(ResultCreateDto dto, UUID userUuid, Quiz quiz) {
        this.userUuid = userUuid;
        this.quiz = quiz;

        HashMap<UUID, Boolean> tfAnswers = dto.getTrueFalseAnswers();
        tfAnswers.keySet().forEach(questionUuid -> trueFalseAnswers.put(questionUuid, tfAnswers.get(questionUuid)));

        HashMap<UUID, Set<UUID>> testAnswers = dto.getTestAnswers();
        testAnswers.keySet().forEach(questionUuid ->
                this.testAnswers.put(
                        questionUuid,
                        testAnswers.get(questionUuid)
                )
        );
    }

    public float getMaxPoints(){
        return calcMaxPoints(quiz);
    }

    public float getPoints() {

        float points = 0.0f;

        Set<TrueFalseQuestion> trueFalseQuestions = quiz.getQuestions().stream().map(Question::getIfTrueFalseQuestion).collect(Collectors.toSet());
        Set<TestQuestion> testQuestions = quiz.getQuestions().stream().map(Question::getIfTestQuestion).collect(Collectors.toSet());
        trueFalseQuestions.remove(null);
        testQuestions.remove(null);

        points += calcTrueFalsePoints(quiz, trueFalseQuestions, trueFalseAnswers) + calcTestPoints(quiz, testQuestions, testAnswers);
        return points;
    }

}


package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import projects.quiz.dto.assessment.AssessmentCreateDto;
import projects.quiz.dto.assessment.AssessmentDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assessment extends AbstractBaseEntity<Long> implements AssessmentDto {

    @Size(max = 255, message = "name.max:255")
    private String name = "";

    @Column(nullable = false)
    @NotNull(message = "{uuid.not_null}")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID ownerUuid;

    @Column(nullable = false)
    @NotNull(message = "{correct_rate.not_null}")
    @Min(value = 0 ,message = "correct_rate.min:0")
    private float correctRate;

    @Column(nullable = false)
    @NotNull(message = "{incorrect_rate.not_null}")
    private float incorrectRate;

    private Float minPoints;

    @Min(value = 0 ,message = "max_points.min:0")
    private Float maxPoints;

    public Assessment(AssessmentCreateDto dto, UUID ownerUuid){

        this.ownerUuid = ownerUuid;
        this.name = dto.getName();
        this.correctRate = dto.getCorrectRate();
        this.incorrectRate = dto.getIncorrectRate();
        this.minPoints = dto.getMinPoints();
        this.maxPoints = dto.getMaxPoints();

    }

    @Override
    public Float getCorrectRate() {
        return correctRate;
    }

    @Override
    public Float getIncorrectRate() {
        return incorrectRate;
    }

    @Override
    public Float getMinPoints() {
        return minPoints;
    }

    @Override
    public Float getMaxPoints() {
        return maxPoints;
    }
}

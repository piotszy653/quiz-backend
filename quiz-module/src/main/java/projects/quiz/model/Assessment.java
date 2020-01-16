package projects.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.math.BigDecimal;
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
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID ownerUuid;

    @Column(nullable = false)
    @NotNull(message = "{correct_rate.not_null}")
    @Min(value = 0, message = "correct_rate.min:0")
    private BigDecimal correctRate;

    @Column(nullable = false)
    @NotNull(message = "{incorrect_rate.not_null}")
    private BigDecimal incorrectRate;

    private BigDecimal minPoints;

    @Min(value = 0, message = "max_points.min:0")
    private BigDecimal maxPoints;

    public Assessment(AssessmentCreateDto dto, UUID ownerUuid) {

        this.ownerUuid = ownerUuid;
        this.name = dto.getName();
        this.correctRate = new BigDecimal(dto.getCorrectRate());
        this.incorrectRate = new BigDecimal(dto.getIncorrectRate());
        this.minPoints = new BigDecimal(dto.getMinPoints());
        this.maxPoints = new BigDecimal(dto.getMaxPoints());

    }

    @Override
    public Float getCorrectRate() {
        return correctRate.floatValue();
    }

    @Override
    public Float getIncorrectRate() {
        return incorrectRate.floatValue();
    }

    @Override
    public Float getMinPoints() {
        return minPoints != null ? minPoints.floatValue() : null;
    }

    @Override
    public Float getMaxPoints() {
        return maxPoints != null ? maxPoints.floatValue() : null;
    }

    @JsonIgnore
    public BigDecimal getCorrectRateBigDecimal() {
        return correctRate;
    }

    @JsonIgnore
    public BigDecimal getIncorrectRateBigDecimal() {
        return incorrectRate;
    }

    @JsonIgnore
    public BigDecimal getMinPointsBigDecimal() {
        return minPoints;
    }

    @JsonIgnore
    public BigDecimal getMaxPointsBigDecimal() {
        return maxPoints;
    }
}

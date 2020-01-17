package projects.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import projects.quiz.dto.rate.RateCreateDto;
import projects.quiz.dto.rate.RateUpdateDto;
import projects.quiz.utils.enums.ObjectType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rate extends AbstractBaseEntity<Long> {

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creationTime = OffsetDateTime.now();

    @NotNull(message = "rated_object_uuid.not_null")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    UUID ratedObjectUuid;

    @NotNull(message = "user_uuid.not_null")
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    UUID userUuid;

    @NotNull(message = "{type.not_null}")
    @Column(nullable = false)
    @Enumerated(value = STRING)
    ObjectType objectType;

    @Min(value = 1, message = "rate.min:1")
    @Max(value = 5, message = "rate.max:5")
    Integer rate;

    @Size(max = 1000, message = "opinion.max_length:1000")
    @Column(nullable = false, length = 1000)
    private String opinion = "";

    public Rate(RateCreateDto dto, ObjectType type, UUID userUuid) {
        this.ratedObjectUuid = dto.getRatedObjectUuid();
        this.userUuid = userUuid;
        this.objectType = type;
        this.rate = dto.getRate() != null ? dto.getRate() : this.rate;
        this.opinion = dto.getOpinion() != null ? dto.getOpinion() : this.opinion;
    }

}

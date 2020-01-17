package projects.quiz.dto.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateCreateDto {

    @NotNull(message = "rated_object_uuid.not_null")
    UUID ratedObjectUuid;

    @Min(value = 1, message = "rate.min:1")
    @Max(value = 5, message = "rate.max:5")
    Integer rate;

    String opinion;
}

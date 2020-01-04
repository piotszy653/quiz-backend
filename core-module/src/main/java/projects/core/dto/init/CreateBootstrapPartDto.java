package projects.core.dto.init;

import projects.core.config.init.BootstrapPartName;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateBootstrapPartDto {

    @NotNull(message = "{key.not_null}")
    private BootstrapPartName key;

}

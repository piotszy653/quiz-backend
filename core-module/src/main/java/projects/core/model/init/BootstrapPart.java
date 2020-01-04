package projects.core.model.init;

import projects.core.config.init.BootstrapPartName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
public class BootstrapPart extends AbstractBaseEntity<Long> {

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "key.not_null")
    BootstrapPartName key;

}

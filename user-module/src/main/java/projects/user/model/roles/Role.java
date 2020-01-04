package projects.user.model.roles;

import projects.user.model.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Role extends AbstractBaseEntity<Long> {


    @NonNull
    @Column(unique = true, nullable = false)
    @NotBlank(message = "{name.not_blank}")
    @Size(max = 255, message = "name.max:255")
    private String name;
}
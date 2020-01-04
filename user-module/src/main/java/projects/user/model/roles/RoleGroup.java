package projects.user.model.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import projects.user.model.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class RoleGroup extends AbstractBaseEntity<Long> {

    @NonNull
    @Column(unique = true, nullable = false)
    @NotBlank(message = "{name.not_blank}")
    @Size(max = 255, message = "name.max:255")
    private String name;

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @NotNull(message = "{roles.not_null}")
    @Size(min = 1, message = "{roles.not_empty}")
    private List<Role> roles = new ArrayList<>();
}
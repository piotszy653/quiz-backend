package projects.user.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import projects.user.model.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class UserProfile extends AbstractBaseEntity<Long> {

    @Size(max = 255, message = "name.max:255")
    private String name;

    private LinkedHashSet<UUID> friends = new LinkedHashSet<>();

    @JsonIgnore
    public UserProfile(String name) {
        this.name = name != null ? name.trim() : null;
    }

}

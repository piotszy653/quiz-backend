package projects.user.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import projects.user.model.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class UserProfile extends AbstractBaseEntity<Long> {

    @Size(max = 255, message = "name.max:255")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = EAGER)
    private Set<User> friends = new LinkedHashSet<>();

    public  UserProfile(String name){
        this.name = name != null ? name.trim() : null;
    }

}

package projects.user.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import projects.user.model.AbstractBaseEntity;
import projects.user.model.roles.Role;
import projects.user.model.roles.RoleGroup;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "\"user\"")
public class User extends AbstractBaseEntity<Long> implements UserDetails {

    @Column(nullable = false, unique = true)
    @NotBlank(message = "{username.not_blank}")
    @Email(message = "{username.mail_required}")
    @Size(max = 255, message = "username.max:255")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "{password.not_blank}")
    @Size(max = 255, message = "password.max:255")
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    @NotNull(message = "{uuid.not_null}")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @NotNull(message = "{profile.not_null}")
    @NonNull
    @OneToOne
    private UserProfile profile;

    @NotNull(message = "{role_group.not_null}")
    @ManyToOne(fetch = FetchType.EAGER)
    RoleGroup roleGroup;

    public User() {
        generateUuid();
    }

    public User(String username, String password, UserProfile profile, RoleGroup roleGroup) {
        this.username = username;
        this.profile = profile;
        this.password = password;
        this.roleGroup = roleGroup;
        generateUuid();
    }

    public void generateUuid() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roleGroup.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return roleGroup.getName().equals("ADMIN");
    }

    @JsonIgnore
    public boolean isManager() {
        return roleGroup.getName().equals("MANAGER");
    }

    @JsonIgnore
    public boolean isUser() {
        return roleGroup.getName().equals("USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

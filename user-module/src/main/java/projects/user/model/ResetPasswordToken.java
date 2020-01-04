package projects.user.model;

import projects.user.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordToken extends AbstractBaseEntity<Long>{

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private Date expirationTime;

    @NotNull
    @OneToOne
    private User user;

    public ResetPasswordToken(String uuid, Long expirationTime, User user){
        this.uuid = uuid;
        this.user = user;
        this.expirationTime = new Date(new Date().getTime() + expirationTime);
    }

    public boolean isExpired(){
        return expirationTime.getTime() < new Date().getTime();
    }
}

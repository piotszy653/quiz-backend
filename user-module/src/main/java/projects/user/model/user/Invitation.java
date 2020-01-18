package projects.user.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import projects.user.model.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inviting_user_id", "invited_user_id"})
})
public class Invitation extends AbstractBaseEntity<Long> {

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creationTime = OffsetDateTime.now();

    @JsonIgnore
    @Column(nullable = false)
    @NotNull(message = "{uuid.not_null}")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid = UUID.randomUUID();

    @NonNull
    @NotNull(message = "{inviting_user.not_null}")
    @ManyToOne(fetch = FetchType.EAGER)
    private User invitingUser;

    @NonNull
    @NotNull(message = "{invited_user.not_null}")
    @ManyToOne(fetch = FetchType.EAGER)
    private User invitedUser;


}

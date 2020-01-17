package projects.quiz.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class AbstractBaseEntity<T> {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Column(nullable = false)
    @NotNull(message = "{uuid.not_null}")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private final UUID uuid = UUID.randomUUID();

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creationTime = OffsetDateTime.now();
}

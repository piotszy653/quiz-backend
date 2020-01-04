package projects.core.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import projects.core.utils.validator.user.CurrentUserHasRole;
import projects.user.dto.user.UserUpdateDto;

import static projects.core.config.enums.roles.RolesEnum.ADMIN;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoreUserUpdateDto extends UserUpdateDto {

    @CurrentUserHasRole(roles = {ADMIN})
    private final Boolean enabled;

    @CurrentUserHasRole(roles = {ADMIN})
    private final String roleGroup;

    public CoreUserUpdateDto(String username, String password, Boolean enabled, String roleGroup) {
        super(username, password, enabled, roleGroup);
        this.enabled = enabled;
        this.roleGroup = roleGroup;
    }
}

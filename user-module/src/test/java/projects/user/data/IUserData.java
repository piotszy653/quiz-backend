package projects.user.data;

import projects.user.dto.user.UserCreateDto;
import projects.user.dto.user.UserUpdateDto;
import projects.user.model.roles.RoleGroup;
import projects.user.model.user.User;


public interface IUserData {

    default String getDefaultUsername() {
        return "mail@mail.com";
    }

    default String getDefaultPassword() {
        return "password";
    }

    default User getDefaultUser(RoleGroup roleGroup) {
        return new User(getDefaultUsername(), getDefaultPassword(), roleGroup);
    }

    default UserCreateDto getDefaultUserCreateDto(RoleGroup roleGroup) {
        return new UserCreateDto(getDefaultUsername(), getDefaultPassword(), true, roleGroup.getName());
    }

    default UserUpdateDto getDefaultUserUpdateDto(RoleGroup roleGroup) {
        return new UserUpdateDto(
                getDefaultUsername(),
                getDefaultPassword(),
                true,
                roleGroup.getName()
        );
    }

    default UserUpdateDto getAnotherUserUpdateDto(RoleGroup roleGroup) {
        return new UserUpdateDto(
                "aaa@aaa.com",
                "pass",
                false,
                roleGroup.getName()
        );
    }
}
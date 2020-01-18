package projects.user.data;

import projects.user.dto.user.UserCreateDto;
import projects.user.dto.user.UserUpdateDto;
import projects.user.model.roles.RoleGroup;
import projects.user.model.user.User;
import projects.user.model.user.UserProfile;


public interface IUserData {

    default String getDefaultUsername() {
        return "mail@mail.com";
    }

    default String getDefaultPassword() {
        return "password";
    }

    default User getDefaultUser(UserProfile userProfile, RoleGroup roleGroup) {
        return new User(getDefaultUsername(), getDefaultPassword(), userProfile, roleGroup);
    }

    default UserCreateDto getDefaultUserCreateDto(RoleGroup roleGroup) {
        return new UserCreateDto(getDefaultUsername(), null, getDefaultPassword(), true, roleGroup.getName());
    }

    default UserUpdateDto getDefaultUserUpdateDto(RoleGroup roleGroup) {
        return new UserUpdateDto(
                getDefaultUsername(),
                null,
                getDefaultPassword(),
                true,
                roleGroup.getName()
        );
    }

    default UserUpdateDto getAnotherUserUpdateDto(RoleGroup roleGroup) {
        return new UserUpdateDto(
                "aaa@aaa.com",
                null,
                "pass",
                false,
                roleGroup.getName()
        );
    }
}
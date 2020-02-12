package projects.user.data;

import projects.user.dto.registration.RegistrationDto;

public interface IRegistrationData extends IUserData {

    default RegistrationDto getDefaultRegistrationDto(boolean regulationsAccepted) {
        return new RegistrationDto(getDefaultUsername(), "name", getDefaultPassword(), regulationsAccepted);
    }
}

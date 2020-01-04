package projects.core.controller.user;

import projects.user.dto.registration.LoginResponseDto;
import projects.user.dto.registration.RegistrationDto;
import projects.user.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto create(@Valid @RequestBody RegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }
}

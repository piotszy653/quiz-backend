package projects.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.dto.user.CoreUserUpdateDto;
import projects.core.service.user.CoreUserService;
import projects.core.utils.validator.user.CurrentUserHasRole;
import projects.user.dto.user.UserCreateDto;
import projects.user.model.user.User;
import projects.user.service.UserService;
import projects.user.utils.validator.currentUser.CurrentUser;

import javax.validation.Valid;

import static projects.core.config.enums.roles.RolesEnum.ADMIN;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final CoreUserService coreUserService;

    @Secured("ROLE_USER_READ")
    @GetMapping
    @CurrentUserHasRole(roles = {ADMIN})
    @ResponseStatus(HttpStatus.OK)
    public Page<User> getAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Secured("ROLE_USER_CREATE")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }

    @Secured("ROLE_USER_READ")
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@CurrentUser @PathVariable long id) {
        return userService.findOne(id);
    }

    @Secured("ROLE_USER_UPDATE")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody CoreUserUpdateDto coreUserUpdateDto, @CurrentUser @PathVariable Long id) {
        return userService.update(coreUserUpdateDto, id);
    }

    @Secured("ROLE_USER_DELETE")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        coreUserService.delete(id);
    }

}
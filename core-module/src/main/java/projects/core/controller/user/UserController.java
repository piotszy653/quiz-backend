package projects.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.dto.user.CoreUserUpdateDto;
import projects.core.service.quiz.CoreQuizService;
import projects.core.service.quiz.CoreRateService;
import projects.core.service.user.CoreUserService;
import projects.quiz.model.Quiz;
import projects.quiz.model.Rate;
import projects.user.dto.user.UserCreateDto;
import projects.user.model.user.User;
import projects.user.service.InvitationService;
import projects.user.service.UserService;
import projects.user.utils.validator.UserExists;
import projects.user.utils.validator.currentUser.CurrentUser;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final CoreUserService coreUserService;

    private final CoreRateService coreRateService;

    private final InvitationService invitationService;

    private final CoreQuizService coreQuizService;

    @Secured("ROLE_USER_READ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userService.findAll();
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

    @GetMapping("/rates")
    @ResponseStatus(HttpStatus.OK)
    public Set<Rate> getRates() {
        return coreRateService.getAllByUser();
    }

    @GetMapping("/friends")
    public Set<User> getFriends(){
        return userService.getFriends();
    }

    @GetMapping("/invited-users")
    public Set<User> getInvitedUsers() {
        return invitationService.getInvitedUsers();
    }

    @GetMapping("/inviting-users")
    public Set<User> getInvitingUsers() {
        return invitationService.getInvitingUsers();
    }

    @PutMapping("/remove-friend/{uuid}")
    public void removeFriend(@PathVariable UUID uuid){
        userService.removeFriend(uuid);
    }


    @GetMapping("/available-quizzes/{uuid}")
    public Set<Quiz> getQuizzesByOwnerUuid(@Valid @PathVariable @UserExists UUID uuid){
        return coreQuizService.getAvailableByOwnerUuid(uuid);
    }
}
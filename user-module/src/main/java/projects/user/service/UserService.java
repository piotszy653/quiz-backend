package projects.user.service;

import projects.user.dto.user.UserCreateDto;
import projects.user.dto.user.UserUpdateDto;
import projects.user.model.user.User;
import projects.user.model.user.UserProfile;
import projects.user.repository.user.UserProfileRepository;
import projects.user.repository.user.UserRepository;
import projects.user.security.model.UserContext;
import projects.user.utils.validator.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private UserProfileRepository userProfileRepository;

    private RoleGroupService roleGroupService;

    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.not_found.username", new Object[]{username}, null)));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByUuid(UUID uuid) {
        return userRepository.existsByUuid(uuid);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("user.not_found.uuid", new Object[]{uuid.toString()}, null)));
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException((messageSource.getMessage("user_id.required", null, null)));
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("user.not_found.id", new Object[]{Long.toString(id)}, null)));
    }

    public Page<User> findAllByIds(List<Long> ids, Pageable pageable) {
        return userRepository.findByIdIn(ids, pageable);
    }

    @Transactional
    public User create(@Valid UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new IllegalArgumentException(messageSource.getMessage("user.exists.username", new Object[]{username}, null));
                });
        User user = new User(
                username,
                new BCryptPasswordEncoder().encode(userCreateDto.getPassword()),
                userProfileRepository.save(new UserProfile(userCreateDto.getName())),
                roleGroupService.getByName(userCreateDto.getRoleGroup()));
        user.setEnabled(userCreateDto.isEnabled());
        return save(user);
    }

    @Transactional
    public Set<User> saveAll(User... users){
        return Stream.of(users).map(this::save).collect(Collectors.toSet());
    }

    @Transactional
    public User save(@Valid User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(@Valid UserUpdateDto userUpdateDto, @CurrentUser Long id) {
        return userRepository.findById(id)
                .map(user -> {
                            user.setUsername(userUpdateDto.getUsername() != null ? userUpdateDto.getUsername() : user.getUsername());
                            user.getProfile().setName(userUpdateDto.getName() != null ? userUpdateDto.getName() : user.getProfile().getName());
                            user.setPassword(userUpdateDto.getPassword() != null ? new BCryptPasswordEncoder().encode(userUpdateDto.getPassword()) : user.getPassword());
                            user.setEnabled(userUpdateDto.getEnabled() != null ? userUpdateDto.getEnabled() : user.isEnabled());
                            user.setRoleGroup(userUpdateDto.getRoleGroup() != null ? roleGroupService.getByName(userUpdateDto.getRoleGroup()) : user.getRoleGroup());
                            save(user);
                            return user;
                        }
                )
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("user.not_found.id", new Object[]{Long.toString(id)}, null)));
    }


    @Transactional
    public void delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException(messageSource.getMessage("user_id.required", null, null));
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("user.not_found.id", new Object[]{Long.toString(id)}, null))
                ));
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public User getCurrentUser() {
        UserContext userContext = ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return userRepository.findByUsername(userContext.getUsername())
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("current_user.not_found.username", new Object[]{userContext.getUsername()}, null)));
    }

    public UUID getCurrentUserUuid(){
        return getCurrentUser().getUuid();
    }

    public void currentUserValidation(long userId) {
        if (!isCurrentUsersId(userId)) {
            throw new IllegalArgumentException(messageSource.getMessage("user.current_user_required", new Object[]{Long.toString(userId)}, null));
        }
    }

    public boolean isCurrentUsersId(long userId) {
        return getCurrentUser().isAdmin() || getCurrentUser().getId() == userId;
    }

    @Transactional
    public void removeFriend(UUID uuid) {

        User currentUser = getCurrentUser();
        User removedUser = findByUuid(uuid);

        currentUser.getProfile().getFriends().remove(removedUser);
        removedUser.getProfile().getFriends().remove(currentUser);

        saveAll(currentUser, removedUser);
    }
}

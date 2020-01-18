package projects.user.unit.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import projects.user.data.IPageRequestData;
import projects.user.data.IRoleGroupData;
import projects.user.data.IUserData;
import projects.user.dto.user.UserCreateDto;
import projects.user.dto.user.UserUpdateDto;
import projects.user.model.user.User;
import projects.user.model.user.UserProfile;
import projects.user.repository.user.UserRepository;
import projects.user.security.model.UserContext;
import projects.user.service.RoleGroupService;
import projects.user.service.UserService;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest implements IUserData, IRoleGroupData, IPageRequestData {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleGroupService roleGroupService;

    @Mock
    MessageSource messageSource;
    
    private UserProfile defaultProfile;

    private User defaultUser;

    @Before
    public void init() {
        defaultProfile = new UserProfile(null);
        defaultUser = getDefaultUser(defaultProfile, getUserRoleGroup());
    }

    @Test
    public void loadAllUsers() {
        //given
        when(userRepository.findAll(getDefaultPageRequest())).thenReturn(new PageImpl<>(Arrays.asList(defaultUser)));

        //when
        Page<User> user = userService.findAll(getDefaultPageRequest());

        //then
        assertNotNull(user);
        assertEquals(user.getContent().size(), 1);
        assertEquals(user.getContent().get(0), defaultUser);
    }

    @Test
    public void loadUserByUsernameWithCorrectUsername() {

        //given
        when(userRepository.findByUsername(getDefaultUsername())).thenReturn(Optional.of(defaultUser));

        //when
        User user = (User) userService.loadUserByUsername(getDefaultUsername());

        //then
        assertNotNull(user);
        assertEquals(user.getUsername(), getDefaultUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameWithIncorrectUsername() {
        userService.loadUserByUsername("aa");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameWithNullUsername() {
        userService.loadUserByUsername(null);
    }

    @Test
    public void findOneWithCorrectId() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));

        //when
        User user = userService.findOne(1L);

        //then
        assertNotNull(user);
        assertEquals(user.getUsername(), getDefaultUsername());
    }

    @Test(expected = NoSuchElementException.class)
    public void findOneWithWrongId() {
        userService.findOne(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOneWithNullId() {
        userService.findOne(null);
    }

    @Test
    public void create() {
        //given
        UserCreateDto userCreateDto = getDefaultUserCreateDto(getDefaultRoleGroup());
        when(roleGroupService.getByName(getDefaultRoleGroup().getName())).thenReturn(getDefaultRoleGroup());
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        User returnedUser = userService.create(userCreateDto);

        //then
        assertNotNull(returnedUser);
        assertEquals(userCreateDto.getUsername(), returnedUser.getUsername());
        assertEquals(userCreateDto.isEnabled(), returnedUser.isEnabled());
        assertEquals(userCreateDto.getRoleGroup(), returnedUser.getRoleGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithExistingUsername() {

        //given
        when(userRepository.findByUsername(getDefaultUsername())).thenReturn(Optional.of(defaultUser));

        //when
        userService.create(getDefaultUserCreateDto(getDefaultRoleGroup()));
    }

    @Test
    public void updateWithCorrectId() {
        //given
        UserUpdateDto userUpdateDto = getDefaultUserUpdateDto(getDefaultRoleGroup());
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
        when(roleGroupService.getByName(getDefaultRoleGroup().getName())).thenReturn(getDefaultRoleGroup());

        //when
        User returnedUser = userService.update(userUpdateDto, 1L);

        //then
        assertNotNull(returnedUser);
        assertEquals(userUpdateDto.getUsername(), returnedUser.getUsername());
        assertEquals(userUpdateDto.getEnabled(), returnedUser.isEnabled());
        assertEquals(userUpdateDto.getRoleGroup(), returnedUser.getRoleGroup().getName());
    }

    @Test
    public void updateWithNullFields() {
        //given
        UserUpdateDto userUpdateDto = getAnotherUserUpdateDto(getRoleGroup("AAA"));
        User user = defaultUser;

        when(roleGroupService.getByName(userUpdateDto.getRoleGroup())).thenReturn(getRoleGroup(userUpdateDto.getRoleGroup()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //when
        User returnedUser = userService.update(userUpdateDto, 1L);

        //then
        assertNotNull(returnedUser);
        assertEquals(userUpdateDto.getUsername(), returnedUser.getUsername());
        assertEquals(userUpdateDto.getEnabled(), returnedUser.isEnabled());
        assertEquals(userUpdateDto.getRoleGroup(), returnedUser.getRoleGroup().getName());
    }

    @Test(expected = NoSuchElementException.class)
    public void updateWithWrongId() {
        userService.update(getDefaultUserUpdateDto(getDefaultRoleGroup()), 1L);
    }

    @Test
    public void deleteWithCorrectId() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
        //when
        userService.delete(1L);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteWithWrongId() {
        userService.delete(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteWithNullId() {
        userService.delete(null);
    }

    @Test
    public void validateMatchingUserId() {
        //given
        User user = defaultUser;
        user.setId(1L);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserContext userContext = new UserContext(getDefaultUsername(), null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userContext, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        userService.currentUserValidation(1L);

        //then
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateNotMatchingUserId() {
        //given
        User user = getDefaultUser(defaultProfile, getUserRoleGroup());
        user.setId(1L);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserContext userContext = new UserContext(getDefaultUsername(), null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userContext, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        userService.currentUserValidation(2L);
    }
}

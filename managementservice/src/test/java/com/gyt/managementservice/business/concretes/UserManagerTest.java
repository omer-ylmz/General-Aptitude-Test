package com.gyt.managementservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.abstracts.RoleService;
import com.gyt.managementservice.business.dtos.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.response.get.GetRoleResponse;
import com.gyt.managementservice.business.dtos.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.response.update.UpdatedUserResponse;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.business.rules.UserBusinessRules;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.entities.concretes.User;
import com.gyt.managementservice.mapper.RoleMapper;
import com.gyt.managementservice.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserBusinessRules userBusinessRules;

    @Mock
    private RoleService roleService;

    @Mock
    private MessageService messageService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_userFound() {
        String username = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(username);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        UserDetails result = userManager.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void loadUserByUsername_userNotFound() {
        String username = "notfound@example.com";
        String errorMessage = "User should exist";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.UserErrors.UserShouldBeExists)).thenReturn(errorMessage);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userManager.loadUserByUsername(username));

        assertEquals(errorMessage, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(username);
        verify(messageService, times(1)).getMessage(Messages.UserErrors.UserShouldBeExists);
    }

    @Test
    void testAddOrganization_Success() {
        RegisterRequest request = new RegisterRequest("org@example.com", "password");
        User user = new User();
        Role role = new Role();
        GetRoleResponse roleResponse = new GetRoleResponse(2L, "organization");

        when(userMapper.registerRequestToUser(any(), any())).thenReturn(user);
        when(roleService.getByIdRole(2L)).thenReturn(roleResponse);
        when(roleMapper.getResponseToRole(roleResponse)).thenReturn(role);

        userManager.addOrganization(request);

        verify(userRepository).save(user);
        verify(userBusinessRules).mailNoCanNotBeDuplicated(request.getEmail());
    }

    @Test
    void testAddOrganization_EmailAlreadyExists() {
        String email = "omer@admin.com";
        RegisterRequest request = new RegisterRequest(email, "password");

        doThrow(new BusinessException("Email already exists"))
                .when(userBusinessRules)
                .mailNoCanNotBeDuplicated(email);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.addOrganization(request);
        });
        assertEquals("Email already exists", thrown.getMessage());
    }

    @Test
    void testGetByIdUser_Success(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setAuthorities(Set.of(new Role(1L,"ROLE_USER",Set.of(user))));

        GetUserResponse expectedResponse = new GetUserResponse();
        expectedResponse.setId(userId);
        expectedResponse.setRoles(List.of("ROLE_USER"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.getUserToResponse(user)).thenReturn(expectedResponse);

        GetUserResponse actualResponse = userManager.getByIdUser(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).getUserToResponse(user);
    }

    @Test
    void testGetByIdUser_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.UserErrors.UserShouldBeExists)).thenReturn("User should exist");

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.getByIdUser(userId);
        });

        assertEquals("User should exist", thrown.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(messageService, times(1)).getMessage(Messages.UserErrors.UserShouldBeExists);
        verify(userMapper, never()).getUserToResponse(any());
    }

    @Test
    void testGetAllUser_Success() {
        int page = 0;
        int size = 10;

        User user = new User();
        user.setId(1L);
        user.setEmail("omer@admin.com");
        user.setAuthorities(Set.of(new Role(1L,"ROLE_USER",Set.of(user))));

        GetAllUserResponse response = new GetAllUserResponse();
        response.setId(1L);
        response.setEmail("omer@admin.com");
        response.setRoleNames(List.of("ROLE_USER"));

        List<User> users = List.of(user);
        Page<User> usersPage = new PageImpl<>(users);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
        when(userMapper.getAllResponseToEntity(user)).thenReturn(response);

        Page<GetAllUserResponse> result = userManager.getAllUser(page, size);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("omer@admin.com", result.getContent().get(0).getEmail());
        assertEquals(List.of("ROLE_USER"), result.getContent().get(0).getRoleNames());
    }



    @Test
    void testUpdatedUser_Success() {
        Long userId = 1L;
        String newEmail = "updatedemail@example.com";
        UpdatedUserRequest request = new UpdatedUserRequest(userId, newEmail, "newpassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("oldemail@example.com");
        existingUser.setAuthorities(Set.of(new Role(1L, "USER", Set.of(existingUser))));

        GetUserResponse authenticatedUserResponse = new GetUserResponse();
        authenticatedUserResponse.setId(userId);
        authenticatedUserResponse.setEmail("authuser@example.com");
        authenticatedUserResponse.setRoles(List.of("ADMIN"));

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail(newEmail);
        updatedUser.setAuthorities(Set.of(new Role(1L, "USER", Set.of(updatedUser))));

        when(authentication.getPrincipal()).thenReturn("authuser@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        when(userRepository.findByEmail("authuser@example.com")).thenReturn(Optional.of(existingUser));
        doNothing().when(userBusinessRules).userUpdateAuthorizationCheck(any(), eq(userId));
        when(userMapper.updatedRequestToUser(request, passwordEncoder)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.updatedUserToResponse(updatedUser)).thenReturn(new UpdatedUserResponse());

        when(userMapper.getUserToResponse(existingUser)).thenReturn(authenticatedUserResponse);

        UpdatedUserResponse response = userManager.updatedUser(request);

        assertNotNull(response);
        verify(userRepository).findById(userId);
        verify(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        verify(userBusinessRules).userUpdateAuthorizationCheck(any(), eq(userId));
        verify(userRepository).save(updatedUser);
        verify(userMapper).updatedUserToResponse(updatedUser);
    }

    @Test
    void testUpdatedUser_UserNotFound() {
        Long userId = 1L;
        String newEmail = "updatedemail@example.com";
        UpdatedUserRequest request = new UpdatedUserRequest(userId, newEmail, "newpassword");

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("authuser@example.com");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(userId)).thenReturn(Optional.empty()); // User not found
        when(messageService.getMessage(Messages.UserErrors.UserShouldBeExists)).thenReturn("User should exist");

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.updatedUser(request);
        });
        assertEquals("User should exist", thrown.getMessage());

        verify(userRepository).findById(userId);
        verify(userBusinessRules, never()).mailNoCanNotBeDuplicated(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).updatedUserToResponse(any());
    }

    @Test
    void testUpdatedUser_EmailAlreadyExists() {
        Long userId = 1L;
        String newEmail = "updatedemail@example.com";
        UpdatedUserRequest request = new UpdatedUserRequest(userId, newEmail, "newpassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("oldemail@example.com");
        existingUser.setAuthorities(Set.of(new Role(1L, "USER", Set.of(existingUser))));

        User anotherUser = new User();
        anotherUser.setEmail(newEmail);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("authuser@example.com");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doThrow(new BusinessException("Email already exists")).when(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        when(userRepository.findByEmail("authuser@example.com")).thenReturn(Optional.of(existingUser));

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.updatedUser(request);
        });
        assertEquals("Email already exists", thrown.getMessage());

        verify(userRepository).findById(userId);
        verify(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).updatedUserToResponse(any());
    }

    @Test
    void testUpdatedUser_UnauthorizedUpdate() {
        Long userId = 1L;
        String newEmail = "updatedemail@example.com";
        UpdatedUserRequest request = new UpdatedUserRequest(userId, newEmail, "newpassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("oldemail@example.com");
        existingUser.setAuthorities(Set.of(new Role(1L, "USER", Set.of(existingUser))));

        GetUserResponse authenticatedUserResponse = new GetUserResponse();
        authenticatedUserResponse.setId(2L); // Different ID
        authenticatedUserResponse.setEmail("authuser@example.com");
        authenticatedUserResponse.setRoles(List.of("USER")); // No authorization to update userId 1

        when(authentication.getPrincipal()).thenReturn("authuser@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        when(userRepository.findByEmail("authuser@example.com")).thenReturn(Optional.of(existingUser));
        doThrow(new BusinessException("Unauthorized to update this user")).when(userBusinessRules).userUpdateAuthorizationCheck(any(), eq(userId));
        when(userMapper.updatedRequestToUser(request, passwordEncoder)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.updatedUserToResponse(existingUser)).thenReturn(new UpdatedUserResponse());
        when(userMapper.getUserToResponse(existingUser)).thenReturn(authenticatedUserResponse);
        when(userManager.getAuthenticatedUser()).thenReturn(authenticatedUserResponse);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.updatedUser(request);
        });
        assertEquals("Unauthorized to update this user", thrown.getMessage());

        verify(userRepository).findById(userId);
        verify(userBusinessRules).mailNoCanNotBeDuplicated(newEmail);
        verify(userBusinessRules).userUpdateAuthorizationCheck(any(), eq(userId));
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).updatedUserToResponse(any());
    }

    @Test
    void testSetUserAuthorities_WithMultipleRoles() {
        Role role1 = new Role(1L, "user", Set.of());
        Role role2 = new Role(2L, "admin", Set.of());
        Set<Role> roles = Set.of(role1, role2);

        Set<Role> result = userManager.setUserAuthorities(roles);

        assertEquals(Set.of(role1), result, "The result should contain only the first role from the set");
    }

    @Test
    void testSetUserAuthorities_WithSingleRole() {
        Role role = new Role(1L, "user", Set.of());
        Set<Role> roles = Set.of(role);

        Set<Role> result = userManager.setUserAuthorities(roles);

        assertEquals(Set.of(role), result, "The result should contain the single role in the set");
    }

    @Test
    void testSetUserAuthorities_EmptySet() {
        Set<Role> roles = Set.of();

        Set<Role> result = userManager.setUserAuthorities(roles);

        assertEquals(roles, result, "The result should be the same empty set");
    }

    @Test
    void testGetAuthenticatedUser_Success() {
        User user = new User();
        user.setEmail("authuser@example.com");
        user.setAuthorities(Set.of(new Role(1L, "USER", Set.of())));

        GetUserResponse response = new GetUserResponse();
        response.setId(1L);
        response.setEmail("authuser@example.com");
        response.setRoles(List.of("USER"));

        when(authentication.getPrincipal()).thenReturn("authuser@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("authuser@example.com")).thenReturn(Optional.of(user));
        when(userMapper.getUserToResponse(user)).thenReturn(response);

        GetUserResponse result = userManager.getAuthenticatedUser();

        assertNotNull(result);
        assertEquals("authuser@example.com", result.getEmail());
        assertEquals(List.of("USER"), result.getRoles());
    }


    @Test
    void testGetAuthenticatedUser_NoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null); // Kimlik doğrulama yok
        SecurityContextHolder.setContext(securityContext);


        doThrow(new BusinessException("User not authenticated"))
                .when(userBusinessRules).ifNotAuthenticated(any());

        BusinessException thrown = assertThrows(BusinessException.class, () -> userManager.getAuthenticatedUser());
        assertEquals("User not authenticated", thrown.getMessage());
    }

    @Test
    void testDeleteUserById_Success() {
        Long userId = 1L;
        doNothing().when(userBusinessRules).userShouldBeExist(userId);
        doNothing().when(userRepository).deleteById(userId);

        userManager.deleteUserById(userId);

        verify(userBusinessRules).userShouldBeExist(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUserById_UserNotFound() {
        Long userId = 1L;
        doThrow(new BusinessException("User does not exist")).when(userBusinessRules).userShouldBeExist(userId);

        BusinessException thrown = assertThrows(BusinessException.class, () -> userManager.deleteUserById(userId));
        assertEquals("User does not exist", thrown.getMessage());
    }

    @Test
    void testGetByEmail_Success() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userManager.getByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetByEmail_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.UserErrors.UserShouldBeExists)).thenReturn("User should exist");

        BusinessException thrown = assertThrows(BusinessException.class, () -> userManager.getByEmail(email));
        assertEquals("User should exist", thrown.getMessage());
    }


}



package com.gyt.managementservice.business.concretes;


import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.abstracts.RoleService;
import com.gyt.managementservice.business.dtos.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.response.get.GetRoleResponse;
import com.gyt.managementservice.business.dtos.response.get.GetUserResponse;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.business.rules.UserBusinessRules;

import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.entities.concretes.User;
import com.gyt.managementservice.mapper.RoleMapper;
import com.gyt.managementservice.mapper.UserMapper;
import kafka.utils.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;

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
        // Arrange
        String email = "omer@admin.com";
        RegisterRequest request = new RegisterRequest(email, "password");

        doThrow(new BusinessException("Email already exists"))
                .when(userBusinessRules)
                .mailNoCanNotBeDuplicated(email);

        // Act & Assert
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.addOrganization(request);
        });
        assertEquals("Email already exists", thrown.getMessage());
    }

    @Test
    void testAddOrganization_RoleNotFound() {
        // Arrange
        String email = "omer@admin.com";
        RegisterRequest request = new RegisterRequest(email, "password");

        // Metodun void olması durumunda doNothing() kullanılır
        doNothing().when(userBusinessRules).mailNoCanNotBeDuplicated(email);

        when(userMapper.registerRequestToUser(any(), any())).thenReturn(new User());
        when(roleService.getByIdRole(2L)).thenReturn(null); // Role not found

        // Act & Assert
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userManager.addOrganization(request);
        });
        assertEquals("Role not found", thrown.getMessage());

        // Verify interactions
        verify(userBusinessRules).mailNoCanNotBeDuplicated(email);
        verify(userMapper).registerRequestToUser(any(), any());
        verify(roleService).getByIdRole(2L);
        verify(userRepository, never()).save(any(User.class)); // Ensure save was not called
    }

}
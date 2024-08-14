package com.gyt.managementservice.business.concretes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gyt.corepackage.jwt.JwtService;
import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.request.LoginRequest;
import com.gyt.managementservice.business.rules.AuthBusinessRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class  AuthManagerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private AuthBusinessRules authBusinessRules;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthManager authManager; // Bu sınıfınızda login metodunu içeren sınıf

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO: 14.08.2024 bunu yapamadım tekrar incelenecek
//    @Test
//    void testLogin_Success() {
//        // Arrange
//        String email = "test@example.com";
//        String token = "mockToken";
//        LoginRequest request = new LoginRequest();
//        request.setEmail(email);
//
//        GrantedAuthority authority = () -> "ROLE_USER"; // Create a mock authority
//        List<GrantedAuthority> authorities = Collections.singletonList(authority);
//
//        // Mock the behavior
//        when(jwtService.generateToken(anyString(), anyList())).thenReturn(token);
//        when(userService.loadUserByUsername(email)).thenReturn(userDetails);
//        when(userDetails.getUsername()).thenReturn(email);
//        when(userDetails.getAuthorities()).thenReturn(authorities);
//
//        // Act
//        String result = authManager.login(request); // Ensure this matches your actual service class name
//
//        // Assert
//        assertEquals(token, result);
//        verify(authBusinessRules).authenticationControl(request);
//        verify(userService).loadUserByUsername(email);
//        verify(jwtService).generateToken(eq(email), eq(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
//    }

    @Test
    void testLogin_AuthenticationFailure() {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "password");

        doThrow(new UsernameNotFoundException("User not found")).when(userService).loadUserByUsername(request.getEmail());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authManager.login(request));
        verify(authBusinessRules).authenticationControl(request);
        verify(userService).loadUserByUsername(request.getEmail());
    }
}

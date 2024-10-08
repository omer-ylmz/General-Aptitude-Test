package com.gyt.managementservice.business.concretes;

import com.gyt.corepackage.jwt.JwtService;
import com.gyt.managementservice.business.abstracts.AuthService;
import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.request.LoginRequest;
import com.gyt.managementservice.business.rules.AuthBusinessRules;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuthManager implements AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthBusinessRules authBusinessRules;

    @Override
    public String login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        authBusinessRules.authenticationControl(request);

        UserDetails user = userService.loadUserByUsername(request.getEmail());

        log.info("Login successful for email: {}", request.getEmail());

        return jwtService.generateToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}

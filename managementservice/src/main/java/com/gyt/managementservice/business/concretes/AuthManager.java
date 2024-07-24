package com.gyt.managementservice.business.concretes;

import com.gyt.corepackage.jwt.JwtService;
import com.gyt.managementservice.business.abstracts.AuthService;
import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.requests.LoginRequest;
import com.gyt.managementservice.business.dtos.requests.RegisterRequest;
import com.gyt.managementservice.business.rules.AuthBusinessRules;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthManager implements AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthBusinessRules authBusinessRules;
    private final UserRepository userRepository;
    private final UserManager userManager; // TODO: 24.07.2024 silinecek

    @Override
    public void register(RegisterRequest request) {
        userService.add(request);
    }

    @Override
    public String login(LoginRequest request) {
        authBusinessRules.authenticationControl(request);

        UserDetails user = userService.loadUserByUsername(request.getEmail());
        User foundUser = userRepository.findByEmail(request.getEmail()).orElseThrow();

        User user1 = userManager.getUserPrincipals();

        return jwtService.generateToken(foundUser.getId(),user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}

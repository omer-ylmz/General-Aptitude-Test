package com.gyt.managementservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.dtos.request.LoginRequest;
import com.gyt.managementservice.business.messages.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthBusinessRules {

    private final AuthenticationManager authenticationManager;
    private final MessageService messageService;

    // TODO: 26.07.2024 bu method başka bir hata daha basıyor !
    public void authenticationControl(LoginRequest request) {
        try {
            log.info("Starting authentication process for user: {}", request.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            log.info("Authentication successful for user: {}", request.getEmail());
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}. Exception: {}", request.getEmail(), e.getMessage());
            throw new BusinessException(messageService.getMessage(Messages.AuthErrors.AuthenticationFailed));
        }
    }
}

package com.gyt.managementservice.api.controllers;


import com.gyt.managementservice.business.abstracts.AuthService;
import com.gyt.managementservice.business.dtos.requests.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginRequest request)
    {
        log.info("Login request received via email: {}", request.getEmail());
        String login = authService.login(request);
        log.info("Successfully logged in with Email: {}", request.getEmail());
        return login;
    }



}

package com.gyt.managementservice.api.controllers;


import com.gyt.managementservice.business.abstracts.AuthService;
import com.gyt.managementservice.business.dtos.requests.LoginRequest;
import com.gyt.managementservice.business.dtos.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request)
    {
        authService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginRequest request)
    {
        return authService.login(request);
    }

    //TODO buraya eklemeler yapılacak

}

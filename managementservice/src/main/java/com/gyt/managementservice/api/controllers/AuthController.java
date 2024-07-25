package com.gyt.managementservice.api.controllers;


import com.gyt.managementservice.business.abstracts.AuthService;
import com.gyt.managementservice.business.dtos.requests.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginRequest request)
    {
        return authService.login(request);
    }


    //TODO buraya eklemeler yapılacak

}

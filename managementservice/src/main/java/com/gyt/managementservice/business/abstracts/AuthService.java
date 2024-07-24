package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.requests.LoginRequest;
import com.gyt.managementservice.business.dtos.requests.RegisterRequest;

public interface AuthService {
    String login(LoginRequest request);
    void register(RegisterRequest request);
}

package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.requests.request.LoginRequest;


public interface AuthService {
    String login(LoginRequest request);
}

package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.requests.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void add(RegisterRequest request);
}

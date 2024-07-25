package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.requests.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.requests.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.requests.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.update.UpdatedUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void addOrganization(RegisterRequest request);
    GetUserResponse getByIdOrganization(Long id);
    Page<GetAllUserResponse> getAllOrganization(int page, int size);
    UpdatedUserResponse updatedUser(UpdatedUserRequest request);


}

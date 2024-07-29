package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.response.update.UpdatedUserResponse;
import com.gyt.managementservice.entities.concretes.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void addOrganization(RegisterRequest request);
    GetUserResponse getByIdUser(Long id);
    Page<GetAllUserResponse> getAllUser(int page, int size);
    UpdatedUserResponse updatedUser(UpdatedUserRequest request);
    GetUserResponse getAuthenticatedUser();
    void deleteUserById(Long id);
    User getByEmail(String email);


}

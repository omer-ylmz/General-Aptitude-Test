package com.gyt.managementservice.business.concretes;

import com.gyt.managementservice.business.abstracts.RoleService;
import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.requests.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.requests.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.requests.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.get.GetRoleResponse;
import com.gyt.managementservice.business.dtos.requests.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.update.UpdatedUserResponse;
import com.gyt.managementservice.business.rules.UserBusinessRules;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.entities.concretes.User;
import com.gyt.managementservice.mapper.RoleMapper;
import com.gyt.managementservice.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserManager implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;  // TODO: 25.07.2024 buradan kalkacak
    private final UserBusinessRules userBusinessRules;
    private final RoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new AccessDeniedException("Giriş başarısız."));
    }

    @Override
    public void addOrganization(RegisterRequest request) {
        User user = UserMapper.INSTANCE.registerRequestToUser(request, passwordEncoder);
        GetRoleResponse roleResponse = roleService.getByIdRole(Long.valueOf(2));
        Role role = RoleMapper.INSTANCE.getResponseToRole(roleResponse);
        user.setAuthorities(Set.of(role));
        userRepository.save(user);
    }

    @Override
    public GetUserResponse getByIdOrganization(Long id) {
        userBusinessRules.userShouldBeExist(id);

        User user = userRepository.findByIdAndRoleName(id, "organization").orElseThrow();
        return UserMapper.INSTANCE.getUserToResponse(user);
    }

    @Override
    public Page<GetAllUserResponse> getAllOrganization(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> UserMapper.INSTANCE.getAllResponseToEntity(user));

    }

    @Override
    public UpdatedUserResponse updatedUser(UpdatedUserRequest request) {
        userBusinessRules.userShouldBeExist(request.getId());
        User authenticatedUser = getAuthenticatedUser();
        userBusinessRules.userUpdateAuthorizationCheck(authenticatedUser,request.getId());
        User user = UserMapper.INSTANCE.updatedRequestToUser(request, passwordEncoder);
        GetRoleResponse roleResponse = roleService.getByIdRole(Long.valueOf(2));
        Role role = RoleMapper.INSTANCE.getResponseToRole(roleResponse);
        user.setAuthorities(Set.of(role));
        userRepository.save(user);
        return UserMapper.INSTANCE.updatedUserToResponse(user);
    }


    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userBusinessRules.ifNotAuthenticated(authentication);
        return userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow();
    }

}

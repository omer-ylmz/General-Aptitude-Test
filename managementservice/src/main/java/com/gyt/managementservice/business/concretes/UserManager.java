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
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserManager implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
    public GetUserResponse getByIdUser(Long id) {
        userBusinessRules.userShouldBeExist(id);
        User user = userRepository.findById(id).get();
        Set<Role> roles = setUserAuthorities(user.getAuthorities());
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        GetUserResponse getUserResponse = UserMapper.INSTANCE.getUserToResponse(user);
        getUserResponse.setRoles(roleNames);
        return getUserResponse;
    }

    @Override
    public Page<GetAllUserResponse> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> {
            GetAllUserResponse response = UserMapper.INSTANCE.getAllResponseToEntity(user);
            List<String> roleNames = user.getAuthorities().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
            response.setRoleNames(roleNames);
            return response;
        });
    }

    @Override
    public UpdatedUserResponse updatedUser(UpdatedUserRequest request) {
        userBusinessRules.userShouldBeExist(request.getId());
        User updateFoundUser = userRepository.findById(request.getId()).orElseThrow();
        GetUserResponse authenticatedUser = getAuthenticatedUser();
        Set<Role> authorities = updateFoundUser.getAuthorities();
        userBusinessRules.userUpdateAuthorizationCheck(authenticatedUser, request.getId());
        User user = UserMapper.INSTANCE.updatedRequestToUser(request, passwordEncoder);
        Set<Role> role = setUserAuthorities(authorities);
        user.setAuthorities(role);
        userRepository.save(user);
        return UserMapper.INSTANCE.updatedUserToResponse(user);
    }


    public Set<Role> setUserAuthorities(Set<Role> authorities) {
        for (Role role : authorities) {
            if (role.getName().equals("organization")) {
                return Set.of(role);
            } else {
                return Set.of(role);
            }
        }
        return authorities;
    }


    public GetUserResponse getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userBusinessRules.ifNotAuthenticated(authentication);
        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow();
        Set<Role> roles = user.getAuthorities();
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        GetUserResponse getUserResponse = UserMapper.INSTANCE.getUserToResponse(user);
        getUserResponse.setRoles(roleNames);
        return getUserResponse;
    }

    @Override
    public void deleteUserById(Long id) {
        userBusinessRules.userShouldBeExist(id);
        userRepository.deleteById(id);
    }

}

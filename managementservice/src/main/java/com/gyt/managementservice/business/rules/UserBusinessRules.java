package com.gyt.managementservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.dtos.response.get.GetUserResponse;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBusinessRules {
    private final UserRepository userRepository;
    private final MessageService messageService;

    public void ifNotAuthenticated(Authentication authentication){
        if (authentication == null || authentication.getPrincipal() == null){
            log.warn("Authentication failed: No user is authenticated");
            throw new BusinessException(messageService.getMessage(Messages.UserErrors.UserShouldBeExists));
        }
        log.info("User is authenticated: {}", authentication.getPrincipal());
    }

    public void userShouldBeExist(Long id){
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()){
            log.error("User not found with ID: {}", id);
            throw new BusinessException(messageService.getMessage(Messages.UserErrors.UserShouldBeExists));
        }
        log.info("User found with ID: {}", id);
    }

    public void userControlViaEmail(String email){
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isEmpty()){
            log.error("User not found with email: {}", email);
            throw new BusinessException(messageService.getMessage(Messages.UserErrors.UserShouldBeExists));
        }
        log.info("User found with email: {}", email);
    }

    public void userUpdateAuthorizationCheck(GetUserResponse user, Long id) {
        List<String> authorities = user.getRoles();
        boolean hasOrganizationRole = false;

        for (String role : authorities) {
            if (role.equals("organization")) {
                hasOrganizationRole = true;
                break;
            }
        }

        if (hasOrganizationRole) {
            if (!user.getId().equals(id)) {
                log.error("Update authorization error: User with ID {} is not allowed to update user with ID {}", user.getId(), id);
                throw new BusinessException(messageService.getMessage(Messages.UserErrors.UpdateAuthorityError));
            }
            log.info("Update authorization check passed for user ID: {}", id);
        }
    }
}

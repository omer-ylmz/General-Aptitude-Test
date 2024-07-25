package com.gyt.managementservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.concretes.UserManager;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.entities.concretes.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserBusinessRules {
    private final UserRepository userRepository;
    private final MessageService messageService;

    public void ifNotAuthenticated(Authentication authentication){
        if (authentication == null || authentication.getPrincipal() == null){
            throw new BusinessException(messageService.getMessage(Messages.UserErrors.UserShouldBeExists));
        }
    }

    public void userShouldBeExist(Long id){
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()){
            throw new BusinessException(messageService.getMessage(Messages.UserErrors.UserShouldBeExists));
        }
    }

    public void userUpdateAuthorizationCheck(User user, Long id) {
        Set<Role> authorities = user.getAuthorities();
        boolean hasOrganizationRole = false;

        for (Role role : authorities) {
            if (role.getName().equals("organization")) {
                hasOrganizationRole = true;
                break;
            }
        }
        if (hasOrganizationRole) {
            if (!user.getId().equals(id)) {
                throw new BusinessException(messageService.getMessage(Messages.UserErrors.UpdateAuthorityError));
            }
        }
    }
}

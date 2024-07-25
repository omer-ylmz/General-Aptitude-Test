package com.gyt.managementservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.entities.concretes.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleBusinessRules {
    private final RoleRepository roleRepository;
    private final MessageService messageService;

    public void roleShouldBeExist(Long id) {
        Optional<Role> foundRole = roleRepository.findById(id);
        if (foundRole.isEmpty()){
            throw new BusinessException(messageService.getMessage(Messages.RoleErrors.RoleShouldBeExists));
        }

    }
}

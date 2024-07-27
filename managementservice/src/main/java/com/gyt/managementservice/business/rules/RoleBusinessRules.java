package com.gyt.managementservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.entities.concretes.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleBusinessRules {
    private final RoleRepository roleRepository;
    private final MessageService messageService;

    public void roleShouldBeExist(Long id) {
        Optional<Role> foundRole = roleRepository.findById(id);
        if (foundRole.isEmpty()){
            log.error("Role not found with ID: {}", id);
            throw new BusinessException(messageService.getMessage(Messages.RoleErrors.RoleShouldBeExists));
        }
        log.info("Role found with ID: {}", id);

    }
}

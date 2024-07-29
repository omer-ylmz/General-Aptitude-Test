package com.gyt.managementservice.business.concretes;

import com.gyt.managementservice.business.abstracts.RoleService;
import com.gyt.managementservice.business.dtos.response.get.GetRoleResponse;
import com.gyt.managementservice.business.rules.RoleBusinessRules;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RoleManager implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleBusinessRules roleBusinessRules;


    @Override
    public GetRoleResponse getByIdRole(Long id) {
        log.debug("Fetching role by ID: {}", id);
        roleBusinessRules.roleShouldBeExist(id);
        Role role = roleRepository.getById(id);
        log.info("Successfully fetched role by ID: {}", id);
        return RoleMapper.INSTANCE.getRoleToResponse(role);
    }
}

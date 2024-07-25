package com.gyt.managementservice.business.concretes;

import com.gyt.managementservice.business.abstracts.RoleService;
import com.gyt.managementservice.business.dtos.requests.response.get.GetRoleResponse;
import com.gyt.managementservice.business.rules.RoleBusinessRules;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleManager implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleBusinessRules roleBusinessRules;


    @Override
    public GetRoleResponse getByIdRole(Long id) {
        roleBusinessRules.roleShouldBeExist(id);

        Role role = roleRepository.getById(id);
        return RoleMapper.INSTANCE.getRoleToResponse(role);
    }
}

package com.gyt.managementservice.business.abstracts;

import com.gyt.managementservice.business.dtos.response.get.GetRoleResponse;

public interface RoleService {
    GetRoleResponse getByIdRole(Long id);
}

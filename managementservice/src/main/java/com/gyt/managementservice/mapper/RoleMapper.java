package com.gyt.managementservice.mapper;

import com.gyt.managementservice.business.dtos.requests.response.get.GetRoleResponse;
import com.gyt.managementservice.entities.concretes.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    GetRoleResponse getRoleToResponse (Role role);
    Role getResponseToRole (GetRoleResponse response);

}

package com.gyt.managementservice.mapper;

import com.gyt.managementservice.business.dtos.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.response.update.UpdatedUserResponse;
import com.gyt.managementservice.entities.concretes.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    User registerRequestToUser(RegisterRequest registerRequest, @Context PasswordEncoder passwordEncoder);

    @Named("encodePassword")
    default String encodePassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    GetUserResponse getUserToResponse(User user);

    GetAllUserResponse getAllResponseToEntity(User user);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    User updatedRequestToUser(UpdatedUserRequest request, @Context PasswordEncoder passwordEncoder);

    UpdatedUserResponse updatedUserToResponse(User user);


}

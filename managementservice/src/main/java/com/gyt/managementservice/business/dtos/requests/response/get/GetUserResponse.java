package com.gyt.managementservice.business.dtos.requests.response.get;

import com.gyt.managementservice.entities.concretes.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserResponse {
    private Long id;
    private String email;
    private List<String> roles;
}

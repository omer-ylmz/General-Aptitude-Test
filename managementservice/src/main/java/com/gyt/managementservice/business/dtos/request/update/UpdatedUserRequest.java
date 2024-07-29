package com.gyt.managementservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatedUserRequest {
    private Long id;
    private String email;
    private String password;
}

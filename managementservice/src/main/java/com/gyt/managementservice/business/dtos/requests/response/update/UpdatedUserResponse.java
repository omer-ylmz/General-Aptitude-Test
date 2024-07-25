package com.gyt.managementservice.business.dtos.requests.response.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatedUserResponse {
    private Long id;
    private String username;

}

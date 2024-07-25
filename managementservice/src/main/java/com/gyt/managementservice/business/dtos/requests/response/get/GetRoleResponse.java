package com.gyt.managementservice.business.dtos.requests.response.get;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetRoleResponse {
    private Long id;
    private String name;
}

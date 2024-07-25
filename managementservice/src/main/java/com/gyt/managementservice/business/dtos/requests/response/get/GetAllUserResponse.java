package com.gyt.managementservice.business.dtos.requests.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllUserResponse {
    private int id;
    private String email;
}

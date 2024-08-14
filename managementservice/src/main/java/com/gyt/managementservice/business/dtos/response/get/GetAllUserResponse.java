package com.gyt.managementservice.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllUserResponse {
    private Long id;
    private String email;
    private List<String> roleNames;
}

package com.gyt.examservice.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserResponse {
    private Long id;
    private String email;
    private List<String> roles;
}

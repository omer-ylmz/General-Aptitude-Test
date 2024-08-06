package com.gyt.examservice.api.clients;

import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.configurations.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="management-service",url = "http://localhost:9001",configuration = FeignConfig.class)
public interface ManagementServiceClient {

    @GetMapping("/api/v1/user/getAuthenticatedUser")
    GetUserResponse getAuthenticatedUser();

}


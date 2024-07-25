package com.gyt.managementservice.api.controllers;

import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.requests.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.requests.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.requests.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.update.UpdatedUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addOrganization")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOrganization(@RequestBody RegisterRequest request)
    {
        userService.addOrganization(request);
    }

    @GetMapping("/getById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getByIdOrganization(@PathVariable Long id){
        return userService.getByIdOrganization(id);
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllUserResponse> getAllOrganization(@RequestParam int page, @RequestParam int size){
        return userService.getAllOrganization(page,size);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UpdatedUserResponse updateUser(@RequestBody UpdatedUserRequest updatedUserRequest){
        return userService.updatedUser(updatedUserRequest);
    }
}

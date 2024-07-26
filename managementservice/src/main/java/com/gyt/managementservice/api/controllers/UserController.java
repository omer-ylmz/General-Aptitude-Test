package com.gyt.managementservice.api.controllers;

import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.requests.request.RegisterRequest;
import com.gyt.managementservice.business.dtos.requests.request.update.UpdatedUserRequest;
import com.gyt.managementservice.business.dtos.requests.response.get.GetAllUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.get.GetUserResponse;
import com.gyt.managementservice.business.dtos.requests.response.update.UpdatedUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addOrganization")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOrganization(@RequestBody RegisterRequest request)
    {
        log.info("Request to add new organization: {}", request.getEmail());
        userService.addOrganization(request);
        log.info("Successfully added new organization", request.getEmail());
    }

    @GetMapping("/getById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getByIdUser(@PathVariable Long id){
        return userService.getByIdUser(id);
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllUserResponse> getAllUser(@RequestParam int page, @RequestParam int size){
        return userService.getAllUser(page,size);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UpdatedUserResponse updateUser(@RequestBody UpdatedUserRequest updatedUserRequest){
        return userService.updatedUser(updatedUserRequest);
    }

    @GetMapping("/getAuthenticatedUser")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getAuthenticatedUser(){
        return userService.getAuthenticatedUser();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id){
         userService.deleteUserById(id);
    }
}

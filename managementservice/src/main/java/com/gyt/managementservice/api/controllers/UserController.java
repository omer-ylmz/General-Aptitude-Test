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
    public GetUserResponse getByIdUser(@PathVariable Long id)
    {
        log.info("Received request to retrieve user by id: {}", id);
        GetUserResponse getUserResponse = userService.getByIdUser(id);
        log.info("Successfully retrieved user with ID: {}", id);
        return getUserResponse;
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllUserResponse> getAllUser(@RequestParam int page, @RequestParam int size)
    {
        log.info("Received request to retrieve all users page : {}, size: {}", page, size);
        Page<GetAllUserResponse> allUser = userService.getAllUser(page, size);
        log.info("Successfully retrieved all users");
        return allUser;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UpdatedUserResponse updateUser(@RequestBody UpdatedUserRequest updatedUserRequest)
    {
        log.info("Request to update user with ID: {}", updatedUserRequest.getId());
        UpdatedUserResponse updatedUserResponse = userService.updatedUser(updatedUserRequest);
        log.info("Successfully updated user with ID: {}", updatedUserRequest.getId());
        return updatedUserResponse;
    }

    @GetMapping("/getAuthenticatedUser")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse getAuthenticatedUser()
    {
        log.info("Received request to retrieve authenticated user");
        GetUserResponse authenticatedUser = userService.getAuthenticatedUser();
        log.info("Successfully retrieved authenticated user");
        return authenticatedUser;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id)
    {
        log.info("Request to delete user by ID: {}", id);
        userService.deleteUserById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }
}

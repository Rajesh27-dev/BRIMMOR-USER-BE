package com.delivery.authservice.controller;

import com.delivery.authservice.dto.UserResponse;
import com.delivery.authservice.dto.CreateUserRequest;
import com.delivery.authservice.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponse> listUsers() throws Exception {
        return service.getAllUsers();
    }

    // ✅ NEW API
    @PostMapping
    public UserResponse createUser(
            @RequestBody CreateUserRequest req
    ) throws Exception {
        return service.createUser(
                req.getName(),
                req.getEmail(),
                req.getPassword()
        );
    }
}



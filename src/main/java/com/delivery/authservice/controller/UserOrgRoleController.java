package com.delivery.authservice.controller;

import com.delivery.authservice.dto.AssignUserRoleRequest;
import com.delivery.authservice.service.UserOrgRoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user-org-roles")
public class UserOrgRoleController {

    private final UserOrgRoleService service;

    public UserOrgRoleController(UserOrgRoleService service) {
        this.service = service;
    }

    @PostMapping
    public void assignUser(@RequestBody AssignUserRoleRequest req) throws Exception {
        service.assignUserToOrg(req);
    }
}

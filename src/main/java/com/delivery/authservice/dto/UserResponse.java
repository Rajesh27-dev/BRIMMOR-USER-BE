package com.delivery.authservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserResponse {

    private String firebaseUid;   // Firebase UID
    private String userId;         // USR_xxx
    private String name;
    private String email;
    private String status;

    // ORG MAPPING (nullable)
    private String orgId;
    private List<String> roles;
}

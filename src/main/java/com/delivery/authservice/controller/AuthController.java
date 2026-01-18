package com.delivery.authservice.controller;

import com.delivery.authservice.dto.GoogleLoginRequest;
import com.delivery.authservice.service.FirebaseAuthService;
import com.delivery.authservice.service.UserService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;
    private final UserService userService;

    public AuthController(FirebaseAuthService firebaseAuthService,
                          UserService userService) {
        this.firebaseAuthService = firebaseAuthService;
        this.userService = userService;
    }

    // IMPORTANT: This handles browser preflight
    @RequestMapping(value = "/google", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> corsPreflight() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google")
    public ResponseEntity<String> googleLogin(
            @RequestBody GoogleLoginRequest request) throws Exception {

        FirebaseToken token =
                firebaseAuthService.verifyToken(request.getIdToken());

        userService.saveUserIfNotExists(token);

        return ResponseEntity.ok("Login successful");
    }
}

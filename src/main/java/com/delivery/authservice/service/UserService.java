package com.delivery.authservice.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    public void saveUserIfNotExists(FirebaseToken token) throws Exception {

        Firestore db = FirestoreClient.getFirestore();
        String uid = token.getUid();

        var ref = db.collection("users").document(uid);
        if (ref.get().get().exists()) return;

        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("userId", generateUserId());
        user.put("name", token.getName());
        user.put("email", token.getEmail());
        user.put("status", "ACTIVE");
        user.put("createdAt", Instant.now().toString());

        ref.set(user);
    }

    private String generateUserId() {
        return "USR_" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}

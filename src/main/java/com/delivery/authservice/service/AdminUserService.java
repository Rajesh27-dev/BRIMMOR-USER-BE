package com.delivery.authservice.service;

import com.delivery.authservice.dto.UserResponse;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import com.delivery.authservice.utils.UserIdGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;

import java.time.Instant;


import java.util.*;

@Service
public class AdminUserService {

    public List<UserResponse> getAllUsers() throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        // 1️⃣ Load all users
        List<QueryDocumentSnapshot> userDocs =
                db.collection("users").get().get().getDocuments();

        Map<String, UserResponse> userMap = new HashMap<>();

        for (QueryDocumentSnapshot doc : userDocs) {

            UserResponse u = new UserResponse();
            u.setFirebaseUid(doc.getString("firebaseUid"));
            u.setUserId(doc.getString("userId"));
            u.setName(doc.getString("name"));
            u.setEmail(doc.getString("email"));
            u.setStatus(doc.getString("status"));
            u.setOrgId(null);
            u.setRoles(List.of());

            userMap.put(u.getFirebaseUid(), u);
        }

        // 2️⃣ Load org-role mappings
        List<QueryDocumentSnapshot> mappings =
                db.collection("user_org_roles").get().get().getDocuments();

        for (QueryDocumentSnapshot m : mappings) {
            String firebaseUid = m.getString("firebaseUid");

            UserResponse u = userMap.get(firebaseUid);
            if (u == null) continue;

            u.setOrgId(m.getString("orgId"));
            u.setRoles((List<String>) m.get("roleIds"));
        }

        return new ArrayList<>(userMap.values());
    }
    public UserResponse createUser(
        String name,
        String email,
        String password
) throws Exception {

    // 1️⃣ Create user in Firebase Auth
    UserRecord.CreateRequest request =
            new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(name)
                    .setDisabled(false);

    UserRecord userRecord =
            FirebaseAuth.getInstance().createUser(request);

    // 2️⃣ Generate internal userId
    String userId = UserIdGenerator.generate();

    // 3️⃣ Store user profile in Firestore
    Firestore db = FirestoreClient.getFirestore();

    Map<String, Object> userDoc = new HashMap<>();
    userDoc.put("firebaseUid", userRecord.getUid());
    userDoc.put("userId", userId);
    userDoc.put("name", name);
    userDoc.put("email", email);
    userDoc.put("status", "ACTIVE");
    userDoc.put("createdAt", Instant.now().toString());

    db.collection("users")
            .document(userRecord.getUid())
            .set(userDoc);

    // 4️⃣ Build response (same as listUsers)
    UserResponse res = new UserResponse();
    res.setFirebaseUid(userRecord.getUid());
    res.setUserId(userId);
    res.setName(name);
    res.setEmail(email);
    res.setStatus("ACTIVE");
    res.setOrgId(null);
    res.setRoles(List.of());

    return res;
}

}

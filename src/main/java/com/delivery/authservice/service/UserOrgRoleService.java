package com.delivery.authservice.service;

import com.delivery.authservice.dto.AssignUserRoleRequest;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserOrgRoleService {

    private static final String COLLECTION = "user_org_roles";

    public void assignUserToOrg(AssignUserRoleRequest req) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        // Document ID = firebaseUid + orgId (guarantees uniqueness)
        String docId = req.getFirebaseUid() + "_" + req.getOrgId();

        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", req.getFirebaseUid());
        data.put("orgId", req.getOrgId());
        data.put("roleIds", req.getRoleIds());
        data.put("updatedAt", Instant.now().toString());

        db.collection(COLLECTION).document(docId).set(data);
    }
}

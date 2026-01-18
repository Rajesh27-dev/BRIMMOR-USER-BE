package com.delivery.authservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) return;

            String json = buildServiceAccountJson();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(
                                    new ByteArrayInputStream(
                                            json.getBytes(StandardCharsets.UTF_8)
                                    )
                            )
                    )
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Firebase init failed", e);
        }
    }

    private String buildServiceAccountJson() {

        Map<String, String> env = System.getenv();

        // REQUIRED fields
        String type = env.get("type");
        String projectId = env.get("project_id");
        String privateKeyId = env.get("private_key_id");
        String privateKey = env.get("private_key");
        String clientEmail = env.get("client_email");
        String clientId = env.get("client_id");

        // OPTIONAL but recommended
        String authUri = env.getOrDefault("auth_uri", "https://accounts.google.com/o/oauth2/auth");
        String tokenUri = env.getOrDefault("token_uri", "https://oauth2.googleapis.com/token");
        String authCertUrl = env.getOrDefault(
                "auth_provider_x509_cert_url",
                "https://www.googleapis.com/oauth2/v1/certs"
        );
        String clientCertUrl = env.get("client_x509_cert_url");
        String universeDomain = env.getOrDefault("universe_domain", "googleapis.com");

        if (type == null || projectId == null || privateKey == null || clientEmail == null) {
            throw new IllegalStateException("Missing Firebase service account environment variables");
        }

        // IMPORTANT: private_key must contain \n
        privateKey = privateKey.replace("\\n", "\n");

        return "{"
                + "\"type\":\"" + type + "\","
                + "\"project_id\":\"" + projectId + "\","
                + "\"private_key_id\":\"" + privateKeyId + "\","
                + "\"private_key\":\"" + privateKey + "\","
                + "\"client_email\":\"" + clientEmail + "\","
                + "\"client_id\":\"" + clientId + "\","
                + "\"auth_uri\":\"" + authUri + "\","
                + "\"token_uri\":\"" + tokenUri + "\","
                + "\"auth_provider_x509_cert_url\":\"" + authCertUrl + "\","
                + "\"client_x509_cert_url\":\"" + clientCertUrl + "\","
                + "\"universe_domain\":\"" + universeDomain + "\""
                + "}";
    }
}

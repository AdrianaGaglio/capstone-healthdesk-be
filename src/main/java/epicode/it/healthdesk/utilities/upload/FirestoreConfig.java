package epicode.it.healthdesk.utilities.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirestoreConfig {

    @Autowired
    @Value("${spring.firestore.bucketName}")
    private String bucketName;

    @Autowired
    @Value("${spring.firestore.config}")
    private String firestoreConfig;

    @Bean
    public FirebaseApp firebaseApp() {
        try (InputStream serviceAccount =
                     new ByteArrayInputStream(firestoreConfig.getBytes(StandardCharsets.UTF_8))) {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(bucketName)
                    .build();

            return FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public StorageClient storageClient() {
        return StorageClient.getInstance();
    }
}

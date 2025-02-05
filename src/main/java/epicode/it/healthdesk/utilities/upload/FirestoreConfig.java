package epicode.it.healthdesk.utilities.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Autowired
    @Value("${spring.firestore.bucketName}")
    private String bucketName;

    @Bean
    public FirebaseApp firebaseApp() {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("src/main/resources/healt-desk-7d46c-firebase-adminsdk-fbsvc-3d15d25b7b.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options = null;

        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(bucketName)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public StorageClient storageClient() {
        return StorageClient.getInstance();
    }
}

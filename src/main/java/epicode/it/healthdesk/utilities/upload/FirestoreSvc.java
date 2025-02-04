package epicode.it.healthdesk.utilities.upload;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirestoreSvc {
    private final FirebaseApp firebase;
    private final StorageClient storageClient;

    public String uploadPrescription(MultipartFile file) {
        // Ottiene l'istanza di Storage
        Storage storage = storageClient.bucket().getStorage();
        String bucketName = firebase.getOptions().getStorageBucket();

        // Genera un nome univoco per il file
        String fileName = "prescriptions/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Genera un token univoco per l'accesso al file
        String token = UUID.randomUUID().toString();

        // Definisce i metadati con il token
        Map<String, String> metadata = new HashMap<>();
        metadata.put("firebaseStorageDownloadTokens", token);

        // Crea il BlobInfo con content type e metadati
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .setMetadata(metadata)
                .build();

        // Carica il file su Firebase Storage
        try {
            Blob blob = storage.create(blobInfo, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Codifica il nome del file per l'URL
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Costruisce e restituisce l'URL del file caricato
        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                bucketName, encodedFileName, token
        );
    }

    public String uploadImages(MultipartFile file) {
        // Ottiene l'istanza di Storage
        Storage storage = storageClient.bucket().getStorage();
        String bucketName = firebase.getOptions().getStorageBucket();

        // Genera un nome univoco per il file
        String fileName = "profile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Genera un token univoco per l'accesso al file
        String token = UUID.randomUUID().toString();

        // Definisce i metadati con il token
        Map<String, String> metadata = new HashMap<>();
        metadata.put("firebaseStorageDownloadTokens", token);

        // Crea il BlobInfo con content type e metadati
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .setMetadata(metadata)
                .build();

        // Carica il file su Firebase Storage
        try {
            Blob blob = storage.create(blobInfo, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Codifica il nome del file per l'URL
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Costruisce e restituisce l'URL del file caricato
        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                bucketName, encodedFileName, token
        );
    }

}

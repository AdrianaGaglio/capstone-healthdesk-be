package epicode.it.healthdesk.utilities.upload;

import com.google.cloud.storage.Acl;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirestoreSvc {
    private final FirebaseApp firebase;
    private final StorageClient storageClient;

    public String uploadPrescription(MultipartFile file) {
        Storage storage = storageClient.bucket().getStorage();
        String bucketName = firebase.getOptions().getStorageBucket();

        // Genera un nome univoco per il file
        String fileName = "documents/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Crea il BlobInfo senza token di accesso
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .build();

        try {
            Blob blob = storage.create(blobInfo, file.getInputStream());

            // Imposta il file come pubblico
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            // Restituisce il link pubblico (senza token)
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName).trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String uploadImages(MultipartFile file) {
        // Ottiene l'istanza di Storage
        Storage storage = storageClient.bucket().getStorage();
        String bucketName = firebase.getOptions().getStorageBucket();

        // Genera un nome univoco per il file
        String fileName = "profile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Crea il BlobInfo con content type
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))) // Rende il file pubblico
                .build();

        // Carica il file su Firebase Storage
        try {
            storage.create(blobInfo, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Costruisce e restituisce l'URL pubblico del file caricato
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName).trim();
    }



}

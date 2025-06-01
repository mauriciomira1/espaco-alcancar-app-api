package br.com.espacoalcancar.espaco_alcancar_app_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        // Tenta carregar o arquivo do resources
        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("espaco-alcancar-fd4bf-firebase-adminsdk-fbsvc-15dbe26e3f.json");

        if (serviceAccount == null) {
            throw new IOException("Arquivo de credencial do Firebase não encontrado em resources!");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
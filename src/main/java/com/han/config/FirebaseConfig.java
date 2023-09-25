package com.han.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
  @Value("${firebase.config.file}")
  private String config;
  @Value("${firebase.bucket}")
  private String bucketName;

  @PostConstruct
  public void initializeFirebase() {
    try {
      ClassPathResource account = new ClassPathResource(config);
      FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(account.getInputStream()))
              .setStorageBucket(bucketName)
              .build();

      FirebaseApp.initializeApp(options);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}

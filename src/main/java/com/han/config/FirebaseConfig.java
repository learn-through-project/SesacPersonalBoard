package com.han.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Log4j2
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

      FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(account.getInputStream()))
              .setStorageBucket(bucketName)
              .build();

      FirebaseApp.initializeApp(options);
    } catch (IOException ex) {
      log.error("Exception in firebase init: >> " + ex.getMessage());
    }
  }

  @Bean
  public Bucket storage() {
    return StorageClient.getInstance().bucket();
  }
}

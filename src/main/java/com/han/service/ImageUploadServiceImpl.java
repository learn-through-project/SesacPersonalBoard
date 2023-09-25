package com.han.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import com.han.repository.PostImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
public class ImageUploadServiceImpl implements ImageUploadService {

  @Value("${firebase.bucket}")
  private String bucketName;
  private Bucket storage;

  private static final String baseUrl = "https://firebasestorage.googleapis.com/v0/b/";
  private static final String mediaQuery = "?alt=media";
  private static final String urlPart = "/o/";

  @Autowired
  public ImageUploadServiceImpl(Bucket storage) {
    this.storage = storage;
  }

  private String getUrl(String blobName) {
    String encoded = URLEncoder.encode(blobName, StandardCharsets.UTF_8);
    return baseUrl + bucketName + urlPart + encoded + mediaQuery;
  }

  @Override
  public String uploadImage(MultipartFile file) throws IOException {
    Blob blob = storage.create(file.getName(), file.getBytes(), file.getContentType());
    String url = getUrl(blob.getName());
    return url;
  }

  @Override
  public List<String> uploadImages(MultipartFile[] files) throws IOException {
    List<String> urls = new LinkedList<>();

    for (int i = 0; i < files.length; i++) {
      try {
        urls.add(uploadImage(files[i]));
      } catch (StorageException ex) {
        log.error("Error in " + i + "th file upload: >> " + ex.getMessage());
      }
    }

    return urls;
  }
}

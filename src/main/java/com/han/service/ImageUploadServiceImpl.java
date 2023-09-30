package com.han.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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
  public boolean deleteImage(String blobName) {
    boolean isSuccess = false;

    try {
      Blob blob = storage.get(blobName);
      isSuccess = blob.delete();
    } catch (StorageException ex) {
      log.error(blobName + "파일 삭제에 실패하였습니다.");
    }

    return isSuccess;
  }

  @Override
  public void deleteImages(List<MultipartFile> files, String pathPrefix) throws IOException {
    for (int i = 0; i < files.size(); i++) {
      deleteImage(pathPrefix + "/" + i);
    }
  }

  @Override
  public String uploadImage(MultipartFile file, String pathname) throws IOException {
    Blob blob = storage.create(pathname, file.getBytes(), file.getContentType());
    String url = getUrl(blob.getName());
    return url;
  }

  @Override
  public List<String> uploadImages(List<MultipartFile> files, String pathPrefix) throws IOException {
    List<String> urls = new LinkedList<>();

    for (int i = 0; i < files.size(); i++) {
      urls.add(uploadImage(files.get(i), pathPrefix + "/" +  i));
    }

    return urls;
  }
}

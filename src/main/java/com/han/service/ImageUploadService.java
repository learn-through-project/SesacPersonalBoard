package com.han.service;

import com.google.cloud.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageUploadService {
  public String uploadImage(MultipartFile file) throws IOException;
  public List<String> uploadImages(List<MultipartFile> files) throws IOException;
}

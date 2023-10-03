package com.han.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageUploadService {
  boolean deleteImage(String pathname);
  void deleteImages (List<MultipartFile> files, String pathPrefix) throws IOException;
  String uploadImage(MultipartFile file, String pathname) throws IOException;
  List<String> uploadImages(List<MultipartFile> files, String pathPrefix) throws IOException;
  String updateImagePath(String prevPath, String currentPath);
  public MultipartFile downloadFile(String path);
}

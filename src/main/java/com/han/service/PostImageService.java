package com.han.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PostImageService {
  boolean createPostImage(int postId, List<MultipartFile> files) throws IOException, SQLException;
}

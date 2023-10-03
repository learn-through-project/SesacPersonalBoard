package com.han.service;

import com.han.model.PostImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PostImageService {
  boolean editPostImage(Integer postId, List<MultipartFile> files, List<Integer> fileFlags) throws Exception;
  List<PostImage> getAllImagesByPostId(Integer postId) throws SQLException;
  boolean createPostImage(Integer postId, List<MultipartFile> files) throws Exception;
  boolean deletePostImage(Integer postId) throws Exception;
}

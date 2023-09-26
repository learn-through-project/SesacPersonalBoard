package com.han.service;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostService {
  List<Post> getPostList(PostListReqDto dto) throws SQLException;
  Optional<Post> getPostDetail(int postId) throws SQLException;
  boolean createPost(PostCreateDto dto, List<MultipartFile> files) throws SQLException;
  boolean editPost(PostUpdateDto dto) throws SQLException;
  boolean deletePermanently(int postId) throws SQLException;
}

package com.han.service;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostService {
  List<Post> getPostList(PostListReqDto dto) throws SQLException;
  Optional<Post> getPostDetail(int postId) throws SQLException;
  boolean createPost(PostCreateDto dto) throws SQLException;
}

package com.han.service;

import com.han.dto.PostCreateDto;
import com.han.dto.PostDetailDto;
import com.han.dto.PostEditDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostService {
  List<Post> getPostList(PostListDto dto) throws SQLException;
  Optional<PostDetailDto> getPostDetail(int postId) throws SQLException;
  boolean createPost(PostCreateDto dto) throws Exception;
  boolean editPost(PostEditDto dto) throws Exception;
  boolean deletePermanently(int postId) throws Exception;
  int getPostListTotalCount() throws SQLException;
}

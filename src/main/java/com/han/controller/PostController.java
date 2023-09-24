package com.han.controller;

import com.han.dto.PostListReqDto;
import com.han.model.Post;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface PostController {
  public ResponseEntity<List<Post>> getPostList(PostListReqDto reqDto) throws SQLException;
}

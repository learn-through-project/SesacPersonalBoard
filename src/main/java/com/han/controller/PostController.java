package com.han.controller;

import com.han.dto.PostListReqDto;
import com.han.model.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.List;

public interface PostController {
  public ResponseEntity<List<Post>> getPostList(@Valid PostListReqDto reqDto) throws SQLException;
  public ResponseEntity<Post> getPostDetail(@PathVariable @Min(1)int postId) throws SQLException;
}

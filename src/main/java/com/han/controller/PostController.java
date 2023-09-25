package com.han.controller;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

public interface PostController {
  public ResponseEntity<List<Post>> getPostList(@Valid PostListReqDto reqDto) throws SQLException;
  public ResponseEntity<Post> getPostDetail(@PathVariable @Min(1)int postId) throws SQLException;
  public ResponseEntity<Boolean> createPost(@RequestBody @Valid PostCreateDto dto) throws SQLException;
  public ResponseEntity<Boolean> editPost(@RequestBody @Valid PostUpdateDto dto) throws SQLException;
  public ResponseEntity<Boolean> deletePermanentlyPost(@RequestParam("id") @Min(1) int postId) throws SQLException;
}

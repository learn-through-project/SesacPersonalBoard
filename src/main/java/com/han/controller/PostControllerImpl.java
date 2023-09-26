package com.han.controller;

import com.han.constants.EndPoint;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@Validated
public class PostControllerImpl implements PostController {

  private final PostService postService;

  @Autowired
  public PostControllerImpl(PostService postService) {
    this.postService = postService;
  }

  @Override
  @DeleteMapping(EndPoint.POST)
  public ResponseEntity<Boolean> deletePermanentlyPost(@RequestParam("id") @Min(1) int postId) throws SQLException {
    boolean result = postService.deletePermanently(postId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @Override
  @PutMapping(EndPoint.POST)
  public ResponseEntity<Boolean> editPost(@RequestBody @Valid PostUpdateDto dto) throws SQLException {
    boolean result = postService.editPost(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @Override
  @PostMapping(EndPoint.POST)
  public ResponseEntity<Boolean> createPost(
          @RequestParam("post") PostCreateDto dto,
          @RequestParam("images") List<MultipartFile> images) throws SQLException {

    boolean result = postService.createPost(dto, images);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @Override
  @GetMapping(EndPoint.POST)
  public ResponseEntity<List<Post>> getPostList(@Valid PostListReqDto reqDto) throws SQLException {
    List<Post> postList = postService.getPostList(reqDto);
    return new ResponseEntity<>(postList, HttpStatus.OK);
  }

  @Override
  @GetMapping(EndPoint.POST_DETAIL)
  public ResponseEntity<Post> getPostDetail(@PathVariable @Min(1) int postId) throws SQLException {
    Optional<Post> post = postService.getPostDetail(postId);
    Post body = post.orElse(null);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }


  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    // getPostDetail 의 postId에 character 들어오면 발생
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<String> handleSqlException(SQLException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

}

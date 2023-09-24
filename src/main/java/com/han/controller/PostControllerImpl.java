package com.han.controller;

import com.han.constants.EndPoint;
import com.han.dto.PostListReqDto;
import com.han.model.Post;
import com.han.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(EndPoint.POST)
public class PostControllerImpl implements PostController {

  private final PostService postService;

  @Autowired
  public PostControllerImpl(PostService postService) {
    this.postService = postService;
  }


  @GetMapping()
  public ResponseEntity<List<Post>> getPostList(@Valid PostListReqDto reqDto) throws SQLException {
      List<Post> postList = postService.getPostList(reqDto);
      return new ResponseEntity<>(postList, HttpStatus.OK);
  }



  @ExceptionHandler(SQLException.class)
  public ResponseEntity<String> handelSqlException(SQLException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handelIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

}

package com.han.controller;

import com.han.constants.EndPoint;
import com.han.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostViewController {

  private final PostService postService;

  @Autowired
  public PostViewController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping(EndPoint.POST)
  public String getPostList() {
    return "postList";
  }
}

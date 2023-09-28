package com.han.controller;

import com.han.constants.EndPoint;
import com.han.constants.ViewName;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Log4j2
@Controller
public class PostViewControllerImpl implements PostViewController {
  private final PostService postService;

  @Autowired
  public PostViewControllerImpl(PostService postService) {
    this.postService = postService;
  }


  @PostMapping(EndPoint.POST)
  @Override
  public String createPost(@Valid @RequestParam("post") PostCreateDto dto,
                           @RequestParam("images") List<MultipartFile> images,
                           BindingResult br,
                           Model model) throws SQLException, IOException {

    if (br.hasErrors()) {
      return ViewName.POST_NEW_VIEW;
    }

    boolean isSuccess = postService.createPost(dto, images);

    model.addAttribute("isSuccess", isSuccess);
    return ViewName.POST_NEW_RESULT;
  }


  @GetMapping(EndPoint.POSTS)
  @Override
  public String getPostList(@Valid PostListDto dto, BindingResult br, Model model) throws SQLException {
    PostListDto validDto = !br.hasErrors() ? dto : PostListDto.getDefaultInstance();

    List<Post> list = postService.getPostList(validDto);
    int count = postService.getPostListTotalCount();

    model.addAttribute("list", list);
    model.addAttribute("count", count);

    return ViewName.POST_LIST;
  }
}

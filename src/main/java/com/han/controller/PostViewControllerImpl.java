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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

  @GetMapping(EndPoint.POST_NEW)
  @Override
  public String showCreatePostForm(@Valid @ModelAttribute("post") PostCreateDto dto, BindingResult br) {
    // ViewName.POST_NEW_VIEW 에서 br을 사용하고 있습니다
    // model에 isRedirected 라는 0 또는 1을 갖는 플래그가 필요합니다.
    return ViewName.POST_NEW_VIEW;
  }

  @PostMapping(EndPoint.POST)
  @Override
  public String createPost(
          @Valid @ModelAttribute("post") PostCreateDto dto,
          BindingResult br,
          RedirectAttributes redirectAttributes
  ) {

    if (!br.hasErrors()) {
      try {
        boolean isSuccess = postService.createPost(dto);
        redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? 1 : 0);
      } catch (Exception ex) {
        redirectAttributes.addFlashAttribute("isSuccess", 0);
        redirectAttributes.addFlashAttribute("msg", ex.getMessage());
      }

    }

    redirectAttributes.addFlashAttribute("post", dto);
    redirectAttributes.addFlashAttribute("isRedirected", 1);
    return "redirect:" + EndPoint.POST_NEW;
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

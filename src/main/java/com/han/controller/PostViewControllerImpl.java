package com.han.controller;

import com.han.constants.EndPoint;
import com.han.constants.ViewName;
import com.han.dto.PostCreateDto;
import com.han.dto.PostDetailDto;
import com.han.dto.PostEditDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
public class PostViewControllerImpl implements PostViewController {
  private final PostService postService;

  @Autowired
  public PostViewControllerImpl(PostService postService) {
    this.postService = postService;
  }

  @DeleteMapping("post/{postId}")
  @Override
  public String deletePost(@PathVariable("postId") Integer postId) throws SQLException {
    postService.deletePermanently(postId);
    return ViewName.POST_LIST;
  }

  @PutMapping(EndPoint.POST)
  @Override
  public String editPost(
          @Valid @ModelAttribute("post") PostEditDto dto,
          BindingResult br,
          RedirectAttributes redirectAttributes
  ) {

    if (br.hasErrors()) {
      redirectAttributes.addFlashAttribute("post", dto);
      redirectAttributes.addFlashAttribute("isSuccess", 0);
      redirectAttributes.addFlashAttribute("isRedirected", 1);
      return "redirect:" + "/post/" + dto.getId() + "/edit";
    }


    boolean isSuccess = false;
    try {
      isSuccess = postService.editPost(dto);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("isSuccess", 0);
      redirectAttributes.addFlashAttribute("msg", ex.getMessage());
    }

    if (isSuccess) {
      redirectAttributes.addFlashAttribute("isSuccess", 1);
      return "redirect:" + "/post/" + dto.getId();
    } else {
      redirectAttributes.addFlashAttribute("isSuccess", 0);
      redirectAttributes.addFlashAttribute("post", dto);
      redirectAttributes.addFlashAttribute("isRedirected", 1);
      return "redirect:" + "/post/" + dto.getId() + "/edit";
    }

  }

  @GetMapping(EndPoint.POST_EDIT)
  @Override
  public String showPostEditForm(@PathVariable("postId") Integer postId, Model model) {
    Optional<PostDetailDto> detail = Optional.empty();

    try {
      detail = postService.getPostDetail(postId);
    } catch (SQLException ex) {
      log.error("Error in showPostDetail: >> " + ex.getMessage());
      model.addAttribute("error", "데이터를 가져오는 중 문제가 발생하였습니다.");
    }

    model.addAttribute("post", detail.orElse(null));


    return ViewName.POST_EDIT_VIEW;
  }

  @GetMapping(EndPoint.POST_DETAIL)
  @Override
  public String showPostDetail(@PathVariable("postId") Integer postId, Model model) throws SQLException {
    Optional<PostDetailDto> detail = Optional.empty();

    try {
      detail = postService.getPostDetail(postId);
    } catch (SQLException ex) {
      log.error("Error in showPostDetail: >> " + ex.getMessage());
      model.addAttribute("error", "데이터를 가져오는 중 문제가 발생하였습니다.");
    }

    model.addAttribute("detail", detail.orElse(null));
    return ViewName.POST_DETAIL;
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

  @ExceptionHandler(TypeMismatchException.class)
  public String handleTypeMismatchException(TypeMismatchException ex) {
    return "redirect:" + EndPoint.POSTS;
  }
}

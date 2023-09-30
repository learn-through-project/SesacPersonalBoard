package com.han.controller;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListDto.PostListDto;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;

public interface PostViewController {
  public String showCreatePostForm(@Valid @ModelAttribute("post") PostCreateDto dto, BindingResult br);

  String createPost(
          @Valid @ModelAttribute("post") PostCreateDto dto, BindingResult br, RedirectAttributes redirectAttributes
  );

  String getPostList(@Valid PostListDto dto, BindingResult br, Model model) throws SQLException;
}

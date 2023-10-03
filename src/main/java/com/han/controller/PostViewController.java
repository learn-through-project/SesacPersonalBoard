package com.han.controller;

import com.han.constants.EndPoint;
import com.han.dto.PostCreateDto;
import com.han.dto.PostEditDto;
import com.han.dto.PostListDto.PostListDto;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;

public interface PostViewController {
  String showPostDetail(@PathVariable("postId") Integer postId, Model model) throws SQLException;
  String showCreatePostForm(@Valid @ModelAttribute("post") PostCreateDto dto, BindingResult br);
  String createPost(@Valid @ModelAttribute("post") PostCreateDto dto, BindingResult br, RedirectAttributes redirectAttributes);
  String getPostList(@Valid PostListDto dto, BindingResult br, Model model) throws SQLException;
  String editPost(@Valid @ModelAttribute("post") PostEditDto dto, BindingResult br, RedirectAttributes redirectAttributes);
  String showPostEditForm(@PathVariable("postId") Integer postId, Model model);
  String deletePost(@PathVariable ("postId") Integer id) throws Exception;
}

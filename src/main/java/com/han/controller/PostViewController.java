package com.han.controller;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListDto.PostListDto;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PostViewController {
  String createPost(
          @Valid @RequestParam("post") PostCreateDto dto,
          @RequestParam("images") List<MultipartFile> images,
          BindingResult br,
          Model model) throws SQLException, IOException;
  String getPostList(@Valid PostListDto dto, BindingResult br, Model model) throws SQLException;
}

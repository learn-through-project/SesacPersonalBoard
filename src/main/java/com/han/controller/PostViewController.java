package com.han.controller;

import com.han.dto.PostListDto.PostListDto;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

public interface PostViewController {
  String getPostList(@Valid PostListDto dto, BindingResult br, Model model) throws SQLException;
}

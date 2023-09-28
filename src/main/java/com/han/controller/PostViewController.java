package com.han.controller;

import com.han.dto.PostListDto.PostListDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

public interface PostViewController {
  ModelAndView getPostList(@Valid PostListDto dto, BindingResult br) throws SQLException;
}

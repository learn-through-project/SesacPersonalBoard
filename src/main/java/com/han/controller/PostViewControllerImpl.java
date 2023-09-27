package com.han.controller;

import com.han.constants.EndPoint;
import com.han.constants.ViewName;
import com.han.dto.PostListDto.PostListDto;
import com.han.dto.PostListDto.PostListResDto;
import com.han.model.Post;
import com.han.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
public class PostViewControllerImpl implements PostViewController {
  public static final String RESULT = "result";
  private final PostService postService;

  @Autowired
  public PostViewControllerImpl(PostService postService) {
    this.postService = postService;
  }

  @GetMapping(EndPoint.POST)
  public ModelAndView getPostList(@Valid PostListDto dto, BindingResult br) throws SQLException {
    PostListDto validDto = !br.hasErrors() ? dto : PostListDto.getDefaultInstance();

    List<Post> list = postService.getPostList(validDto);
    int count = postService.getPostListTotalCount();

    ModelAndView mv = new ModelAndView(ViewName.POST_LIST);
    PostListResDto result = new PostListResDto(list, count);
    mv.addObject(RESULT, result);
    return mv;
  }
}

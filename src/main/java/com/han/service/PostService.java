package com.han.service;

import com.han.dto.PostListReqDto;
import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
  List<Post> getPostList(PostListReqDto dto) throws SQLException;
}

package com.han.service;

import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
  List<Post> getPostList(String orderBy, int limit, int offset) throws SQLException;
}

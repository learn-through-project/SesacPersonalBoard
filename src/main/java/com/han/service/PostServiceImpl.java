package com.han.service;

import com.han.model.Post;
import com.han.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {


  private final PostRepository postRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }


  @Override
  public List<Post> getPostList(String sort, int limit, int page) throws SQLException {
    int offset = (page - 1) * limit;

    List<Post> list = postRepository.findAll(sort, limit, offset);

    return list;
  }
}

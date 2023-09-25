package com.han.service;

import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {


  private final PostRepository postRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public boolean deletePermanently(int postId) throws SQLException {
    boolean result = postRepository.deletePermanently(postId);
    return result;
  }

  @Override
  public boolean editPost(PostUpdateDto dto) throws SQLException {
    Post post = fromUpdateDtoToPost(dto);
    boolean result = postRepository.update(post);
    return result;
  }

  @Override
  public boolean createPost(PostCreateDto dto) throws SQLException {
    Post post = fromCreateDtoToPost(dto);
    boolean result = postRepository.insert(post);
    return result;
  }

  @Override
  public List<Post> getPostList(PostListReqDto dto) throws SQLException {
    String sort = dto.getSort();
    int page = dto.getPage();
    int limit = dto.getLimit();

    int offset = (page - 1) * limit;
    List<Post> list = postRepository.findAll(sort, limit, offset);

    return list;
  }

  @Override
  public Optional<Post> getPostDetail(int postId) throws SQLException {

    Optional<Post> post = postRepository.findById(postId);

    return post;
  }

  private Post fromCreateDtoToPost(PostCreateDto dto) {
    Post post = new Post(dto.getAuthor(), dto.getTextContent());

    return post;
  }

  private Post fromUpdateDtoToPost(PostUpdateDto dto) {
    Post post = new Post(dto.getId(), dto.getAuthor(), dto.getTextContent());
    return post;
  }

}

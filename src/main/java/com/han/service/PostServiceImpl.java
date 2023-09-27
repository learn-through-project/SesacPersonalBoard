package com.han.service;

import com.han.constants.OrderType;
import com.han.constants.SortType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PostServiceImpl implements PostService {


  private final PostImageService postImageService;
  private final PostRepository postRepository;


  @Autowired
  public PostServiceImpl(PostRepository postRepository, PostImageService postImageService) {
    this.postRepository = postRepository;
    this.postImageService = postImageService;

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


  @Transactional(rollbackFor = {SQLException.class, IOException.class})
  @Override
  public boolean createPost(PostCreateDto dto, List<MultipartFile> files) throws SQLException, IOException {
    Post post = fromCreateDtoToPost(dto);

    Integer postId = postRepository.insert(post);
    boolean isSuccess = postImageService.createPostImage(postId, files);

    return isSuccess;
  }

  @Override
  public List<Post> getPostList(PostListDto dto) throws SQLException {
    int page = dto.getPage();
    int limit = dto.getLimit();
    OrderType order = dto.getOrder();
    TableColumnsPost sort = dto.getSort().equals(SortType.NEW)
            ? TableColumnsPost.CREATED_AT
            : TableColumnsPost.ID;

    int offset = (page - 1) * limit;
    List<Post> list = postRepository.findAll(order, sort, limit, offset);

    return list;
  }

  @Override
  public Optional<Post> getPostDetail(int postId) throws SQLException {

    Optional<Post> post = postRepository.findById(postId);

    return post;
  }

  private Post fromCreateDtoToPost(PostCreateDto dto) {
    Post post = new Post(dto.getUserId(), dto.getTextContent());

    return post;
  }

  private Post fromUpdateDtoToPost(PostUpdateDto dto) {
    Post post = new Post(dto.getId(), dto.getAuthor(), dto.getTextContent());
    return post;
  }

}

package com.han.service;

import com.han.constants.OrderType;
import com.han.constants.SortType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.dto.PostCreateDto;
import com.han.dto.PostDetailDto;
import com.han.dto.PostEditDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.model.PostImage;
import com.han.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public int getPostListTotalCount() throws SQLException {
    int count = postRepository.getPostTotalCount();
    return count;
  }

  @Override
  public boolean deletePermanently(int postId) throws SQLException {
    boolean result = postRepository.deletePermanently(postId);
    return result;
  }

  @Override
  public boolean editPost(PostEditDto dto) throws Exception {

    postImageService.editPostImage(dto.getId(), dto.getImages(), dto.getImgFlag());

    Post post = fromUpdateDtoToPost(dto);
    boolean result = postRepository.update(post);
    return result;
  }

  @Override
  public boolean createPost(PostCreateDto dto) throws Exception {
    boolean isSuccess = false;

    Post post = fromCreateDtoToPost(dto);
    Integer postId = postRepository.insert(post);

    if (postId != null) {
      try {
        //Q: createPostImage는 성공이면 true, 실패면 throw인데 boolean반환하는게 의미가 있는지, 차라리 void가 나은지,
        isSuccess = postImageService.createPostImage(postId, dto.getImages());
      } catch (Exception ex) {
        postRepository.deletePermanently(postId);
        throw ex;
      }
    }

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
  public Optional<PostDetailDto> getPostDetail(int postId) throws SQLException {
    PostDetailDto result = null;
    Optional<Post> post = postRepository.findById(postId);

    if (post.isPresent()) {
      List<PostImage> images = postImageService.getAllImagesByPostId(postId);
      result = toPostDetailDto(post.get(), images);
    }

    return Optional.ofNullable(result);
  }

  private Post fromCreateDtoToPost(PostCreateDto dto) {
    Post post = new Post(dto.getUserId(), dto.getTitle(), dto.getTextContent());

    return post;
  }

  private Post fromUpdateDtoToPost(PostEditDto dto) {
    Post post = new Post(dto.getId(), dto.getUserId(), dto.getTitle(), dto.getTextContent());
    return post;
  }

  private PostDetailDto toPostDetailDto(Post post, List<PostImage> images) {
    PostDetailDto dto = new PostDetailDto();
    dto.setId(post.getId());
    dto.setUserId(post.getUserId());
    dto.setTitle(post.getTitle());
    dto.setTextContent(post.getTextContent());
    dto.setImages(images);
    dto.setUpdatedAt(post.getUpdatedAt());

    return dto;
  }

}

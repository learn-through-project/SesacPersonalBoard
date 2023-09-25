package unit.com.han.service;

import com.han.constants.TableColumnsPost;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.repository.PostRepository;
import com.han.service.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostServiceImpl postService;

  @Nested
  class EditPost_Test {

    boolean success = true;

    boolean fail = false;
    private PostUpdateDto dummyDto = new PostUpdateDto(1, 1, "this is update");
    private Post dummyPost = new Post(dummyDto.getId(), dummyDto.getAuthor(), dummyDto.getTextContent());

    @Test
    public void editPost_Throw_Exception() throws SQLException {
      when(postRepository.update(dummyPost)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.editPost(dummyDto));

      verify(postRepository).update(dummyPost);
    }
    @Test
    public void editPost_Return_False() throws SQLException {
      when(postRepository.update(dummyPost)).thenReturn(fail);

      boolean result = postService.editPost(dummyDto);

      verify(postRepository).update(dummyPost);
      assertThat(result).isFalse();
    }
    @Test
    public void editPost_Return_True() throws SQLException {
      when(postRepository.update(dummyPost)).thenReturn(success);

      boolean result = postService.editPost(dummyDto);

      verify(postRepository).update(dummyPost);
      assertThat(result).isTrue();
    }

  }
  @Nested
  class CreatePost_Test {

    private PostCreateDto dummyDto = new PostCreateDto(1, "this is sample");
    private Post dummyPost = new Post(1, "this is sample");

    @Test
    public void createPost_Throw_Exception() throws SQLException {
      when(postRepository.insert(dummyPost)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.createPost(dummyDto));

      verify(postRepository).insert(dummyPost);
    }
    @Test
    public void createPost_Return_False() throws SQLException {
      when(postRepository.insert(dummyPost)).thenReturn(false);
      boolean isSuccess = postService.createPost(dummyDto);

      verify(postRepository).insert(dummyPost);
      assertThat(isSuccess).isFalse();
    }
    @Test
    public void createPost_Return_True() throws SQLException {
      when(postRepository.insert(dummyPost)).thenReturn(true);
      boolean isSuccess = postService.createPost(dummyDto);

      verify(postRepository).insert(dummyPost);
      assertThat(isSuccess).isTrue();
    }
  }

  @Nested
  class GetPostDetail_Test {
    private int validPostId = 1;

    private Post dummyPost = new Post(1);

    @Test
    public void getPostDetail_Throws_Exception() throws SQLException {
      when(postRepository.findById(validPostId)).thenThrow(SQLException.class);

      assertThrows(SQLException.class, () -> postService.getPostDetail(validPostId));
      verify(postRepository).findById(validPostId);
    }

    @Test
    public void getPostDetail_Return_Empty() throws SQLException {
      int nonExistingId = validPostId + 1000;
      when(postRepository.findById(nonExistingId)).thenReturn(Optional.empty());

      Optional<Post> post = postService.getPostDetail(nonExistingId);

      verify(postRepository).findById(nonExistingId);
      assertThat(post).isNotNull();
      assertThat(post.isPresent()).isFalse();
    }

    @Test
    public void getPostDetail_Return_Post() throws SQLException {
      when(postRepository.findById(validPostId)).thenReturn(Optional.of(dummyPost));

      Optional<Post> post = postService.getPostDetail(validPostId);

      verify(postRepository).findById(validPostId);
      assertThat(post).isNotNull();
      assertThat(post.isPresent()).isTrue();
      assertThat(post.get().getId()).isEqualTo(validPostId);
    }
  }

  @Nested
  class GetPostList_Test {

    private Post validPost = new Post(1);

    private PostListReqDto dto = new PostListReqDto(TableColumnsPost.ID.getName(), 10, 1);

    private int page;
    private int limit;
    private int offset;
    private String sort;


    @BeforeEach
    public void setUp() {
      offset = (dto.getPage() - 1) * dto.getLimit();
      page = dto.getPage();
      limit = dto.getLimit();
      sort = dto.getSort();
    }

    @Test
    public void getPostList_Throw_Exception() throws SQLException {
      when(postRepository.findAll(sort, limit, offset)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.getPostList(dto));
    }

    @Test
    public void getPostList_Return_Post_List() throws SQLException {

      List<Post> mockList = IntStream
              .range(0, limit)
              .mapToObj((i) -> validPost)
              .collect(Collectors.toList());

      when(postRepository.findAll(sort, limit, offset)).thenReturn(mockList);

      List<Post> list = postService.getPostList(dto);

      verify(postRepository).findAll(sort, limit, offset);
      assertThat(list.size()).isEqualTo(mockList.size());
    }
  }

}
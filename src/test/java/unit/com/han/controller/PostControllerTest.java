package unit.com.han.controller;

import com.han.controller.PostControllerImpl;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
public class PostControllerTest {
  @Mock
  private PostService postService;

  @InjectMocks
  private PostControllerImpl postController;


  @Nested
  class EditPost_Test {
    boolean success = true;

    boolean fail = false;
    private PostUpdateDto dummyDto = new PostUpdateDto(1, 1, "sample");

    @Test
    public void editPost_Return_True() throws SQLException {
      when(postService.editPost(dummyDto)).thenReturn(success);

      ResponseEntity<Boolean> result = postController.editPost(dummyDto);

      assertThat(result.getBody()).isEqualTo(success);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class GetPostDetail_Test {
    private int validPostId = 1;
    private int invalidPostId = 0;
    private Post dummyPost = new Post(1);

    @Test
    public void getPostDetail_Throw_Exception() throws SQLException {
      when(postService.getPostDetail(invalidPostId)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.getPostDetail(invalidPostId));

      verify(postService).getPostDetail(invalidPostId);
    }

    @Test
    public void getPostDetail_Return_Empty() throws SQLException {
      int nonExistingId = 0;
      when(postService.getPostDetail(nonExistingId)).thenReturn(Optional.empty());

      Optional<Post> detail = postService.getPostDetail(nonExistingId);

      verify(postService).getPostDetail(nonExistingId);
      assertThat(detail).isNotNull();
      assertThat(detail.isPresent()).isFalse();
    }

    @Test
    public void getPostDetail_Return_Post() throws SQLException {
      when(postService.getPostDetail(validPostId)).thenReturn(Optional.of(dummyPost));

      Optional<Post> detail = postService.getPostDetail(validPostId);

      assertThat(detail).isNotNull();
      assertThat(detail.isPresent()).isTrue();
      assertThat(detail.get().getId()).isEqualTo(validPostId);
    }
  }

  @Nested
  class createPost_Test {

    boolean success = true;

    boolean fail = false;
    private PostCreateDto dto = new PostCreateDto(1, "this is sample");

    @Test
    public void createPost_Throws_SQLException() throws SQLException {
      when(postService.createPost(dto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postController.createPost(dto));

      verify(postService).createPost(dto);
    }

    @Test
    public void createPost_Return_False() throws SQLException {
      when(postService.createPost(dto)).thenReturn(fail);

      ResponseEntity result = postController.createPost(dto);

      verify(postService).createPost(dto);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(fail);
    }

    @Test
    public void createPost_Return_True() throws SQLException {

      when(postService.createPost(dto)).thenReturn(success);

      ResponseEntity result = postController.createPost(dto);

      verify(postService).createPost(dto);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(success);
    }
  }

  @Nested
  class GetPostList_Test {
    private PostListReqDto reqDto = new PostListReqDto("id", 10, 1);

    private List<Post> dummyList;

    @BeforeEach
    public void setUp() {
      dummyList = IntStream
              .range(0, 10)
              .mapToObj((i) -> new Post(1))
              .collect(Collectors.toList());
    }

    @Test
    public void getPostList_Throw_Exception() throws SQLException {
      when(postService.getPostList(reqDto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postController.getPostList(reqDto));
    }

    @Test
    public void getPostList_Return_List() throws SQLException {
      when(postService.getPostList(reqDto)).thenReturn(dummyList);
      ResponseEntity<List<Post>> res = postController.getPostList(reqDto);

      verify(postService).getPostList(reqDto);
      assertThat(res.hasBody()).isTrue();
      assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class ExceptionHandler_Test {
    @Test
    public void handelSqlException_Return_ResponseEntity() {
      String errorMsg = "error test";
      ResponseEntity<String> res = postController.handleSqlException(new SQLException(errorMsg));
      assertThat(res.getBody()).contains(errorMsg);
      assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handelIllegalArgumentException_Return_ResponseEntity() {
      String errorMsg = "error test";
      ResponseEntity<String> res = postController.handleIllegalArgumentException(new IllegalArgumentException(errorMsg));
      assertThat(res.getBody()).contains(errorMsg);
      assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
  }


}

package unit.com.han.controller;

import com.han.constants.tablesColumns.TableColumnsPost;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
  class DeletePermanently_Test {

    boolean success = true;
    boolean fail = false;
    int postId = 1;

    @Test
    public void deletePermanently_Throw_Exception() throws SQLException {
      when(postService.deletePermanently(postId)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postController.deletePermanentlyPost(postId));

      verify(postService).deletePermanently(postId);
    }
    @Test
    public void deletePermanently_Return_False() throws SQLException {
      when(postService.deletePermanently(postId)).thenReturn(fail);
      ResponseEntity<Boolean> result = postController.deletePermanentlyPost(postId);

      verify(postService).deletePermanently(postId);
      assertThat(result.getBody()).isEqualTo(fail);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deletePermanently_Return_True() throws SQLException {
      when(postService.deletePermanently(postId)).thenReturn(success);
      ResponseEntity<Boolean> result = postController.deletePermanentlyPost(postId);

      verify(postService).deletePermanently(postId);
      assertThat(result.getBody()).isEqualTo(success);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class EditPost_Test {
    boolean success = true;

    boolean fail = false;
    private PostUpdateDto dummyDto = new PostUpdateDto(1, 1, "sample");

    @Test
    public void editPost_Throw_Exception() throws SQLException {
      when(postService.editPost(dummyDto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postController.editPost(dummyDto));

      verify(postService).editPost(dummyDto);
    }
    @Test
    public void editPost_Return_False() throws SQLException {
      when(postService.editPost(dummyDto)).thenReturn(fail);

      ResponseEntity<Boolean> result = postController.editPost(dummyDto);

      assertThat(result.getBody()).isEqualTo(fail);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

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
      assertThrows(SQLException.class, () -> postController.getPostDetail(invalidPostId));

      verify(postService).getPostDetail(invalidPostId);
    }

    @Test
    public void getPostDetail_Return_Empty() throws SQLException {
      int nonExistingId = 0;
      when(postService.getPostDetail(nonExistingId)).thenReturn(Optional.empty());

      ResponseEntity<Post> detail = postController.getPostDetail(nonExistingId);

      verify(postService).getPostDetail(nonExistingId);
      assertThat(detail.getBody()).isNull();
      assertThat(detail.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getPostDetail_Return_Post() throws SQLException {
      when(postService.getPostDetail(validPostId)).thenReturn(Optional.of(dummyPost));

      ResponseEntity<Post> detail = postController.getPostDetail(validPostId);

      verify(postService).getPostDetail(validPostId);

      assertThat(detail.getBody()).isEqualTo(dummyPost);
      assertThat(detail.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class createPost_Test {
    @Mock
    private MultipartFile dummyFile;

    boolean success = true;
    boolean fail = false;
    private PostCreateDto dto = new PostCreateDto(1, "this is sample");

    private List<MultipartFile> dummyFiles;

    @BeforeEach
    public void setUp() {
      this.dummyFiles = List.of(dummyFile, dummyFile);
    }


    @Test
    public void createPost_Throws_SQLException() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postController.createPost(dto, dummyFiles));

      verify(postService).createPost(dto, dummyFiles);
    }

    @Test
    public void createPost_Return_False() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenReturn(fail);

      ResponseEntity<Boolean> result = postController.createPost(dto, dummyFiles);

      verify(postService).createPost(dto, dummyFiles);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(fail);
    }

    @Test
    public void createPost_Return_True() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenReturn(success);

      ResponseEntity<Boolean> result = postController.createPost(dto, dummyFiles);

      verify(postService).createPost(dto, dummyFiles);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(success);
    }
  }

  @Nested
  class GetPostList_Test {
    private PostListReqDto reqDto = new PostListReqDto(10, 1);

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

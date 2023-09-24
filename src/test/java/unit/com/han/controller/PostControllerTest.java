package unit.com.han.controller;

import com.han.controller.PostControllerImpl;
import com.han.dto.PostListReqDto;
import com.han.model.Post;
import com.han.service.PostService;
import org.assertj.core.api.Assertions;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
  class ExceptionHandler_Test {
    @Test
    public void handelSqlException_Return_ResponseEntity() {
      String errorMsg = "error test";
      ResponseEntity<String> res = postController.handelSqlException(new SQLException(errorMsg));
      Assertions.assertThat(res.getBody()).contains(errorMsg);
      Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handelIllegalArgumentException_Return_ResponseEntity() {
      String errorMsg = "error test";
      ResponseEntity<String> res = postController.handelIllegalArgumentException(new IllegalArgumentException(errorMsg));
      Assertions.assertThat(res.getBody()).contains(errorMsg);
      Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
      Assertions.assertThat(res.hasBody()).isTrue();
      Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }


}

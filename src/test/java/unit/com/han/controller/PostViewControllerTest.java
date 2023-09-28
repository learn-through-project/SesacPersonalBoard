package unit.com.han.controller;

import com.han.controller.PostViewControllerImpl;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.service.PostService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostViewControllerTest {

  @Mock
  private PostService postService;

  @Mock
  private BindingResult bindingResult;

  @InjectMocks
  private PostViewControllerImpl postViewController;

  @Nested
  class GetPostList_Test {

    PostListDto dto = PostListDto.getDefaultInstance();

    int count = 100;

    Post postDummy = new Post(1);

    List<Post> dummyList = List.of(postDummy, postDummy);

    @Test
    public void getPostList_Throws_Exception_When_TotalCount_Throws() throws SQLException {
      when(postService.getPostList(dto)).thenReturn(dummyList);
      when(postService.getPostListTotalCount()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postViewController.getPostList(dto, bindingResult));

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
    }

    @Test
    public void getPostList_Throws_Exception_When_PostList_Throws() throws SQLException {
      when(postService.getPostList(dto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postViewController.getPostList(dto, bindingResult));

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
    }

    @Test
    public void getPostList_Return_ResDto() throws SQLException {
      when(postService.getPostList(dto)).thenReturn(dummyList);
      when(postService.getPostListTotalCount()).thenReturn(count);
      when(bindingResult.hasErrors()).thenReturn(false);

      ModelAndView mv = postViewController.getPostList(dto, bindingResult);

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
      verify(postService).getPostListTotalCount();
      assertThat(mv.getModel().get("list")).isEqualTo(dummyList);
      assertThat(mv.getModel().get("count")).isEqualTo(count);
    }
  }
}

package unit.com.han.controller;

import com.han.constants.ViewName;
import com.han.controller.PostViewControllerImpl;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

  @Mock
  private Model model;

  @Mock
  private MultipartFile dummyFile;

  @InjectMocks
  private PostViewControllerImpl postViewController;

  @Nested
  class CreatePost_Test {
    private PostCreateDto dto = new PostCreateDto(1, "sample post");

    private List<MultipartFile> dummyFiles;

    @BeforeEach
    public void setUp() {
      dummyFiles = List.of(dummyFile, dummyFile);
    }

    @Test
    public void createPost_Throws_Exception_When_Service_Throws() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postViewController.createPost(dto, dummyFiles, bindingResult, model));
    }

    @Test
    public void createPost_Return_FromView_When_Input_Invalid() throws SQLException, IOException {
      when(bindingResult.hasErrors()).thenReturn(true);
      String viewName = postViewController.createPost(dto, dummyFiles, bindingResult, model);

      assertThat(viewName).isEqualTo(ViewName.POST_NEW_VIEW);
    }

    @Test
    public void createPost_Return_ResultView_With_IsSuccess_False() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenReturn(false);
      String viewName = postViewController.createPost(dto, dummyFiles, bindingResult, model);

      verify(model).addAttribute("isSuccess", false);
      assertThat(viewName).isEqualTo(ViewName.POST_NEW_RESULT);
    }
    @Test
    public void createPost_Return_ResultView_With_IsSuccess_True() throws SQLException, IOException {
      when(postService.createPost(dto, dummyFiles)).thenReturn(true);
      String viewName = postViewController.createPost(dto, dummyFiles, bindingResult, model);

      verify(model).addAttribute("isSuccess", true);
      assertThat(viewName).isEqualTo(ViewName.POST_NEW_RESULT);
    }
  }

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
      assertThrows(SQLException.class, () -> postViewController.getPostList(dto, bindingResult, model));

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
    }

    @Test
    public void getPostList_Throws_Exception_When_PostList_Throws() throws SQLException {
      when(postService.getPostList(dto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postViewController.getPostList(dto, bindingResult, model));

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
    }

    @Test
    public void getPostList_Return_ResDto() throws SQLException {
      when(postService.getPostList(dto)).thenReturn(dummyList);
      when(postService.getPostListTotalCount()).thenReturn(count);
      when(bindingResult.hasErrors()).thenReturn(false);

      String viewName = postViewController.getPostList(dto, bindingResult, model);

      verify(bindingResult).hasErrors();
      verify(postService).getPostList(dto);
      verify(postService).getPostListTotalCount();
      verify(model).addAttribute("list", dummyList);
      verify(model).addAttribute("count", count);
      assertThat(viewName).isEqualTo(ViewName.POST_LIST);
    }
  }
}

package unit.com.han.controller;

import com.han.constants.EndPoint;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  private RedirectAttributes redirectAttributes;
  @Mock
  private Model model;

  @InjectMocks
  private PostViewControllerImpl postViewController;

  @Nested
  class ShowCreatePostForm_Test {
    private PostCreateDto dto = new PostCreateDto();
    @Test
    public void showCreatePostForm_Return_View() {
      String viewName = postViewController.showCreatePostForm(dto, bindingResult);
      assertThat(viewName).isEqualTo(ViewName.POST_NEW_VIEW);
    }
  }

  @Nested
  class CreatePost_Test {

    private PostCreateDto dto = new PostCreateDto();

    @Test
    public void createPost_Throws_Exception_When_Service_Throws() throws SQLException, IOException {
      when(postService.createPost(dto)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postViewController.createPost(dto, bindingResult, redirectAttributes));
    }

    @Test
    public void createPost_Return_Form_EndPoint_When_Input_Invalid() throws SQLException, IOException {
      when(bindingResult.hasErrors()).thenReturn(true);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
    }

    @Test
    public void createPost_Return_FormEndPoint_With_IsSuccess_0() throws SQLException, IOException {
      when(postService.createPost(dto)).thenReturn(false);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      verify(redirectAttributes).addFlashAttribute("isSuccess", 0);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
    }
    @Test
    public void createPost_Return_FormEndPoint_With_IsSuccess_1() throws SQLException, IOException {
      when(postService.createPost(dto)).thenReturn(true);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      verify(redirectAttributes).addFlashAttribute("isSuccess", 1);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
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

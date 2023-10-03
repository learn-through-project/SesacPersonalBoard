package unit.com.han.controller;

import com.han.constants.EndPoint;
import com.han.constants.ViewName;
import com.han.controller.PostViewControllerImpl;
import com.han.dto.PostCreateDto;
import com.han.dto.PostDetailDto;
import com.han.dto.PostEditDto;
import com.han.dto.PostListDto.PostListDto;
import com.han.model.Post;
import com.han.service.PostService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
  class EditPost_Test {

    private PostEditDto dummyDto = new PostEditDto();
    @Test
    public void editPost_Return_View_Success() throws Exception {
      String viewName = postViewController.editPost(dummyDto, bindingResult, redirectAttributes);
      assertThat(viewName).isEqualTo("hi");
    }


  }

  @Nested
  class ShowEditPostForm_Test {

    private Integer postId = 1;
    private PostDetailDto dummyDetailDto = new PostDetailDto();
    @Test
    public void showPostEditForm_Return_ViewName_With_Null_When_Service_Throws() throws SQLException {
      when(postService.getPostDetail(postId)).thenThrow(SQLException.class);

      String view = postViewController.showPostEditForm(postId, model);

      verify(model).addAttribute("error", "데이터를 가져오는 중 문제가 발생하였습니다.");
      verify(model).addAttribute("post", null);
      assertThat(view).isEqualTo(ViewName.POST_EDIT_VIEW);
    }
    @Test
    public void showPostEditForm_Return_ViewName_With_Null() throws SQLException {
      when(postService.getPostDetail(postId)).thenReturn(Optional.empty());

      String view = postViewController.showPostEditForm(postId, model);

      verify(postService).getPostDetail(postId);
      verify(model).addAttribute("post", null);
      assertThat(view).isEqualTo(ViewName.POST_EDIT_VIEW);
    }

    @Test
    public void showPostEditForm_Return_ViewName_With_Detail() throws SQLException {
      when(postService.getPostDetail(postId)).thenReturn(Optional.of(dummyDetailDto));

      String view = postViewController.showPostEditForm(postId, model);

      verify(postService).getPostDetail(postId);
      verify(model).addAttribute("post", dummyDetailDto);
      assertThat(view).isEqualTo(ViewName.POST_EDIT_VIEW);
    }
  }

  @Nested
  class ShowPostDetail_Test {
    private Integer postId = 1;

    private PostDetailDto dummyDetailDto = new PostDetailDto();

    @Test
    public void showPostDetail_Return_ViewName_With_Null_When_Service_Throws() throws SQLException {
      when(postService.getPostDetail(postId)).thenThrow(SQLException.class);

      String view = postViewController.showPostDetail(postId, model);

      verify(model).addAttribute("error", "데이터를 가져오는 중 문제가 발생하였습니다.");
      verify(model).addAttribute("detail", null);
      assertThat(view).isEqualTo(ViewName.POST_DETAIL);
    }
    @Test
    public void showPostDetail_Return_ViewName_With_Null() throws SQLException {
      when(postService.getPostDetail(postId)).thenReturn(Optional.empty());

      String view = postViewController.showPostDetail(postId, model);

      verify(postService).getPostDetail(postId);
      verify(model).addAttribute("detail", null);
      assertThat(view).isEqualTo(ViewName.POST_DETAIL);
    }

    @Test
    public void showPostDetail_Return_ViewName_With_Detail() throws SQLException {
      when(postService.getPostDetail(postId)).thenReturn(Optional.of(dummyDetailDto));

      String view = postViewController.showPostDetail(postId, model);

      verify(postService).getPostDetail(postId);
      verify(model).addAttribute("detail", dummyDetailDto);
      assertThat(view).isEqualTo(ViewName.POST_DETAIL);
    }
  }
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
    public void createPost_Return_Form_EndPoint_When_Service_Throws() throws Exception {
      when(postService.createPost(dto)).thenThrow(Exception.class);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("isSuccess", 0);
      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
    }
    @Test
    public void createPost_Return_Form_EndPoint_When_Input_Invalid() throws Exception {
      when(bindingResult.hasErrors()).thenReturn(true);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
    }

    @Test
    public void createPost_Return_FormEndPoint_With_IsSuccess_0() throws Exception {
      when(postService.createPost(dto)).thenReturn(false);
      String endPoint = postViewController.createPost(dto, bindingResult, redirectAttributes);

      verify(redirectAttributes).addFlashAttribute("post", dto);
      verify(redirectAttributes).addFlashAttribute("isRedirected", 1);
      verify(redirectAttributes).addFlashAttribute("isSuccess", 0);
      assertThat(endPoint).isEqualTo("redirect:" + EndPoint.POST_NEW);
    }

    @Test
    public void createPost_Return_FormEndPoint_With_IsSuccess_1() throws Exception {
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

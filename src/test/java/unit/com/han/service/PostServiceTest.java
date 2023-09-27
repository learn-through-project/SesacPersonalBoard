package unit.com.han.service;

import com.han.constants.OrderType;
import com.han.constants.SortType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.dto.PostCreateDto;
import com.han.dto.PostListReqDto;
import com.han.dto.PostUpdateDto;
import com.han.model.Post;
import com.han.repository.PostRepository;
import com.han.service.PostImageService;
import com.han.service.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class PostServiceTest {

  @Mock
  private PostImageService postImageService;
  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostServiceImpl postService;

  @Nested
  class DeletePermanently_Test {

    private boolean success = true;

    private boolean fail = false;
    private int postId = 1;

    @Test
    public void DeletePermanently_Throws_Exception() throws SQLException {
      when(postRepository.deletePermanently(postId)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.deletePermanently(postId));

      verify(postRepository).deletePermanently(postId);
    }

    @Test
    public void DeletePermanently_Return_False() throws SQLException {
      when(postRepository.deletePermanently(postId)).thenReturn(fail);

      boolean isSuccess = postService.deletePermanently(postId);

      verify(postRepository).deletePermanently(postId);
      assertThat(isSuccess).isFalse();
    }

    @Test
    public void DeletePermanently_Return_True() throws SQLException {
      when(postRepository.deletePermanently(postId)).thenReturn(success);

      boolean isSuccess = postService.deletePermanently(postId);

      verify(postRepository).deletePermanently(postId);
      assertThat(isSuccess).isTrue();
    }
  }

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
    @Mock
    private MultipartFile dummyFile;

    private int postId = 1;
    private PostCreateDto dummyDto = new PostCreateDto(1, "this is sample");
    private Post dummyPost = new Post(1, "this is sample");
    private List<MultipartFile> dummyFiles;

    private String testUrl = "url";

    @BeforeEach
    public void setUp() {
      this.dummyFiles = List.of(dummyFile, dummyFile);
    }

    @Test
    public void createPost_Throw_Exception() throws SQLException {
      when(postRepository.insert(dummyPost)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.createPost(dummyDto, dummyFiles));

      verify(postRepository).insert(dummyPost);
    }

    @Test
    public void createPost_Throws_NullPointException_When_Insert_Fail() throws SQLException, IOException {
      Integer nullPostId = null;
      when(postRepository.insert(dummyPost)).thenReturn(null);
      assertThrows(NullPointerException.class, () -> postImageService.createPostImage(nullPostId, dummyFiles));
      assertThrows(NullPointerException.class, () -> postService.createPost(dummyDto, dummyFiles));

      verify(postRepository).insert(dummyPost);
    }

    @Test
    public void createPost_Return_True() throws SQLException, IOException {
      when(postRepository.insert(dummyPost)).thenReturn(postId);
      when(postImageService.createPostImage(postId, dummyFiles)).thenReturn(true);

      boolean isSuccess = postService.createPost(dummyDto, dummyFiles);


      verify(postRepository).insert(dummyPost);
      verify(postImageService).createPostImage(postId, dummyFiles);

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
    private PostListReqDto dto = new PostListReqDto(10, 1);
    private int limit;
    private int offset;
    private TableColumnsPost sort;

    private OrderType orderAsc = OrderType.ASC;
    private OrderType orderDesc = OrderType.DESC;

    @BeforeEach
    public void setUp() {
      offset = (dto.getPage() - 1) * dto.getLimit();
      limit = dto.getLimit();
      sort = dto.getSort().equals(SortType.NEW)
              ? TableColumnsPost.CREATED_AT
              : TableColumnsPost.ID;
    }

    @Test
    public void getPostList_Throw_Exception() throws SQLException {
      when(postRepository.findAll(orderAsc, sort, limit, offset)).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postService.getPostList(dto));
    }

    @Test
    public void getPostList_Return_Post_List_With_Desc_Order() throws SQLException {

      List<Post> mockList = IntStream
              .range(0, limit)
              .mapToObj((i) -> new Post(i))
              .sorted((p1, p2) -> p2.getId().compareTo(p1.getId()))
              .collect(Collectors.toList());

      when(postRepository.findAll(orderDesc, sort, limit, offset)).thenReturn(mockList);

      dto.setOrder(orderDesc);
      List<Post> list = postService.getPostList(dto);

      verify(postRepository).findAll(orderDesc, sort, limit, offset);
      assertThat(list.size()).isEqualTo(mockList.size());
      assertThat(list.get(0).getId()).isGreaterThan(list.get(1).getId());
    }

    @Test
    public void getPostList_Return_Post_List_With_Asc_Order() throws SQLException {

      List<Post> mockList = IntStream
              .range(0, limit)
              .mapToObj((i) -> new Post(i))
              .collect(Collectors.toList());

      when(postRepository.findAll(orderAsc, sort, limit, offset)).thenReturn(mockList);

      List<Post> list = postService.getPostList(dto);

      verify(postRepository).findAll(orderAsc, sort, limit, offset);
      assertThat(list.size()).isEqualTo(mockList.size());
      assertThat(list.get(1).getId()).isGreaterThan(list.get(0).getId());
    }
  }

}

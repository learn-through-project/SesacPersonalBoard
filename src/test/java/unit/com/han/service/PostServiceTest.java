package unit.com.han.service;

import com.han.model.Post;
import com.han.repository.PostRepository;
import com.han.service.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 class GetPostList_Test {
   private int page = 1;
   private int limit = 10;

   private int offset = (page - 1) * limit;
   private String sort = Post.Columns.ID;

   private Post validPost = new Post(1);

   @Test
   public void getPostList_Throw_Exception() throws SQLException {
     when(postRepository.findAll(sort, limit, offset)).thenThrow(SQLException.class);
     assertThrows(SQLException.class, () -> postService.getPostList(sort, limit, page));
   }

   @Test
   public void getPostList_Return_Post_List() throws SQLException {

     List<Post> mockList = IntStream
             .range(0, limit)
             .mapToObj((i) -> validPost)
             .collect(Collectors.toList());

     when(postRepository.findAll(sort, limit, offset)).thenReturn(mockList);

     List<Post> list = postService.getPostList(sort, limit, page);

     verify(postRepository).findAll(sort, limit, offset);
     Assertions.assertThat(list.size()).isEqualTo(mockList.size());
   }
 }

}

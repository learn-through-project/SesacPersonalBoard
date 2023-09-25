package unit.com.han.repository;

import com.han.constants.OrderType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.model.Post;
import com.han.repository.PostRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostRepositoryTest {

  @Mock
  private DataSource dataSource;

  @Mock
  private Connection connection;

  @Mock
  private PreparedStatement statement;

  @Mock
  private ResultSet resultSet;
  @InjectMocks
  private PostRepositoryImpl postRepository;

  private final List<String> skipSetUpMethods = List.of(
          "findAll_Throw_IllegalArgumentException"
  );

  @BeforeEach
  public void setUp(TestInfo info) throws SQLException {
    if (skipSetUpMethods.contains(info.getTestMethod().get().getName())) return;

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  @Nested
  class DeletePermanently_Test {
    int success = 1;
    int fail = 0;
    int postId = 1;

    @Test
    public void deletePermanently_Throw_Exception() throws SQLException {
      when(statement.executeUpdate()).thenThrow(SQLException.class);

      assertThrows(SQLException.class, ()-> postRepository.deletePermanently(postId));
      verify(statement).setInt(1, postId);
    }
    @Test
    public void deletePermanently_Return_False() throws SQLException {
      when(statement.executeUpdate()).thenReturn(fail);

      boolean isSuccess = postRepository.deletePermanently(postId);

      verify(statement).setInt(1, postId);
      assertThat(isSuccess).isFalse();
    }

    @Test
    public void deletePermanently_Return_True() throws SQLException {
      when(statement.executeUpdate()).thenReturn(success);

      boolean isSuccess = postRepository.deletePermanently(postId);

      verify(statement).setInt(1, postId);
      assertThat(isSuccess).isTrue();
    }
  }

  @Nested
  class Update_Test {
    int success = 1;
    int fail = 0;
    private Post dummyPost = new Post( 1,1, "this is sample00");

    @Test
    public void update_Throws_Exception() throws SQLException {
      when(statement.executeUpdate()).thenThrow(SQLException.class);

      assertThrows(SQLException.class, () -> postRepository.update(dummyPost));

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).setInt(3, dummyPost.getId());
    }

    @Test
    public void update_Return_False() throws SQLException {
      when(statement.executeUpdate()).thenReturn(fail);

      boolean result = postRepository.update(dummyPost);

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).setInt(3, dummyPost.getId());

      assertThat(result).isFalse();
    }
    @Test
    public void update_Return_True() throws SQLException {
      when(statement.executeUpdate()).thenReturn(success);

      boolean result = postRepository.update(dummyPost);

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).setInt(3, dummyPost.getId());

      assertThat(result).isTrue();
    }


  }

  @Nested
  class Insert_Test {

    int success = 1;
    int fail = 0;
    private Post dummyPost = new Post( 1, "this is sample");

    @Test
    public void insert_Throw_SQLException() throws SQLException {
      when(statement.executeUpdate()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postRepository.insert(dummyPost));

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).executeUpdate();

    }
    @Test
    public void insert_Return_False() throws SQLException {
      when(statement.executeUpdate()).thenReturn(fail);

      boolean result = postRepository.insert(dummyPost);

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).executeUpdate();

      assertThat(result).isFalse();
    }
    @Test
    public void insert_Return_True() throws SQLException {
      when(statement.executeUpdate()).thenReturn(success);

      boolean result = postRepository.insert(dummyPost);

      verify(statement).setInt(1, dummyPost.getUserId());
      verify(statement).setString(2, dummyPost.getTextContent());
      verify(statement).executeUpdate();

      assertThat(result).isTrue();
    }

  }

  @Nested
  class FindAll_Test {

    private int limit = 20;
    private int offset = 20;
    private String orderBy = TableColumnsPost.ID.getName();

    private OrderType orderAsc = OrderType.ASC;
    private OrderType orderDesc = OrderType.DESC;


    @Test
    public void findAll_Throw_IllegalArgumentException() throws IllegalArgumentException {
      String invalidColumnName = "hi";
      Integer invalidLimit = limit;
      Integer invalidOffset = offset;

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        postRepository.findAll(orderAsc, invalidColumnName, invalidLimit, invalidOffset);
      });
      assertEquals("Invalid parameters: Please check parameter", exception.getMessage());

    }

    @Test
    public void findAll_Throw_SQLException_With_Invalid_Query() throws SQLException {
      when(statement.executeQuery()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postRepository.findAll(orderAsc, orderBy, limit, offset));
    }

    @Test
    public void findAll_Return_Empty_List() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      List<Post> post = postRepository.findAll(orderAsc, orderBy, limit, offset);

      verify(statement).setInt(1, limit);
      verify(statement).setInt(2, offset);

      assertThat(post.size()).isEqualTo(0);
    }

    @Test
    public void findAll_Return_Post_List_With_Desc_Order() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPost.ID.getName())).thenReturn(2).thenReturn(1);

      List<Post> post = postRepository.findAll(orderDesc, orderBy, limit, offset);

      verify(statement).setInt(1, limit);
      verify(statement).setInt(2, offset);

      assertThat(post.size()).isEqualTo(2);
      assertThat(post.get(0).getId()).isGreaterThan(post.get(1).getId());
    }

    @Test
    public void findAll_Return_Post_List_With_Asc_Order() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPost.ID.getName())).thenReturn(1).thenReturn(2);

      List<Post> post = postRepository.findAll(orderAsc, orderBy, limit, offset);

      verify(statement).setInt(1, limit);
      verify(statement).setInt(2, offset);

      assertThat(post.size()).isEqualTo(2);
      assertThat(post.get(1).getId()).isGreaterThan(post.get(0).getId());
    }
  }

  @Nested
  class FindById_Test {
    private int nonExistingPostId = -1;
    private int existingPostId = 1;
    @Test
    public void findById_Throw_SQLException_With_Invalid_Query() throws SQLException {
      when(statement.executeQuery()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postRepository.findById(nonExistingPostId));
    }

    @Test
    public void findById_Return_Null() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      Optional<Post> post = postRepository.findById(nonExistingPostId);

      verify(statement).setInt(1, nonExistingPostId);
      assertThat(post).isNotNull();
      assertThat(post.isPresent()).isFalse();
    }


    @Test
    public void findById_Return_Post() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPost.ID.getName())).thenReturn(existingPostId);

      Optional<Post> post = postRepository.findById(existingPostId);

      verify(statement).setInt(1, existingPostId);
      assertThat(post).isNotNull();
      assertThat(post.isPresent()).isTrue();
      assertThat(post.get().getId()).isEqualTo(existingPostId);
    }
  }



}

package unit.com.han.repository;

import com.han.constants.TableColumnsPost;
import com.han.model.Post;
import com.han.repository.PostRepositoryImpl;
import org.assertj.core.api.Assertions;
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

  private final List<String> skipSetUpMethods = List.of("findAll_Throw_IllegalArgumentException");

  @BeforeEach
  public void setUp(TestInfo info) throws SQLException {
    if (skipSetUpMethods.contains(info.getTestMethod().get().getName())) return;

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  @Nested
  class FindAll_Test {

    private int limit = 20;
    private int offset = 20;
    private String orderBy = TableColumnsPost.ID.getName();

    @Test
    public void findAll_Throw_IllegalArgumentException() throws IllegalArgumentException {
      String invalidColumnName = "hi";
      Integer invalidLimit = limit;
      Integer invalidOffset = offset;

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> postRepository.findAll(invalidColumnName, invalidLimit, invalidOffset));
      assertEquals("Invalid parameters: Please check parameter", exception.getMessage());

    }

    @Test
    public void findAll_Throw_SQLException_With_Invalid_Query() throws SQLException {
      when(statement.executeQuery()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postRepository.findAll(orderBy, limit, offset));
    }

    @Test
    public void findAll_Return_Empty_List() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      List<Post> post = postRepository.findAll(orderBy, limit, offset);

      verify(statement).setInt(1, limit);
      verify(statement).setInt(2, offset);

      Assertions.assertThat(post.size()).isEqualTo(0);
    }

    @Test
    public void findAll_Return_Post_List() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPost.ID.getName())).thenReturn(2).thenReturn(1);

      List<Post> post = postRepository.findAll(orderBy, limit, offset);

      verify(statement).setInt(1, limit);
      verify(statement).setInt(2, offset);

      Assertions.assertThat(post.size()).isEqualTo(2);
      Assertions.assertThat(post.get(0).getId()).isGreaterThan(post.get(1).getId());
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
      Assertions.assertThat(post).isNotNull();
      Assertions.assertThat(post.isPresent()).isFalse();
    }


    @Test
    public void findById_Return_Post() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPost.ID.getName())).thenReturn(existingPostId);

      Optional<Post> post = postRepository.findById(existingPostId);

      verify(statement).setInt(1, existingPostId);
      Assertions.assertThat(post).isNotNull();
      Assertions.assertThat(post.isPresent()).isTrue();
      Assertions.assertThat(post.get().getId()).isEqualTo(existingPostId);
    }
  }



}

package unit.com.han.repository;

import com.han.model.Post;
import com.han.repository.PostRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

  @BeforeEach
  public void setUp() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  @Test
  public void findAll_Throw_SQLException_With_Invalid_Query() throws SQLException {
    String orderBy = Post.Columns.ID;
    int limit = 20;
    int offset = 10;

    when(statement.executeQuery()).thenThrow(SQLException.class);
    assertThrows(SQLException.class, () -> postRepository.findAll(orderBy, limit, offset));
  }

  @Test
  public void findAll_Return_Empty_List() throws SQLException {
    String orderBy = Post.Columns.ID;
    int limit = 20;
    int offset = 10;

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    List<Post> post = postRepository.findAll(orderBy, limit, offset);

    verify(statement).setString(1, orderBy);
    verify(statement).setInt(2, limit);
    verify(statement).setInt(3, limit * offset);

    Assertions.assertThat(post.size()).isEqualTo(0);
  }

  @Test
  public void findAll_Return_Post_List() throws SQLException {
    String orderBy = Post.Columns.ID;
    int limit = 20;
    int offset = 10;

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(resultSet.getInt(Post.Columns.ID)).thenReturn(2).thenReturn(1);

    List<Post> post = postRepository.findAll(orderBy, limit, offset);

    verify(statement).setString(1, orderBy);
    verify(statement).setInt(2, limit);
    verify(statement).setInt(3, limit * offset);

    Assertions.assertThat(post.size()).isEqualTo(2);
    Assertions.assertThat(post.get(0).getId()).isGreaterThan(post.get(1).getId());
  }

  @Test
  public void findById_Throw_SQLException_With_Invalid_Query() throws SQLException {
    int nonExistingPostId = -1;
    when(statement.executeQuery()).thenThrow(SQLException.class);
    assertThrows(SQLException.class, () -> postRepository.findById(nonExistingPostId));
  }

  @Test
  public void findById_Return_Null() throws SQLException {
    int nonExistingPostId = -1;
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    Optional<Post> post = postRepository.findById(nonExistingPostId);

    verify(statement).setInt(1, nonExistingPostId);
    Assertions.assertThat(post).isNotNull();
    Assertions.assertThat(post.isPresent()).isFalse();
  }


  @Test
  public void findById_Return_Post() throws SQLException {
    int existingPostId = 1;

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getInt(Post.Columns.ID)).thenReturn(existingPostId);

    Optional<Post> post = postRepository.findById(existingPostId);

    verify(statement).setInt(1, existingPostId);
    Assertions.assertThat(post).isNotNull();
    Assertions.assertThat(post.isPresent()).isTrue();
    Assertions.assertThat(post.get().getId()).isEqualTo(existingPostId);
  }

}

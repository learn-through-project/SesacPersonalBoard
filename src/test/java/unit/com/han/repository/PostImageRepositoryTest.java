package unit.com.han.repository;


import com.han.constants.tablesColumns.TableColumnsPostImages;
import com.han.model.Post;
import com.han.model.PostImage;
import com.han.repository.PostImageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostImageRepositoryTest {

  @Mock
  private DataSource dataSource;

  @Mock
  private Connection connection;

  @Mock
  private PreparedStatement statement;

  @Mock
  private ResultSet resultSet;

  @Mock
  private MultipartFile dummyFile;

  @InjectMocks
  private PostImageRepositoryImpl postImagesRepository;

  private final List<String> skipSetUpMethods = List.of(
          "insert_Return_Fail",
          "insert_Return_Success",
          "insert_Throws_Exception",
          "insertBulk_Return_Success"
  );

  @BeforeEach
  public void setUp(TestInfo info) throws SQLException {
    if (skipSetUpMethods.contains(info.getTestMethod().get().getName())) return;

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  @Nested
  class DeleteById_Test {

    private int success = 1;
    private int fail = 0;
    private int imageId = 1;

    @Test
    public void deleteById_Throws_Exception() throws SQLException {
      when(statement.executeUpdate()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postImagesRepository.deleteById(imageId));

      verify(statement).executeUpdate();
      verify(statement).setInt(1, imageId);
    }

    @Test
    public void deleteById_Return_Fail() throws SQLException {
      when(statement.executeUpdate()).thenReturn(fail);
      boolean result = postImagesRepository.deleteById(imageId);

      verify(statement).setInt(1, imageId);
      verify(statement).executeUpdate();
      assertThat(result).isFalse();
    }

    @Test
    public void deleteById_Return_Success() throws SQLException {
      when(statement.executeUpdate()).thenReturn(success);
      boolean result = postImagesRepository.deleteById(imageId);

      verify(statement).setInt(1, imageId);
      verify(statement).executeUpdate();
      assertThat(result).isTrue();
    }
  }

  @Nested
  class Update_Test {
    private int success = 1;
    private int fail = 0;
    private PostImage dummyImage = new PostImage(1, 1, "http://", 1);

    @Test
    public void update_Throws_Exception() throws SQLException {
      when(statement.executeUpdate()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postImagesRepository.update(dummyImage));

      verify(statement).executeUpdate();
      verify(statement).setInt(1, dummyImage.getPostId());
      verify(statement).setString(2, dummyImage.getUrl());
      verify(statement).setInt(3, dummyImage.getId());
    }

    @Test
    public void update_Return_Fail() throws SQLException {
      when(statement.executeUpdate()).thenReturn(fail);
      boolean result = postImagesRepository.update(dummyImage);
      assertThat(result).isFalse();
    }

    @Test
    public void update_Return_Success() throws SQLException {
      when(statement.executeUpdate()).thenReturn(success);
      boolean result = postImagesRepository.update(dummyImage);
      assertThat(result).isTrue();
    }
  }

  @Nested
  class InsertBulk_Test {

//    private List<MultipartFile> dummyImages;

    private List<PostImage> dummyImages;

    @BeforeEach
    public void setUp() {
      this.dummyImages = List.of(
              new PostImage(1, "url", 1),
              new PostImage(1, "url2", 2)
      );
    }
    @Test
    public void insertBulk_Throw_Exception_And_Rollback() throws SQLException {
      when(dataSource.getConnection()).thenReturn(connection);
      when(connection.prepareStatement(anyString())).thenReturn(statement);
      when(statement.executeUpdate()).thenReturn(1).thenThrow(SQLException.class);

      assertThrows(SQLException.class, () -> postImagesRepository.insertBulk(dummyImages));

      verify(connection).setAutoCommit(false);
      verify(statement, times(dummyImages.size())).executeUpdate();
      verify(connection).rollback();
    }
    @Test
    public void insertBulk_Return_Success() throws SQLException {
      when(dataSource.getConnection()).thenReturn(connection);
      when(connection.prepareStatement(anyString())).thenReturn(statement);
      for (PostImage image : dummyImages) {
        when(statement.executeUpdate()).thenReturn(1);
      }

      boolean result = postImagesRepository.insertBulk(dummyImages);

      verify(connection).setAutoCommit(false);
      verify(statement, times(2)).setInt(1, dummyImages.get(0).getPostId());
      verify(statement, times(dummyImages.size())).setInt(1, dummyImages.get(0).getPostId());
      for (PostImage image: dummyImages) {
        verify(statement).setString(2, image.getUrl());
        verify(statement).setInt(3, image.getImageOrder());
      }
      verify(statement, times(dummyImages.size())).executeUpdate();
      verify(connection).commit();
      assertThat(result).isTrue();
    }
  }


  @Nested
  class Insert_Test {

    private int success = 1;
    private int fail = 0;
    private PostImage dummyImage = new PostImage(1, 1, "http://", 1);

    private Integer dummyImageId = 1;

    @Test
    public void insert_Throws_Exception() throws SQLException {
      when(dataSource.getConnection()).thenReturn(connection);
      when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
      when(statement.executeUpdate()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postImagesRepository.insert(dummyImage));

      verify(statement).setInt(1, dummyImage.getPostId());
      verify(statement).setString(2, dummyImage.getUrl());
      verify(statement).setInt(3, dummyImage.getImageOrder());
      verify(statement).executeUpdate();
    }

    @Test
    public void insert_Return_Fail() throws SQLException {
      when(dataSource.getConnection()).thenReturn(connection);
      when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
      when(statement.executeUpdate()).thenReturn(fail);
      when(statement.getGeneratedKeys()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      Integer imageId = postImagesRepository.insert(dummyImage);

      verify(statement).executeUpdate();
      verify(statement).setInt(1, dummyImage.getPostId());
      verify(statement).setString(2, dummyImage.getUrl());
      verify(statement).setInt(3, dummyImage.getImageOrder());
      verify(statement).getGeneratedKeys();
      verify(resultSet).next();
      assertThat(imageId).isNull();
    }

    @Test
    public void insert_Return_Success() throws SQLException {
      when(dataSource.getConnection()).thenReturn(connection);
      when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
      when(statement.executeUpdate()).thenReturn(success);
      when(statement.getGeneratedKeys()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true);
      when(resultSet.getInt(1)).thenReturn(dummyImageId);

      Integer imageId = postImagesRepository.insert(dummyImage);

      verify(statement).setInt(1, dummyImage.getPostId());
      verify(statement).setString(2, dummyImage.getUrl());
      verify(statement).setInt(3, dummyImage.getImageOrder());
      verify(statement).executeUpdate();
      verify(statement).getGeneratedKeys();
      verify(resultSet).next();
      verify(resultSet).getInt(1);
      assertThat(imageId).isEqualTo(dummyImageId);
    }
  }

  @Nested
  class FindByPostId_Test {
    private int postId = 1;

    @Test
    public void findById_Throw_Exception() throws SQLException {
      when(statement.executeQuery()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postImagesRepository.findById(postId));

      verify(statement).setInt(1, postId);
    }

    @Test
    public void findByPostId_Return_Empty() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      Optional<PostImage> image = postImagesRepository.findById(postId);

      verify(statement).setInt(1, postId);

      assertThat(image).isNotNull();
      assertThat(image).isEmpty();
    }

    @Test
    public void findByPostId_Return_PostImage() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPostImages.ID.getName())).thenReturn(postId).thenReturn(postId);

      List<PostImage> image = postImagesRepository.findByPostId(postId);

      verify(statement).setInt(1, postId);

      assertThat(image.size()).isEqualTo(2);
      assertThat(image.get(0).getId()).isEqualTo(postId);
    }

  }

  @Nested
  class FindById_Test {
    private int imageId = 1;

    private PostImage dummyImage = new PostImage(imageId);

    @Test
    public void findById_Throw_Exception() throws SQLException {
      when(statement.executeQuery()).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> postImagesRepository.findById(imageId));

      verify(statement).setInt(1, imageId);
    }

    @Test
    public void findById_Return_Empty() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      Optional<PostImage> image = postImagesRepository.findById(imageId);

      verify(statement).setInt(1, imageId);

      assertThat(image).isNotNull();
      assertThat(image.isPresent()).isFalse();
    }

    @Test
    public void findById_Return_PostImage() throws SQLException {
      when(statement.executeQuery()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true).thenReturn(false);
      when(resultSet.getInt(TableColumnsPostImages.ID.getName())).thenReturn(imageId);

      Optional<PostImage> image = postImagesRepository.findById(imageId);

      verify(statement).setInt(1, imageId);

      assertThat(image).isNotNull();
      assertThat(image.isPresent()).isTrue();
      assertThat(image.get().getId()).isEqualTo(dummyImage.getId());
    }

  }

}

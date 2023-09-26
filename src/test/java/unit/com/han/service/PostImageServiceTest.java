package unit.com.han.service;

import com.han.model.PostImage;
import com.han.repository.PostImageRepository;
import com.han.service.ImageUploadService;
import com.han.service.PostImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostImageServiceTest {

  @Mock
  private PostImageRepository postImageRepository;

  @Mock
  private ImageUploadService imageUploadService;

  @Mock
  private MultipartFile dummyFile;

  @InjectMocks
  private PostImageServiceImpl postImageService;

  private List<MultipartFile> dummyFiles;

  private int postId = 1;

  @BeforeEach
  public void setUp() {
    this.dummyFiles = List.of(dummyFile, dummyFile, dummyFile);
  }

  @Nested
  class CreatePostImage_Test {

    private List<String>  dummyUrls = List.of("1","2","3");
    private List<PostImage> dummyPostImage = List.of(
            new PostImage(postId, dummyUrls.get(0), 0 + 1),
            new PostImage(postId, dummyUrls.get(1), 1 + 1),
            new PostImage(postId, dummyUrls.get(2), 2 + 1)
    );


    @Test
    public void createPostImage_Return_True_When_Success_All() throws IOException, SQLException {

      when(imageUploadService.uploadImages(dummyFiles)).thenReturn(dummyUrls);
      for (int i = 0; i < dummyUrls.size(); i++) {
        when(postImageRepository.insert(dummyPostImage.get(i))).thenReturn(true);
      }

      boolean result = postImageService.createPostImage(postId, dummyFiles);

      verify(imageUploadService).uploadImages(dummyFiles);
      verify(postImageRepository).insert(dummyPostImage.get(0));
      verify(postImageRepository).insert(dummyPostImage.get(1));
      verify(postImageRepository).insert(dummyPostImage.get(2));

      assertThat(result).isTrue();
    }
  }


}

package unit.com.han.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import com.han.service.ImageUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ImageUploadServiceTest {

  @Mock
  private Bucket storage;

  @Mock
  private Blob blob;

  @Mock
  private MultipartFile dummyFile;

  @InjectMocks
  private ImageUploadServiceImpl imageUploadService;

  @Nested
  class UploadImages_Test {

    private List<MultipartFile> dummyFiles;

    private String pathPrefix = "post/1";

    @BeforeEach
    public void setUp() {
      this.dummyFiles = List.of(dummyFile, dummyFile, dummyFile);
    }

    @Test
    public void uploadImage_Return_List_Of_String_When_Some_File_Fail() throws IOException {

      when(storage.create(pathPrefix + "/" +  0, dummyFile.getBytes(), dummyFile.getContentType())).thenReturn(blob);
      when(blob.getName()).thenReturn("mockedBlobName" + 0);

      when(storage.create(pathPrefix + "/" +  1, dummyFile.getBytes(), dummyFile.getContentType())).thenThrow(RuntimeException.class);


      assertThrows(RuntimeException.class, ()-> imageUploadService.uploadImages(dummyFiles, pathPrefix));

      verify(storage, times(1)).create(pathPrefix + "/" +  0, dummyFile.getBytes(), dummyFile.getContentType());
      verify(storage, times(1) ).create(pathPrefix + "/" +  1, dummyFile.getBytes(), dummyFile.getContentType());
    }

    @Test
    public void uploadImage_Return_List_Of_String_When_All_File_Success() throws IOException {

      for (int i = 0; i < dummyFiles.size(); i++) {
        when(storage.create(pathPrefix + "/" +  i, dummyFile.getBytes(), dummyFile.getContentType())).thenReturn(blob);
        when(blob.getName()).thenReturn("mockedBlobName" + i);
      }

      List<String> urls = imageUploadService.uploadImages(dummyFiles, pathPrefix);

      for (int i = 0; i < dummyFiles.size(); i++) {
        verify(storage).create(pathPrefix + "/" +  i, dummyFile.getBytes(), dummyFile.getContentType());
      }

      assertThat(urls.size()).isEqualTo(dummyFiles.size());
    }
  }


  @Nested
  class UploadImage_Test {

    private String pathname = "post/1/0";

    @Test
    public void uploadImage_Throws_IOException_When_GetBytes_Fail() throws IOException {
      when(dummyFile.getBytes()).thenThrow(IOException.class);
      assertThrows(IOException.class, () -> imageUploadService.uploadImage(dummyFile, pathname));
    }

    @Test
    public void uploadImage_Throws_StorageException_When_Create_Fail() throws IOException {
      when(storage.create(pathname, dummyFile.getBytes(), dummyFile.getContentType()))
              .thenThrow(StorageException.class);

      assertThrows(StorageException.class, () -> imageUploadService.uploadImage(dummyFile, pathname));
      verify(storage).create(pathname, dummyFile.getBytes(), dummyFile.getContentType());
    }

    @Test
    public void uploadImage_Return_String() throws IOException {
      when(storage.create(pathname, dummyFile.getBytes(), dummyFile.getContentType()))
              .thenReturn(blob);
      when(blob.getName()).thenReturn("mockedBlobName");

      String url = imageUploadService.uploadImage(dummyFile, pathname);

      verify(storage).create(pathname, dummyFile.getBytes(), dummyFile.getContentType());
      assertThat(url).contains(blob.getName());
    }
  }

}

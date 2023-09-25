package unit.com.han.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import com.han.repository.PostImageRepository;
import com.han.service.ImageUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Test
    public void uploadImage_Return_Empty_List_When_All_File_Fail() throws IOException {
      int fileCount = 3;
      int failFileCount = 3;

      when(storage.create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType()))
              .thenThrow(StorageException.class).thenThrow(StorageException.class).thenThrow(StorageException.class);

      MultipartFile[] files = { dummyFile, dummyFile, dummyFile };
      List<String> urls = imageUploadService.uploadImages(files);

      verify(storage, times(fileCount)).create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType());
      assertThat(urls.size()).isEqualTo(fileCount - failFileCount);
    }


    @Test
    public void uploadImage_Return_List_Of_String_When_Some_File_Success() throws IOException {
      int fileCount = 3;
      int failFileCount = 1;

      when(storage.create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType()))
              .thenReturn(blob).thenThrow(StorageException.class).thenReturn(blob);
      when(blob.getName()).thenReturn("mockedBlobName1").thenReturn("mockedBlobName2");

      MultipartFile[] files = { dummyFile, dummyFile, dummyFile };
      List<String> urls = imageUploadService.uploadImages(files);

      verify(storage, times(fileCount)).create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType());
      assertThat(urls.size()).isEqualTo(fileCount - failFileCount);
    }

    @Test
    public void uploadImage_Return_List_Of_String_When_All_File_Success() throws IOException {
      when(storage.create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType()))
              .thenReturn(blob).thenReturn(blob);
      when(blob.getName()).thenReturn("mockedBlobName1").thenReturn("mockedBlobName2");

      MultipartFile[] files = { dummyFile, dummyFile };
      List<String> urls = imageUploadService.uploadImages(files);

      verify(storage, times(files.length)).create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType());
      assertThat(urls.size()).isEqualTo(files.length);
    }
  }


  @Nested
  class UploadImage_Test {

    @Test
    public void uploadImage_Throws_IOException_When_GetBytes_Fail() throws IOException {
      when(dummyFile.getBytes()).thenThrow(IOException.class);
      assertThrows(IOException.class, () -> imageUploadService.uploadImage(dummyFile));
    }

    @Test
    public void uploadImage_Throws_StorageException_When_Create_Fail() throws IOException {
      when(storage.create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType()))
              .thenThrow(StorageException.class);

      assertThrows(StorageException.class, () -> imageUploadService.uploadImage(dummyFile));
      verify(storage).create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType());
    }

    @Test
    public void uploadImage_Return_String() throws IOException {
      when(storage.create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType()))
              .thenReturn(blob);
      when(blob.getName()).thenReturn("mockedBlobName");

      String url = imageUploadService.uploadImage(dummyFile);

      verify(storage).create(dummyFile.getName(), dummyFile.getBytes(), dummyFile.getContentType());
      assertThat(url).contains(blob.getName());
    }
  }

}

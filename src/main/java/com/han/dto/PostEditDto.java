package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostEditDto {

  @NotNull(message = "not null")
  private int userId;

  @NotEmpty(message = "not empty")
  @Size(min = 5, message = "At least 5 long")
  private String title;

  @NotEmpty (message = "not empty")
  @Size(min = 5, message = "At least 5 long")
  private String textContent;
  @Size(min = 1, message = "At least one image is required")
  private List<MultipartFile> images;
  private List<Integer> imgFlag;

  public PostEditDto(int userId, String title, String textContent, List<MultipartFile> images, List<Integer> imgFlag) {
    this.userId = userId;
    this.title = title;
    this.textContent = textContent;
    this.images = images;
    this.imgFlag = imgFlag;
  }
}

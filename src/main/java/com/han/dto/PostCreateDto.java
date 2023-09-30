package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class PostCreateDto {

  @NotNull
  private int userId;

  @NotEmpty
  private String textContent;
  @Size(min = 1, message = "At least one image is required")
  private List<MultipartFile> images;

  public PostCreateDto(int userId, String textContent, List<MultipartFile> images) {
    this.userId = userId;
    this.textContent = textContent;
    this.images = images;
  }
}

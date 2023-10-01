package com.han.dto;

import com.han.model.PostImage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDetailDto {
  private Integer id;
  private Integer userId;
  private String title;
  private String textContent;
  private List<PostImage> images;
  private LocalDateTime updatedAt;
}

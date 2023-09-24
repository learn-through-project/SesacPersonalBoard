package com.han.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
  private Integer id;
  private Integer author;
  private String textContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

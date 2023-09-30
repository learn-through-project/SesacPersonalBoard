package com.han.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {

  private Integer id;
  private Integer userId;
  private String title;
  private String textContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Post() {}
  public Post (Integer id) {
    this.id = id;
  }
  public Post (Integer userId, String title, String textContent) {
    this.userId = userId;
    this.title = title;
    this.textContent = textContent;
  }

  public Post (Integer postId, Integer userId, String title, String textContent) {
    this.id = postId;
    this.userId = userId;
    this.title = title;
    this.textContent = textContent;
  }
}

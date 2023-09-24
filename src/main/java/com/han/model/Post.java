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

  public Post() {}
  public Post (Integer id) {
    this.id = id;
  }

  public Post (Integer author, String textContent) {
    this.author = author;
    this.textContent = textContent;
  }
}

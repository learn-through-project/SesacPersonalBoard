package com.han.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
  public static class Columns {
    public final static String ID = "id"; // int

    public final static String AUTHOR = "author"; // int

    public final static String TEXT_CONTENT = "text_content"; // String

    public final static String CREATED_AT = "CREATED_AT"; // LocalDateTime

    public final static String UPDATED_AT = "UPDATED_AT"; // LocalDateTime
  }

  private Integer id;
  private Integer author;
  private String textContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Post() {}
  public Post (Integer id) {
    this.id = id;
  }
}

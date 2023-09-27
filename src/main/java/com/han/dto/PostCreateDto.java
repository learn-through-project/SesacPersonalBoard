package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDto {

  @NotNull
  private int userId;

  @NotEmpty
  private String textContent;

  public PostCreateDto(int userId, String textContent) {
    this.userId = userId;
    this.textContent = textContent;
  }
}

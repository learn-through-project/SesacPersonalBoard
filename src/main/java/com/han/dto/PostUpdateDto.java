package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostUpdateDto {
  @NotNull
  private final Integer id;

  private final int userId;

  private String title;

  @NotEmpty
  private final String textContent;
}

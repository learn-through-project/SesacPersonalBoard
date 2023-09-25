package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostUpdateDto {
  @NotNull
  private final int id;

  @NotNull
  private final int author;

  @NotEmpty
  private final String textContent;
}

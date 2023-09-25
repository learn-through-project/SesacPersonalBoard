package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostCreateDto {

  @NotNull
  private final int userId;

  @NotEmpty
  private final String textContent;
}

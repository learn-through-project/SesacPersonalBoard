package com.han.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostListReqDto {
  @NotNull(message = "Sort can not be null")
  private final String sort;

  @NotNull(message = "Limit can not be null")
  private final int limit;

  @NotNull(message = "Page can not be null")
  private final int page;

}

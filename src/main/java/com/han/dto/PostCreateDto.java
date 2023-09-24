package com.han.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostCreateDto {

  private final int author;
  private final String textContent;
}

package com.han.dto;

import com.han.constants.OrderType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
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

  private OrderType order = OrderType.ASC;

}

package com.han.dto;

import com.han.constants.OrderType;
import com.han.constants.tablesColumns.TableColumnsPost;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostListReqDto {

  private final TableColumnsPost sort;

  @NotNull(message = "Limit can not be null")
  private final int limit;

  @NotNull(message = "Page can not be null")
  private final int page;

  private OrderType order = OrderType.ASC;

}

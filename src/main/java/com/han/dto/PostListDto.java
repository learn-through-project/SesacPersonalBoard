package com.han.dto;

import com.han.constants.OrderType;
import com.han.constants.SortType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class PostListDto {
  @NotNull(message = "Limit can not be null")
  private int limit;

  @NotNull(message = "Page can not be null")
  private int page;
  private OrderType order = OrderType.ASC;
  private SortType sort = SortType.NEW;

  public PostListDto(int limit, int page) {
    this.limit = limit;
    this.page = page;
  }

  public void setOrder(String order) {
    Optional<OrderType> orderType = OrderType.getOrderType(order);
    if (orderType.isEmpty()) {
      throw new IllegalArgumentException("invalid order type");
    }
    this.order = orderType.get();
  }
  public void setSort(String sort) {
      Optional<SortType> sortType = SortType.getSortType(sort);
      if (sortType.isEmpty()) {
        throw new IllegalArgumentException("invalid sort type");
      }
      this.sort = sortType.get();
    }
}

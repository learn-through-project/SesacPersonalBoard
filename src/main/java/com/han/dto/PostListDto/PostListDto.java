package com.han.dto.PostListDto;

import com.han.annotation.ValidEnum.ValidEnum;
import com.han.constants.OrderType;
import com.han.constants.SortType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class PostListDto {
  private static final int defaultLimit = 10;
  private static final int defaultPage = 1;

  @NotNull(message = "Limit can be null")
  private Integer limit;
  @NotNull(message = "Page can be null")
  private Integer page;

  @ValidEnum(enumClass = OrderType.class, message = "Check order type")
  private OrderType order = OrderType.ASC;

  @ValidEnum(enumClass = SortType.class, message = "Check sort type")
  private SortType sort = SortType.NEW;

  public static PostListDto getDefaultInstance() {
    return new PostListDto(PostListDto.defaultLimit, PostListDto.defaultPage);
  }

  public PostListDto(int limit, int page) {
    this.limit = limit;
    this.page = page;
  }

  public void setOrder(String order) {
    Optional<OrderType> orderType = OrderType.getOrderType(order);
    this.order = orderType.orElse(null);
  }
  public void setSort(String sort) {
      Optional<SortType> sortType = SortType.getSortType(sort);
      this.sort = sortType.orElse(null);
    }
}

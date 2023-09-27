package com.han.constants;

import java.util.Arrays;
import java.util.Optional;

public enum OrderType {
  ASC, DESC;

  public static Optional<OrderType> getOrderType(String order) {
    return Arrays.stream(OrderType.values())
            .filter((value) -> value.name().equalsIgnoreCase(order))
            .findAny();
  }
}
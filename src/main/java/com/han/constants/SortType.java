package com.han.constants;

import java.util.Arrays;
import java.util.Optional;

public enum SortType {
  NEW;

  public static Optional<SortType> getSortType(String sort) {
    return Arrays.stream(SortType.values())
            .filter((value) -> value.name().equalsIgnoreCase(sort))
            .findAny();
    }

}

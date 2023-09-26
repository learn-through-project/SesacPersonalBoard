package com.han.constants.tablesColumns;

import java.util.Arrays;

public enum TableColumnsPostImages {
  TABLE("post_images", String.class),
  ID("id", Integer.class),
  POST_ID("post_id", Integer.class),
  URL("url", String.class),
  IMAGE_ORDER("image_order", Integer.class);

  private final String columnName;

  TableColumnsPostImages(String columnName, Class<?> columnType) {
    this.columnName = columnName;
  }

  public String getName() {
    return columnName;
  }

  public static boolean isValidColumn(String targetColumn) {
    return Arrays
            .stream(TableColumnsPost.values())
            .anyMatch((column) -> column.
                    getName()
                    .equalsIgnoreCase(targetColumn));

  }
}

package com.han.constants;

import java.time.LocalDateTime;
import java.util.Arrays;

public enum TableColumnsPost {

  TABLE("posts", String.class),
  ID("id", Integer.class),
  AUTHOR("author", Integer.class),
  TEXT_CONTENT("text_content", String.class),
  CREATED_AT("created_at", LocalDateTime.class),
  UPDATED_AT("updated_at", LocalDateTime.class);

  private final String columnName;

  TableColumnsPost(String columnName, Class<?> columnType) {
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

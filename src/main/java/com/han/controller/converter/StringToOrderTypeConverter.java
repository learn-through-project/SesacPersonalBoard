package com.han.controller.converter;

import com.han.constants.OrderType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderTypeConverter implements Converter<String, OrderType> {

  @Override
  public OrderType convert(String source) {
    try {
      return OrderType.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid value for OrderType: " + source);
    }
  }
}

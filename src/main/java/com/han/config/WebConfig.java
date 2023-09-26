package com.han.config;

import com.han.controller.converter.StringToOrderTypeConverter;
import com.han.controller.converter.StringToPostCreateDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToOrderTypeConverter());
    registry.addConverter(new StringToPostCreateDtoConverter());
  }
}


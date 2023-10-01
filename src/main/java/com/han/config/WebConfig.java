package com.han.config;

import com.han.constants.EndPoint;
import com.han.constants.ViewName;
import com.han.controller.converter.StringToPostCreateDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToPostCreateDtoConverter());
  }
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(EndPoint.POST_NEW).setViewName(ViewName.POST_NEW_VIEW);
  }
  @Bean
  public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
    return new HiddenHttpMethodFilter();
  }
}


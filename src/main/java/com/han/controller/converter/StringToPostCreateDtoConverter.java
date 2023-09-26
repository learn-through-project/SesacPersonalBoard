package com.han.controller.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.han.dto.PostCreateDto;
import org.springframework.core.convert.converter.Converter;

public class StringToPostCreateDtoConverter implements Converter<String, PostCreateDto> {
  @Override
  public PostCreateDto convert(String source) {
    try {
      PostCreateDto dto = new ObjectMapper().readValue(source, PostCreateDto.class);
      return dto;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

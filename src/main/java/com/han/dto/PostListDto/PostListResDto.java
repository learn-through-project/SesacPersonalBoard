package com.han.dto.PostListDto;

import com.han.model.Post;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostListResDto {
  private final List<Post> list;
  private final int count;
}

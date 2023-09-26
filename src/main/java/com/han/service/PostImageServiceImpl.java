package com.han.service;

import com.han.model.PostImage;
import com.han.repository.PostImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
public class PostImageServiceImpl implements PostImageService {

  private PostImageRepository postImageRepository;

  private ImageUploadService imageUploadService;

  @Autowired
  public PostImageServiceImpl(PostImageRepository postImageRepository, ImageUploadService imageUploadService) {
    this.postImageRepository = postImageRepository;
    this.imageUploadService = imageUploadService;
  }

  @Override
  public boolean createPostImage(int postId, List<MultipartFile> files) throws IOException, SQLException {
    List<Boolean> results = new LinkedList<>();

    List<String> urls = imageUploadService.uploadImages(files);

    for (int i = 0; i < urls.size(); i++) {
      boolean result = postImageRepository.insert(new PostImage(postId, urls.get(i), i + 1));
      results.add(result);
    }

    return results.size() == urls.size();
  }
}

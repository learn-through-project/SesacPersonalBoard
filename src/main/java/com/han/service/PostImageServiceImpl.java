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
  public boolean createPostImage(Integer postId, List<MultipartFile> files) throws Exception {
    List<Boolean> results = new LinkedList<>();
    String pathPrefix = "post/" + postId;

    try {
      List<String> urls = imageUploadService.uploadImages(files, pathPrefix);

      for (int i = 0; i < urls.size(); i++) {
        results.add(postImageRepository.insert(new PostImage(postId, urls.get(i), i + 1)));
      }

      for (boolean isSuccess : results) {
        if (!isSuccess) throw new Exception("이미지 삽입에 실패하였습니다.");
      }

    } catch (Exception ex) {
      log.error("error in createPostImage: >>" + ex.getMessage());
      imageUploadService.deleteImages(files, pathPrefix);
      throw new Exception("이미지 삽입에 실패하였습니다.");
    }
    
    return true;
  }
}

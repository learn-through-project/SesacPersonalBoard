package com.han.service;

import com.han.model.PostImage;
import com.han.repository.PostImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
  public List<PostImage> getAllImagesByPostId(Integer postId) throws SQLException {
    List<PostImage> images = postImageRepository.findByPostId(postId);
    return images;
  }

  @Override
  public boolean createPostImage(Integer postId, List<MultipartFile> files) throws Exception {
    String pathPrefix = "post/" + postId;

    try {
      List<String> urls = imageUploadService.uploadImages(files, pathPrefix);

      List<PostImage> postImages = IntStream
              .range(0, files.size())
              .mapToObj((idx) -> new PostImage(postId, urls.get(idx), idx + 1))
              .collect(Collectors.toList());

      postImageRepository.insertBulk(postImages);

    } catch (Exception ex) {
      log.error("error in createPostImage: >>" + ex.getMessage());
      imageUploadService.deleteImages(files, pathPrefix);
      throw new Exception("이미지 삽입에 실패하였습니다.");
    }

    return true;
  }
}

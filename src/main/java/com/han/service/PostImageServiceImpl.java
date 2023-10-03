package com.han.service;

import com.han.model.PostImage;
import com.han.repository.PostImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
public class PostImageServiceImpl implements PostImageService {

  private static final Integer DELETED_FILE_FLAG = -1;
  private static final Integer NEW_FILE_FLAG = 0;
  private static final String PATH_PREFIX = "post/";

  private PostImageRepository postImageRepository;

  private ImageUploadService imageUploadService;

  @Autowired
  public PostImageServiceImpl(PostImageRepository postImageRepository, ImageUploadService imageUploadService) {
    this.postImageRepository = postImageRepository;
    this.imageUploadService = imageUploadService;
  }

  @Override
  public boolean deletePostImage(Integer postId) throws Exception {
    String pathPrefix = PATH_PREFIX + postId;
    List<MultipartFile> cachedExistingFiles = getAllExistingImages(postId, pathPrefix);

    Integer count = postImageRepository.count(postId);
    log.info("count: > " + count);
    try {
      for (int i = 0; i < count; i++) {
        log.info("path: > " + pathPrefix + "/" + i);
        imageUploadService.deleteImage(pathPrefix + "/" + i);
      }

      postImageRepository.deleteByPostId(postId);

    } catch (Exception ex) {
      log.error("error in editPostImage: >>" + ex.getMessage());
      imageUploadService.uploadImages(cachedExistingFiles, pathPrefix);
      throw new Exception("이미지 삽입에 실패하였습니다.");
    }




    return false;
  }

  @Override
  public boolean editPostImage(Integer postId, List<MultipartFile> files, List<Integer> fileFlags) throws Exception {
    String pathPrefix = PATH_PREFIX + postId;
    List<Integer> newFlags = rearrangeFlags(fileFlags);
    List<MultipartFile> cachedExistingFiles = getAllExistingImages(postId, pathPrefix);
    List<MultipartFile> selectedExistingFiles = getSelectedExistingImages(pathPrefix, newFlags);
    List<MultipartFile> fileList = mergeNewFileWithExistingImages(newFlags, files, selectedExistingFiles);

    try {
      List<String> urls = imageUploadService.uploadImages(fileList, pathPrefix);

      for (int j = 0; j < newFlags.size(); j++) {
        Integer flag = newFlags.get(j);
        if (flag == DELETED_FILE_FLAG) {
          imageUploadService.deleteImage(pathPrefix + "/" + j);
        }
      }

      List<PostImage> postImages = IntStream
              .range(0, fileList.size())
              .mapToObj((idx) -> new PostImage(postId, urls.get(idx), idx))
              .collect(Collectors.toList());

      postImageRepository.upsertBulk(postId, postImages);

    } catch (Exception ex) {
      log.error("error in editPostImage: >>" + ex.getMessage());
      imageUploadService.deleteImages(fileList, pathPrefix);
      imageUploadService.uploadImages(cachedExistingFiles, pathPrefix);
      throw new Exception("이미지 삽입에 실패하였습니다.");
    }

    return true;
  }

  private List<Integer> rearrangeFlags(List<Integer> fileFlags) {
    List<Integer> newFlags = new LinkedList<>();

    for (Integer flag : fileFlags) {
      if (flag != DELETED_FILE_FLAG && flag != null) {
        newFlags.add(flag);
      }
    }

    for (int i = 0; i < fileFlags.size() - newFlags.size(); i++) {
      newFlags.add(DELETED_FILE_FLAG);
    }

    return newFlags;
  }

  private List<MultipartFile> getAllExistingImages(Integer postId, String pathPrefix) throws SQLException {
    List<MultipartFile> existingFiles = new LinkedList<>();
    int count = postImageRepository.count(postId);

    for (int i = 0; i < count; i++) {
      String path = pathPrefix + "/" + i;
      MultipartFile file = imageUploadService.downloadFile(path);
      existingFiles.add(file);
    }

    return existingFiles;
  }

  private List<MultipartFile> getSelectedExistingImages(String pathPrefix, List<Integer> fileFlags) throws SQLException {
    List<MultipartFile> existingFiles = new LinkedList<>();

    for (int j = 0; j < fileFlags.size(); j++) {
      Integer flag = fileFlags.get(j);
      if (flag != NEW_FILE_FLAG && flag != DELETED_FILE_FLAG && flag != null) {
        Optional<PostImage> image = postImageRepository.findById(flag);
        if (image.isPresent()) {
          String path = pathPrefix + "/" + image.get().getImageOrder();
          MultipartFile file = imageUploadService.downloadFile(path);
          existingFiles.add(file);
        }
      }
    }

    return existingFiles;
  }

  private List<MultipartFile> mergeNewFileWithExistingImages(List<Integer> flags, List<MultipartFile> newFiles, List<MultipartFile> existingFiles) throws SQLException {
    Queue<MultipartFile> existingFileQ = new LinkedList<>(existingFiles);
    Queue<MultipartFile> newFileQ = newFiles.stream().filter((f) -> !f.isEmpty()).collect(Collectors.toCollection(LinkedList::new));

    List<MultipartFile> files = new LinkedList<>();

    for (int i = 0; i < flags.size(); i++) {
      Integer flag = flags.get(i);
      if (flag != NEW_FILE_FLAG && flag != DELETED_FILE_FLAG && flag != null) {
        files.add(existingFileQ.poll());
      } else if (flag == NEW_FILE_FLAG && flag != null) {
        files.add(newFileQ.poll());
      }
    }

    return files;
  }

  @Override
  public List<PostImage> getAllImagesByPostId(Integer postId) throws SQLException {
    List<PostImage> images = postImageRepository.findByPostId(postId);
    return images;
  }

  @Override
  public boolean createPostImage(Integer postId, List<MultipartFile> files) throws Exception {
    String pathPrefix = PATH_PREFIX + postId;
    List<MultipartFile> newFiles = files.stream().filter((f) -> !f.isEmpty()).collect(Collectors.toList());

    if (newFiles.size() > 0) {
      try {
        List<String> urls = imageUploadService.uploadImages(newFiles, pathPrefix);

        List<PostImage> postImages = IntStream
                .range(0, newFiles.size())
                .mapToObj((idx) -> new PostImage(postId, urls.get(idx), idx))
                .collect(Collectors.toList());

        postImageRepository.insertBulk(postImages);

      } catch (Exception ex) {
        log.error("error in createPostImage: >>" + ex.getMessage());
        imageUploadService.deleteImages(newFiles, pathPrefix);
        throw new Exception("이미지 삽입에 실패하였습니다.");
      }
    }

    return true;
  }
}

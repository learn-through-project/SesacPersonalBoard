package com.han.repository;

import com.han.model.PostImage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostImageRepository {
  public Optional<PostImage> findById(int imageId) throws SQLException;
  public List<PostImage> findByPostId(int postId) throws SQLException;
  public boolean insert(PostImage image) throws SQLException;
  public boolean update(PostImage image) throws SQLException;
  public boolean deleteById(int imageId) throws SQLException;
}

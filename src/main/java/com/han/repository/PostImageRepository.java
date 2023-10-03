package com.han.repository;

import com.han.model.PostImage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostImageRepository {
  Optional<PostImage> findById(int imageId) throws SQLException;

  List<PostImage> findByPostId(int postId) throws SQLException;

  boolean insertBulk(List<PostImage> images) throws SQLException;

  Integer insert(PostImage image) throws SQLException;

  boolean update(PostImage image) throws SQLException;

  boolean deleteById(int imageId) throws SQLException;

  public boolean upsertBulk(Integer postId, List<PostImage> images) throws SQLException;

  public int count (Integer postId) throws SQLException;
}

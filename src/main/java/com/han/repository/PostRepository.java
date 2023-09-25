package com.han.repository;

import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
  public Optional<Post> findById(int postId) throws SQLException;
  public List<Post> findAll(String orderBy, int limit, int offset) throws SQLException, IllegalArgumentException;
  public boolean insert(Post post) throws SQLException;
  public boolean update(Post post) throws SQLException;
  public boolean deletePermanently(int postId) throws SQLException;
}

package com.han.repository;

import com.han.constants.OrderType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
  public Optional<Post> findById(int postId) throws SQLException;
  public List<Post> findAll(OrderType order, TableColumnsPost orderBy, int limit, int offset) throws SQLException, IllegalArgumentException;
  public Integer insert(Post post) throws SQLException;
  public boolean update(Post post) throws SQLException;
  public boolean deletePermanently(int postId) throws SQLException;
  public int getPostTotalCount() throws SQLException;
}

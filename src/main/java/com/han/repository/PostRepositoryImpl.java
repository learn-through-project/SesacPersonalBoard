package com.han.repository;

import com.han.constants.OrderType;
import com.han.constants.tablesColumns.TableColumnsPost;
import com.han.model.Post;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Log4j2
@Repository
public class PostRepositoryImpl implements PostRepository {

  private final DataSource dataSource;

  @Autowired
  public PostRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Override
  public int getPostTotalCount() throws SQLException {
    String countQuery = "SELECT COUNT(*) FROM " + TableColumnsPost.TABLE.getName();
    int count = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(countQuery)) {
        try (ResultSet rs = statement.executeQuery()) {
          if (rs != null && rs.next()) {
            count = rs.getInt(1);
          }
        }
      }
    }

    return count;
  }

  @Override
  public boolean deletePermanently(int postId) throws SQLException {
    String deleteQuery = "DELETE FROM " + TableColumnsPost.TABLE.getName() + " WHERE "
            + TableColumnsPost.ID + " = ?";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
        statement.setInt(1, postId);
        result = statement.executeUpdate();
      }
    }

    return result > 0;

  }

  @Override
  public boolean update(Post post) throws SQLException {
    String updateQuery = "UPDATE posts SET "
            + TableColumnsPost.USER_ID + " = ? , "
            + TableColumnsPost.TEXT_CONTENT + " = ? "
            + " WHERE " + TableColumnsPost.ID + " = ?";

    int result = 0;


    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
        statement.setInt(1, post.getUserId());
        statement.setString(2, post.getTextContent());
        statement.setInt(3, post.getId());

        result = statement.executeUpdate();
      }
    }


    return result > 0;
  }

  @Override
  public Integer insert(Post post) throws SQLException {
    String insertQuery = "INSERT INTO " + TableColumnsPost.TABLE.getName() + " ("
            + TableColumnsPost.USER_ID.getName() + ","
            + TableColumnsPost.TITLE.getName() + ","
            + TableColumnsPost.TEXT_CONTENT.getName() + ")"
            + " VALUES (?, ?, ?)";

    Integer createdPostId = null;

    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement statement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
        statement.setInt(1, post.getUserId());
        statement.setString(2, post.getTitle());
        statement.setString(3, post.getTextContent());

        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            createdPostId = generatedKeys.getInt(1);
          }
        }

        conn.commit();
      } catch (Exception ex) {
        conn.rollback();
        throw ex;
      }

    }

    return createdPostId;
  }

  @Override
  public List<Post> findAll(OrderType order, TableColumnsPost orderBy, int limit, int offset) throws SQLException, IllegalArgumentException {
    if (!isValidParamsForFindAll(limit, offset)) {
      throw new IllegalArgumentException("Invalid parameters: Please check parameter");
    }

    String query = "SELECT * FROM "
            + TableColumnsPost.TABLE.getName()
            + " ORDER BY " + orderBy.getName() + " " + order.toString()
            + " limit ? "
            + " offset ?";

    List<Post> postList = new LinkedList<>();

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setInt(1, limit);
        statement.setInt(2, offset);

        try (ResultSet rs = statement.executeQuery()) {
          while (rs != null && rs.next()) {
            postList.add(resultSetToPost(rs));
          }
        }
      }
    }

    return postList;
  }

  private boolean isValidParamsForFindAll(int limit, int offset) {
    boolean isValidLimit = limit > 0;
    boolean isValidOffset = offset >= 0;

    return isValidOffset && isValidLimit;
  }

  @Override
  public Optional<Post> findById(int postId) throws SQLException {
    String query = "SELECT * FROM posts WHERE id = ?";
    Post post = null;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setInt(1, postId);
        try (ResultSet rs = statement.executeQuery()) {
          while (rs.next()) {
            post = resultSetToPost(rs);
          }
        }
      }
    }

    return Optional.ofNullable(post);
  }

  private Post resultSetToPost(ResultSet rs) throws SQLException {
    Integer id = rs.getInt(TableColumnsPost.ID.getName());
    Integer userId = rs.getInt(TableColumnsPost.USER_ID.getName());
    String title = rs.getString(TableColumnsPost.TITLE.getName());
    String textContent = rs.getString(TableColumnsPost.TEXT_CONTENT.getName());
    Timestamp createdAt = rs.getTimestamp(TableColumnsPost.CREATED_AT.getName());
    Timestamp updatedAt = rs.getTimestamp(TableColumnsPost.UPDATED_AT.getName());

    Post post = new Post();
    post.setId(id);
    post.setUserId(userId);
    post.setTitle(title);
    post.setTextContent(textContent);
    post.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
    post.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

    return post;
  }
}

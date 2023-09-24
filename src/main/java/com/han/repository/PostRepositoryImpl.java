package com.han.repository;

import com.han.constants.TableColumnsPost;
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
  public PostRepositoryImpl (DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Override
  public boolean insert(Post post) throws SQLException {
    String insertQuery = "INSERT INTO posts (author, text_content) VALUES (?, ?)";
    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
          statement.setInt(1, post.getAuthor());
          statement.setString(2, post.getTextContent());

          result = statement.executeUpdate();
      }
    }

    return result > 0;
  }

  @Override
  public List<Post> findAll(String orderBy, int limit, int offset) throws SQLException, IllegalArgumentException {
    if (!isValidParamsForFindAll(orderBy, limit, offset)) {
      throw new IllegalArgumentException("Invalid parameters: Please check parameter");
    }

    String query = "SELECT * FROM posts ORDER BY " + orderBy + " desc limit ? offset ?";
    List<Post> postList = new LinkedList<>();

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setInt(1, limit);
        statement.setInt(2, offset);

        try (ResultSet rs = statement.executeQuery()) {
          while (rs.next()) {
            postList.add(resultSetToPost(rs));
          }
        }
      }
    }

    return postList;
  }

  private boolean isValidParamsForFindAll(String orderBy, int limit, int offset) {
    boolean isValidSort = TableColumnsPost.isValidColumn(orderBy);
    boolean isValidLimit = limit >= 0;
    boolean isValidOffset = offset >= 0;

    return isValidSort && isValidOffset && isValidLimit;
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
    Integer authorId = rs.getInt(TableColumnsPost.AUTHOR.getName());
    String textContent = rs.getString(TableColumnsPost.TEXT_CONTENT.getName());
    Timestamp createdAt = rs.getTimestamp(TableColumnsPost.CREATED_AT.getName());
    Timestamp updatedAt = rs.getTimestamp(TableColumnsPost.UPDATED_AT.getName());

    Post post = new Post();
    post.setId(id);
    post.setAuthor(authorId);
    post.setTextContent(textContent);
    post.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
    post.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

    return post;
  }
}

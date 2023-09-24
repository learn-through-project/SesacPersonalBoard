package com.han.repository;

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
    Integer id = rs.getInt(Post.Columns.ID);
    Integer authorId = rs.getInt(Post.Columns.AUTHOR);
    String textContent = rs.getString(Post.Columns.TEXT_CONTENT);
    Timestamp createdAt = rs.getTimestamp(Post.Columns.CREATED_AT);
    Timestamp updatedAt = rs.getTimestamp(Post.Columns.UPDATED_AT);

    Post post = new Post();
    post.setId(id);
    post.setAuthor(authorId);
    post.setTextContent(textContent);
    post.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
    post.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

    return post;
  }
}

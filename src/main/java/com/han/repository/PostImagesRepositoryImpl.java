package com.han.repository;

import com.han.constants.tablesColumns.TableColumnsPostImages;
import com.han.model.PostImage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PostImagesRepositoryImpl implements PostImagesRepository {

  private DataSource dataSource;

  @Autowired
  public PostImagesRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public boolean insert(PostImage image) throws SQLException {
    String insertQuery = "INSERT INTO "
            + TableColumnsPostImages.TABLE.getName()
            + " VALUES (?, ?)";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
        statement.setInt(1, image.getPostId());
        statement.setString(2, image.getUrl());
        result = statement.executeUpdate();
      }
    }

    return result > 0;
  }

  @Override
  public List<PostImage> findByPostId(int postId) throws SQLException {
    String selectSql = "SELECT * FROM "
            + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.POST_ID.getName() + " = ?";

    List<PostImage> imageList = new LinkedList<>();

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(selectSql)) {
        statement.setInt(1, postId);

        try (ResultSet rs = statement.executeQuery()) {
          while (rs.next()) {
            imageList.add(resultSetToPostImage(rs));
          }
        }

      }
    }

    return imageList;
  }

  @Override
  public Optional<PostImage> findById(int id) throws SQLException {
    String selectSql = "SELECT * FROM "
            + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.ID.getName() + " = ?";

    PostImage image = null;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(selectSql)) {
        statement.setInt(1, id);

        try (ResultSet rs = statement.executeQuery()) {
          while (rs.next()) {
            image = resultSetToPostImage(rs);
          }
        }
        ;
      }
    }


    return Optional.ofNullable(image);
  }

  private PostImage resultSetToPostImage(ResultSet rs) throws SQLException {
    int id = rs.getInt(TableColumnsPostImages.ID.getName());
    int postId = rs.getInt(TableColumnsPostImages.POST_ID.getName());
    String url = rs.getString(TableColumnsPostImages.URL.getName());

    PostImage image = new PostImage(id, postId, url);
    return image;
  }
}

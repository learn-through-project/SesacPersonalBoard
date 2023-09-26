package com.han.repository;

import com.han.constants.tablesColumns.TableColumnsPostImages;
import com.han.model.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostImageRepositoryImpl implements PostImageRepository {

  private DataSource dataSource;

  @Autowired
  public PostImageRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public boolean deleteById(int imageId) throws SQLException {
    String deleteQuery = "DELETE FROM " + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.ID.getName() + " = ? ";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
        statement.setInt(1, imageId);

        result = statement.executeUpdate();
      }
    }

    return result > 0;
  }

  @Override
  public boolean update(PostImage image) throws SQLException {
    String updateQuery = "UPDATE " + TableColumnsPostImages.TABLE.getName() + " SET "
            + TableColumnsPostImages.POST_ID.getName() + " = ? , "
            + TableColumnsPostImages.URL.getName() + " = ? "
            + " WHERE " + TableColumnsPostImages.ID.getName() + " = ? ";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
        statement.setInt(1, image.getPostId());
        statement.setString(2, image.getUrl());
        statement.setInt(3, image.getId());
        result = statement.executeUpdate();
      }
    }

    return result > 0;
  }

  @Override
  public boolean insert(PostImage image) throws SQLException {
    String insertQuery = "INSERT INTO "
            + TableColumnsPostImages.TABLE.getName() + " ( "
            + TableColumnsPostImages.POST_ID + ", "
            + TableColumnsPostImages.URL + ","
            + TableColumnsPostImages.IMAGE_ORDER + " ) "
            + " VALUES (?, ?, ?)";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
        statement.setInt(1, image.getPostId());
        statement.setString(2, image.getUrl());
        statement.setInt(3, image.getImageOrder());
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
    int imageOrder = rs.getInt(TableColumnsPostImages.IMAGE_ORDER.getName());

    PostImage image = new PostImage(id, postId, url, imageOrder);
    return image;
  }
}

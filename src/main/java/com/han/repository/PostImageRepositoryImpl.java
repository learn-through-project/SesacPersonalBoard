package com.han.repository;

import com.han.constants.tablesColumns.TableColumnsPostImages;
import com.han.model.PostImage;
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
public class PostImageRepositoryImpl implements PostImageRepository {

  private DataSource dataSource;

  @Autowired
  public PostImageRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public int count (Integer postId) throws SQLException {
    String countSql = "SELECT COUNT(*) FROM "
            + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.POST_ID.getName() + " =?";

    int count = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(countSql)) {
        statement.setInt(1, postId);
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
  public boolean upsertBulk(Integer postId, List<PostImage> images) throws SQLException {
    String selectSql = "SELECT * FROM "
            + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.POST_ID.getName() + " = ?"
            + " AND " + TableColumnsPostImages.IMAGE_ORDER.getName() + " = ?";

    String updateQuery = "UPDATE " + TableColumnsPostImages.TABLE.getName() + " SET "
            + TableColumnsPostImages.POST_ID.getName() + " = ? , "
            + TableColumnsPostImages.URL.getName() + " = ? , "
            + TableColumnsPostImages.IMAGE_ORDER.getName() + " = ? "
            + " WHERE " + TableColumnsPostImages.ID.getName() + " = ? ";

    String insertQuery = "INSERT INTO "
            + TableColumnsPostImages.TABLE.getName() + " ( "
            + TableColumnsPostImages.POST_ID.getName() + ", "
            + TableColumnsPostImages.URL.getName() + ", "
            + TableColumnsPostImages.IMAGE_ORDER.getName() + " ) "
            + " VALUES (?, ?, ?)";

    String deleteQuery = "DELETE FROM "
            + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.POST_ID.getName() + " = ?"
            + " AND " + TableColumnsPostImages.IMAGE_ORDER.getName() + " > "
            + Integer.toString(images.size() - 1);

    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);

      try {
        for (PostImage image: images) {
          PostImage selectedImage = null;

          try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, postId);
            selectStmt.setInt(2, image.getImageOrder());
            try (ResultSet rs = selectStmt.executeQuery()) {
              while (rs.next()) {
                log.info("exceute next");
                selectedImage = resultSetToPostImage(rs);
              }
            }
          }

          if (selectedImage != null) {
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
              updateStmt.setInt(1, image.getPostId());
              updateStmt.setString(2, image.getUrl());
              updateStmt.setInt(3, image.getImageOrder());
              updateStmt.setInt(4, selectedImage.getId());
              updateStmt.executeUpdate();
            }
          } else {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
              insertStmt.setInt(1, image.getPostId());
              insertStmt.setString(2, image.getUrl());
              insertStmt.setInt(3, image.getImageOrder());
              insertStmt.executeUpdate();
            }
          }
        }

        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
          deleteStmt.setInt(1, postId);
          deleteStmt.executeUpdate();
        }

        conn.commit();
      } catch (SQLException ex) {
        log.error("Error in upsertBulk: >>" + ex.getMessage());
        conn.rollback();
        throw ex;
      }
    }

    return true;
  }

  @Override
  public boolean deleteByPostId(Integer PostId) throws SQLException {
    String deleteQuery = "DELETE FROM " + TableColumnsPostImages.TABLE.getName()
            + " WHERE " + TableColumnsPostImages.POST_ID.getName() + " = ? ";

    int result = 0;

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
        statement.setInt(1, PostId);

        result = statement.executeUpdate();
      }
    }

    return result > 0;
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

  public boolean insertBulk(List<PostImage> images) throws SQLException {
    String insertQuery = "INSERT INTO "
            + TableColumnsPostImages.TABLE.getName() + " ( "
            + TableColumnsPostImages.POST_ID + ", "
            + TableColumnsPostImages.URL + ", "
            + TableColumnsPostImages.IMAGE_ORDER + " ) "
            + " VALUES (?, ?, ?)";

    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
        for (PostImage image : images) {
          statement.setInt(1, image.getPostId());
          statement.setString(2, image.getUrl());
          statement.setInt(3, image.getImageOrder());

          statement.executeUpdate();
        }
        conn.commit();
      } catch (Exception ex) {
        log.error("Error in insertBulk Image: >>" + ex.getMessage());
        conn.rollback();
        throw ex;
      }
    }

    return true;
  }

  @Override
  public Integer insert(PostImage image) throws SQLException {
    String insertQuery = "INSERT INTO "
            + TableColumnsPostImages.TABLE.getName() + " ( "
            + TableColumnsPostImages.POST_ID + ", "
            + TableColumnsPostImages.URL + ","
            + TableColumnsPostImages.IMAGE_ORDER + " ) "
            + " VALUES (?, ?, ?)";

    Integer createdImageId = null;

    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement statement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
        statement.setInt(1, image.getPostId());
        statement.setString(2, image.getUrl());
        statement.setInt(3, image.getImageOrder());

        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            createdImageId = generatedKeys.getInt(1);
          }
        }

        conn.commit();
      } catch (Exception ex) {
        conn.rollback();
        throw ex;
      }
    }

    return createdImageId;
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

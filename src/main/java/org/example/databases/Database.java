package org.example.databases;

import org.example.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:1234/mrp_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private static Connection conn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ================= USERS =================

    public static void createUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users(username, password) VALUES (?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }
    }

    public static int login(String username, String password) throws SQLException {
        String sql = "SELECT id FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Invalid login");
            return rs.getInt("id");
        }
    }

    public static User getUser(int id) {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT id, username FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(rs.getInt("id"), rs.getString("username"));
        } catch (Exception e) {
            return null;
        }
    }

    // ================= MEDIA =================

    public static void createMedia(Media m, int ownerId) {
        String sql = """
            INSERT INTO media(title, description, genre, media_type, age_restriction, owner_id)
            VALUES (?,?,?,?,?,?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getDescription());
            ps.setString(3, m.getGenre());
            ps.setString(4, m.getType());
            ps.setInt(5, m.getAgeRestriction());
            ps.setInt(6, ownerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create media", e);
        }
    }

    public static List<Media> getAllMedia() {
        List<Media> list = new ArrayList<>();
        try (Statement st = conn().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM media");
            while (rs.next()) {
                list.add(new Media(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("genre"),
                        rs.getString("media_type"),
                        rs.getInt("age_restriction"),
                        rs.getInt("owner_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch media", e);
        }
        return list;
    }

    public static void updateMedia(int id, Media m, int userId) {
        String sql = """
            UPDATE media
            SET title=?, description=?, genre=?, media_type=?, age_restriction=?
            WHERE id=? AND owner_id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getDescription());
            ps.setString(3, m.getGenre());
            ps.setString(4, m.getType());
            ps.setInt(5, m.getAgeRestriction());
            ps.setInt(6, id);
            ps.setInt(7, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update media", e);
        }
    }

    public static void deleteMedia(int id, int userId) {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM media WHERE id=? AND owner_id=?")) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete media", e);
        }
    }

    // ================= RATINGS =================

    public static void upsertRating(int userId, Rating rating) {
        String sql = """
        INSERT INTO ratings(user_id, media_id, rating, comment)
        VALUES (?,?,?,?)
        ON CONFLICT (user_id, media_id)
        DO UPDATE SET rating = EXCLUDED.rating,
                      comment = EXCLUDED.comment
    """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, rating.getMediaId());
            ps.setInt(3, rating.getRating());
            ps.setString(4, rating.getComment());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to upsert rating", e);
        }
    }

    public static List<Rating> getRatingsForMedia(int mediaId) {
        List<Rating> list = new ArrayList<>();

        String sql = "SELECT id, media_id, rating, comment FROM ratings WHERE media_id=?";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, mediaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Rating r = new Rating();

                set(r, "id", rs.getInt("id"));
                set(r, "mediaId", rs.getInt("media_id"));
                set(r, "rating", rs.getInt("rating"));
                set(r, "comment", rs.getString("comment"));

                list.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch ratings", e);
        }

        return list;
    }

    public static List<Rating> getRatingsByUser(int userId) {
        List<Rating> list = new ArrayList<>();

        String sql = """
        SELECT id, media_id, rating, comment
        FROM ratings
        WHERE user_id=?
        ORDER BY id DESC
    """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Rating r = new Rating();
                set(r, "id", rs.getInt("id"));
                set(r, "mediaId", rs.getInt("media_id"));
                set(r, "userId", userId);
                set(r, "rating", rs.getInt("rating"));
                set(r, "comment", rs.getString("comment"));
                list.add(r);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ratings by user", e);
        }

        return list;
    }



    private static void set(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Used by RatingService.delete(...) */
    public static void deleteRating(int ratingId, int userId) {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM ratings WHERE id=? AND user_id=?")) {
            ps.setInt(1, ratingId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete rating", e);
        }
    }

    public static int countRatingsByUser(int userId) {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT COUNT(*) FROM ratings WHERE user_id=?")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ================= FAVORITES =================

    public static void addFavorite(int userId, int mediaId) {
        String sql = "INSERT INTO favorites(user_id, media_id) VALUES (?,?) ON CONFLICT DO NOTHING";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, mediaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add favorite", e);
        }
    }

    public static void removeFavorite(int userId, int mediaId) {
        String sql = "DELETE FROM favorites WHERE user_id=? AND media_id=?";

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, mediaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove favorite", e);
        }
    }

    public static List<Media> getFavorites(int userId) {
        List<Media> list = new ArrayList<>();

        String sql = """
        SELECT m.*
        FROM media m
        JOIN favorites f ON f.media_id = m.id
        WHERE f.user_id=?
    """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Media(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("genre"),
                        rs.getString("media_type"),
                        rs.getInt("age_restriction"),
                        rs.getInt("owner_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch favorites", e);
        }

        return list;
    }

    public static int countFavoritesByUser(int userId) {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT COUNT(*) FROM favorites WHERE user_id=?")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

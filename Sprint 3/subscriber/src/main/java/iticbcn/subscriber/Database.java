package iticbcn.subscriber;

import java.sql.*;

public class Database {

  private static Database instance;
  private Connection connection;

  private static final String URL = "jdbc:mariadb://192.168.13.2:3306/proj";
  private static final String USER = "java_remote_client";
  private static final String PASSWORD = "system";
  
  private Database() throws SQLException {
    try {
      connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Error al conectar a la base de datos: " + e.getMessage());
    }
  }

  public static Database getInstance() throws SQLException {
    if (instance == null) {
      synchronized (Database.class) {
        if (instance == null) {
          instance = new Database();
        }
      }
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  // Método para obtener la fecha actual desde la base de datos
  public String getCurrentDateAsString() {
    String query = "SELECT NOW() AS 'current_date'";
    try (Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(query)) {
      if (rs.next()) {
        return rs.getString("current_date");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Método para verificar si una habitación existe
  public boolean checkRoomExists(String roomCode) {
    String query = "SELECT COUNT(*) AS 'room_count' FROM room WHERE code = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, roomCode);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("room_count") > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  // Método para obtener el user_id a partir de un RFID
  public String getUserIdFromRFID(String rfid) {
    String query = "SELECT id FROM user WHERE rfid_uid = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, rfid);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Método para insertar un registro en la tabla checkin
  public boolean insertCheckin(int userId, String roomCode) {
    String query = "INSERT INTO checkin (user_id, room_code, checkin_time) VALUES (?, ?, NOW())";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setInt(1, userId);
      pstmt.setString(2, roomCode);
      int rowsInserted = pstmt.executeUpdate();
      connection.commit();
      return rowsInserted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean canUserAccessRoom(int userId, String roomCode) {
    String query = """
      SELECT COUNT(*) AS 'valid_schedule'
      FROM schedule s
      INNER JOIN grouped_users gu ON s.group_name = gu.group_name
      WHERE gu.user_id = ? 
        AND s.room_code = ? 
        AND s.week_day = DAYNAME(NOW()) 
        AND TIME(NOW()) BETWEEN s.start_time AND s.end_time
    """;

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setInt(1, userId);
      pstmt.setString(2, roomCode);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("valid_schedule") > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

}


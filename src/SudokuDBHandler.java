import java.io.Reader;
import java.sql.*;
import java.io.StringReader;

public class SudokuDBHandler {

    private static final String DB_URL = "jdbc:derby:SudokuDB;create=true";
    private static final String TABLE_NAME = "GAMES";

    public static void saveGameToDatabase(String gameName, String xmlContent) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            String sql = "INSERT INTO " + TABLE_NAME + " (NAME, GAME_DATA) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameName);
            pstmt.setCharacterStream(2, new StringReader(xmlContent), xmlContent.length());
            pstmt.executeUpdate();

            System.out.println("Spiel '" + gameName + "' wurde erfolgreich gespeichert.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Speichern in die Datenbank.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String loadGameFromDatabase(String gameName) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder xmlBuilder = new StringBuilder();

        try {
            conn = DriverManager.getConnection(DB_URL);
            String sql = "SELECT GAME_DATA FROM " + TABLE_NAME + " WHERE NAME = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Reader reader = rs.getCharacterStream("GAME_DATA");
                char[] buffer = new char[1024];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    xmlBuilder.append(buffer, 0, bytesRead);
                }
                return xmlBuilder.toString();
            } else {
                return null; // Kein Spiel gefunden
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Fehler beim Laden aus der Datenbank: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

}

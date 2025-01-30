package courses;

import java.sql.*;

public class enrollment {
    private int userId;
    private int courseId;

    public Enrollment(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public static void enroll(int userId, int courseId, Connection conn) {
        try {
            String query = "INSERT INTO enrollments (user_id, course_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

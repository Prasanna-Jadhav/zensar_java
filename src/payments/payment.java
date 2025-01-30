package payments;

import java.sql.*;

public class payment {
    private int userId;
    private int courseId;
    private double amount;

    public Payment(int userId, int courseId, double amount) {
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
    }

    public static boolean makePayment(int userId, int courseId, double amount, Connection conn) {
        try {
            String query = "INSERT INTO payments (user_id, course_id, amount) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getAmount() {
        return amount;
    }
}

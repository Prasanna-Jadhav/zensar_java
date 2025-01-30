package courses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private int id;
    private String title;
    private String description;
    private String instructor;
    private double price;
    private String duration;

    public Course(int id, String title, String description, String instructor, double price, String duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.price = price;
        this.duration = duration;
    }

    public static List<Course> getAllCourses(Connection conn) {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT * FROM courses";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("instructor"),
                        rs.getDouble("price"),
                        rs.getString("duration")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void addCourse(String title, String description, String instructor, double price, String duration, Connection conn) {
        try {
            String query = "INSERT INTO courses (title, description, instructor, price, duration) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, instructor);
            stmt.setDouble(4, price);
            stmt.setString(5, duration);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDuration() {
        return duration;
    }
}

package App; 
import courses.Course;
import  courses.enrollment;
import payments.payment;
import users.user;



import java.sql.*;
import java.util.Scanner;

public class main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/udemy_clone";
    private static final String DB_USER = "root"; // Change if needed
    private static final String DB_PASSWORD = "qwer1234"; // Change if needed
    private static Connection conn;
    private static user loggedInUser;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n===== Udemy Clone Platform =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. View All Courses");
                System.out.println("4. Add Course (Admin Only)");
                System.out.println("5. Enroll in Course (with Payment)");
                System.out.println("6. View Enrolled Courses");
                System.out.println("7. Logout");
                System.out.println("8. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> registerUser(scanner);
                    case 2 -> loginUser(scanner);
                    case 3 -> viewAllCourses();
                    case 4 -> {
                        if (loggedInUser != null && isAdmin(loggedInUser)) {
                            addCourse(scanner);
                        } else {
                            System.out.println("Only admins can add courses!");
                        }
                    }
                    case 5 -> {
                        if (loggedInUser != null) enrollWithPayment(scanner);
                        else System.out.println("You must log in first!");
                    }
                    case 6 -> {
                        if (loggedInUser != null) viewEnrolledCourses();
                        else System.out.println("You must log in first!");
                    }
                    case 7 -> logout();
                    case 8 -> {
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    }
                    default -> System.out.println("Invalid option! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isAdmin(user user) {
        return user.getEmail().equals("admin@example.com");
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (user.register(name, email, password, conn)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        loggedInUser = user.login(email, password, conn);
        if (loggedInUser != null) {
            System.out.println("Login successful! Welcome, " + loggedInUser.getEmail());
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void viewAllCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course course : Course.getAllCourses(conn)) {
            System.out.println(course.getTitle() + " | Instructor: " + course.getInstructor() + " | Price: $" + course.getPrice());
        }
    }

    private static void addCourse(Scanner scanner) {
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        System.out.print("Enter course description: ");
        String description = scanner.nextLine();
        System.out.print("Enter instructor name: ");
        String instructor = scanner.nextLine();
        System.out.print("Enter course price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Consume leftover newline
        System.out.print("Enter course duration: ");
        String duration = scanner.nextLine();

        Course.addCourse(title, description, instructor, price, duration, conn);
        System.out.println("Course added successfully!");
    }

    private static void enrollWithPayment(Scanner scanner) {
        System.out.print("Enter Course ID to enroll: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        for (Course course : Course.getAllCourses(conn)) {
            if (courseId == course.getId()) {
                payment.makePayment(loggedInUser.getId(), courseId, course.getPrice(), conn);
                enrollment.enroll(loggedInUser.getId(), courseId, conn);
                System.out.println("Payment successful, enrolled in course: " + course.getTitle());
                return;
            }
        }

        System.out.println("Invalid course ID.");
    }

    private static void viewEnrolledCourses() {
        try {
            String query = "SELECT c.title FROM enrollments e JOIN courses c ON e.course_id = c.id WHERE e.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUser.getId());
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nYour Enrolled Courses:");
            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void logout() {
        loggedInUser = null;
        System.out.println("You have logged out.");
    }
}


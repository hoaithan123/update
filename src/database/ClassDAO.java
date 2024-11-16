package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    public static class ClassModel {
        private int class_id;
        private String class_name;
        private int teacher_id;

        public ClassModel(int classId, String className, int teacherId) {
            this.class_id = classId;
            this.class_name = className;
            this.teacher_id = teacherId;
        }

        public int getClassId() { return class_id; }
        public String getClassName() { return class_name; }
        public int getTeacherId() { return teacher_id; }
    }

    /**
     * Thêm một lớp học mới
     */
    public boolean addClass(String className, int teacherId) {
        String query = "INSERT INTO classes (class_name, teacher_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, className);
            stmt.setInt(2, teacherId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách tất cả các lớp học
     */
    public List<ClassModel> getAllClasses() {
        List<ClassModel> classes = new ArrayList<>();
        String query = "SELECT * FROM classes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                classes.add(new ClassModel(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getInt("teacher_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * Lấy danh sách tên tất cả các lớp học
     */
    public List<String> getClassNames() {
        List<String> classNames = new ArrayList<>();
        String query = "SELECT class_name FROM classes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                classNames.add(rs.getString("class_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classNames;
    }
}

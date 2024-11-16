package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Thêm sinh viên mới vào cơ sở dữ liệu
    public boolean addStudent(Student student) {
        String query = "INSERT INTO students (student_code, student_name, student_email, attendance_count, address, phone_number) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getStudentName());
            stmt.setString(3, student.getStudentEmail());
            stmt.setInt(4, student.getAttendanceCount());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhoneNumber());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả sinh viên
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_code"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getInt("attendance_count"),
                        rs.getString("address"),
                        rs.getString("phone_number")
                ));
            }
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
        }
        return students;
    }

    // Tìm kiếm sinh viên theo tên
    public List<Student> searchStudentByName(String name) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE student_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_code"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getInt("attendance_count"),
                        rs.getString("address"),
                        rs.getString("phone_number")
                ));
            }
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
        }
        return students;
    }

    // Cập nhật thông tin sinh viên
    public boolean updateStudentInfo(Student student) {
        String query = "UPDATE students SET student_name = ?, student_email = ?, address = ?, phone_number = ? " +
                "WHERE student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getStudentName());
            stmt.setString(2, student.getStudentEmail());
            stmt.setString(3, student.getAddress());
            stmt.setString(4, student.getPhoneNumber());
            stmt.setString(5, student.getStudentCode());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra sự tồn tại của sinh viên theo mã số sinh viên
    public boolean isStudentExists(String studentCode) {
        String query = "SELECT COUNT(*) FROM students WHERE student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
        }
        return false;
    }

    // Xóa sinh viên theo mã số sinh viên
    public boolean deleteStudent(String studentCode) {
        String query = "DELETE FROM students WHERE student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) { // Bắt ngoại lệ tổng quát
            e.printStackTrace();
            return false;
        }
    }
}

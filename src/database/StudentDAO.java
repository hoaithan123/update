package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public String getStudentNameByCode(String studentCode) {
        String student_name = null;
        try {
            // Lấy kết nối cơ sở dữ liệu từ DBConnection
            Connection connection = DBConnection.getConnection(); // Đây là nơi bạn lấy kết nối CSDL

            // Truy vấn tên sinh viên theo mã số
            String query = "SELECT student_name FROM students WHERE student_code = ?";  // Chỉnh sửa ở đây
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                student_name = resultSet.getString("student_name"); // Chỉnh sửa ở đây
            }

            // Đóng kết nối sau khi sử dụng
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student_name;
    }



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

    // Kiểm tra xem sinh viên có điểm danh hay không
    private boolean isStudentHasAttendance(String studentCode) {
        String query = "SELECT COUNT(*) FROM attendance WHERE student_id = (SELECT student_id FROM students WHERE student_code = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Trả về true nếu có bản ghi điểm danh
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Không có bản ghi điểm danh
    }

    // Xóa các bản ghi điểm danh của sinh viên
    private boolean deleteAttendanceByStudentCode(String studentCode) {
        String query = "DELETE FROM attendance WHERE student_id = (SELECT student_id FROM students WHERE student_code = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);  // Set mã sinh viên vào câu lệnh SQL
            int rowsAffected = stmt.executeUpdate();  // Thực thi câu lệnh SQL
            return rowsAffected > 0;  // Trả về true nếu có ít nhất 1 bản ghi bị xóa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Trả về false nếu có lỗi xảy ra
        }
    }

    // Xóa sinh viên theo mã số sinh viên
    public boolean deleteStudent(String studentCode) {
        // Kiểm tra xem sinh viên có điểm danh không
        if (isStudentHasAttendance(studentCode)) {
            // Nếu có điểm danh, xóa các bản ghi điểm danh trước
            if (!deleteAttendanceByStudentCode(studentCode)) {
                System.out.println("Lỗi khi xóa điểm danh của sinh viên.");
                return false;
            }
        }

        // Xóa sinh viên
        String query = "DELETE FROM students WHERE student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

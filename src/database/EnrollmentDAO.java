package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    /**
     * Đăng ký sinh viên vào lớp và môn học (Teacher)
     */
    public boolean enrollStudent(int studentId, int classId, int subjectId) {
        String query = "INSERT INTO enrollments (student_id, class_id, subject_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, classId);
            stmt.setInt(3, subjectId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách lớp học mà sinh viên đã đăng ký (Student)
     */
    public List<String> getClassesByStudent(String studentCode) {
        List<String> classes = new ArrayList<>();
        String query = "SELECT c.class_name FROM enrollments e " +
                "JOIN classes c ON e.class_id = c.class_id " +
                "JOIN students s ON e.student_id = s.student_id " +
                "WHERE s.student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(rs.getString("class_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * Lấy danh sách môn học mà sinh viên đã đăng ký (Student)
     */
    public List<String> getSubjectsByStudent(String studentCode) {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT sub.subject_name FROM enrollments e " +
                "JOIN subjects sub ON e.subject_id = sub.subject_id " +
                "JOIN students st ON e.student_id = st.student_id " +
                "WHERE st.student_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getString("subject_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Lấy danh sách sinh viên trong một lớp và môn học (Teacher)
     */
    public List<String> getStudentsByClassAndSubject(int classId, int subjectId) {
        List<String> students = new ArrayList<>();
        String query = "SELECT s.student_code, s.student_name FROM enrollments e " +
                "JOIN students s ON e.student_id = s.student_id " +
                "WHERE e.class_id = ? AND e.subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentInfo = "MSSV: " + rs.getString("student_code") + ", Tên: " + rs.getString("student_name");
                students.add(studentInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Hủy đăng ký lớp học và môn học cho sinh viên (Teacher)
     */
    public boolean unenrollStudent(int studentId, int classId, int subjectId) {
        String query = "DELETE FROM enrollments WHERE student_id = ? AND class_id = ? AND subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, classId);
            stmt.setInt(3, subjectId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách môn học mà sinh viên đăng ký theo ID
     */
    public List<Integer> getEnrolledSubjects(int studentId) {
        List<Integer> subjects = new ArrayList<>();
        String query = "SELECT subject_id FROM enrollments WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getInt("subject_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }
}

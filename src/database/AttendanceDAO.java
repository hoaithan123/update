package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    /**
     * Điểm danh sinh viên theo mã sinh viên, lớp và môn học (Teacher)
     */
    public boolean markAttendance(String studentCode, int classId, int subjectId, String status) {
        String insertAttendance = "INSERT INTO attendance (student_id, class_id, subject_id, attendance_date, status) " +
                "SELECT student_id, ?, ?, CURDATE(), ? FROM students WHERE student_code = ?";
        String updateAttendanceCountPresent = "UPDATE students SET attendance_count = attendance_count + 1 WHERE student_code = ?";
        String updateAttendanceCountAbsent = "UPDATE students SET attendance_count = GREATEST(attendance_count - 1, 0) WHERE student_code = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Chèn bản ghi điểm danh vào bảng attendance
            try (PreparedStatement stmt = conn.prepareStatement(insertAttendance)) {
                stmt.setInt(1, classId);
                stmt.setInt(2, subjectId);
                stmt.setString(3, status);
                stmt.setString(4, studentCode);
                stmt.executeUpdate();
            }

            // Cập nhật attendance_count
            if ("Present".equalsIgnoreCase(status)) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateAttendanceCountPresent)) {
                    updateStmt.setString(1, studentCode);
                    updateStmt.executeUpdate();
                }
            } else if ("Absent".equalsIgnoreCase(status)) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateAttendanceCountAbsent)) {
                    updateStmt.setString(1, studentCode);
                    updateStmt.executeUpdate();
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy lịch sử điểm danh của sinh viên theo mã sinh viên (Student)
     */
    public List<String> getAttendanceHistoryByStudent(String studentCode) {
        List<String> history = new ArrayList<>();
        String query = "SELECT attendance_date, status, class_id, subject_id FROM attendance " +
                "JOIN students ON attendance.student_id = students.student_id " +
                "WHERE students.student_code = ? ORDER BY attendance_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                history.add("Sinh viên chưa điểm danh buổi nào.");
            } else {
                while (rs.next()) {
                    String date = rs.getString("attendance_date");
                    String status = rs.getString("status");
                    int classId = rs.getInt("class_id");
                    int subjectId = rs.getInt("subject_id");
                    history.add("Ngày: " + date + ", Trạng thái: " + status +
                            ", Lớp: " + classId + ", Môn học: " + subjectId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Lấy lịch sử điểm danh cho một lớp và môn học (Teacher)
     */
    public List<String> getAttendanceHistoryByClassAndSubject(int classId, int subjectId) {
        List<String> history = new ArrayList<>();
        String query = "SELECT students.student_code, students.student_name, attendance_date, status FROM attendance " +
                "JOIN students ON attendance.student_id = students.student_id " +
                "WHERE class_id = ? AND subject_id = ? ORDER BY attendance_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String studentCode = rs.getString("student_code");
                String studentName = rs.getString("student_name");
                String date = rs.getString("attendance_date");
                String status = rs.getString("status");
                history.add("MSSV: " + studentCode + ", Tên: " + studentName +
                        ", Ngày: " + date + ", Trạng thái: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Lấy lịch sử điểm danh cho một sinh viên theo lớp và môn học cụ thể (Student)
     */
    public List<String> getAttendanceHistoryByStudentAndSubject(String studentCode, int classId, int subjectId) {
        List<String> history = new ArrayList<>();
        String query = "SELECT attendance_date, status FROM attendance " +
                "JOIN students ON attendance.student_id = students.student_id " +
                "WHERE students.student_code = ? AND class_id = ? AND subject_id = ? " +
                "ORDER BY attendance_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentCode);
            stmt.setInt(2, classId);
            stmt.setInt(3, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("attendance_date");
                String status = rs.getString("status");
                history.add("Ngày: " + date + ", Trạng thái: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Cập nhật trạng thái điểm danh cho một sinh viên (Teacher)
     */
    public boolean updateAttendanceStatus(int attendanceId, String newStatus) {
        String query = "UPDATE attendance SET status = ? WHERE attendance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa bản ghi điểm danh (Teacher)
     */
    public boolean deleteAttendanceRecord(int attendanceId) {
        String query = "DELETE FROM attendance WHERE attendance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

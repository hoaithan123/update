package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public static class SubjectModel {
        private int subject_id;
        private String subject_name;
        private int class_id;

        public SubjectModel(int subjectId, String subjectName, int classId) {
            this.subject_id = subjectId;
            this.subject_name = subjectName;
            this.class_id = classId;
        }

        public int getSubjectId() { return subject_id; }
        public String getSubjectName() { return subject_name; }
        public int getClassId() { return class_id; }
    }

    /**
     * Thêm một môn học mới
     */
    public boolean addSubject(String subjectName, int classId) {
        String query = "INSERT INTO subjects (subject_name, class_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectName);
            stmt.setInt(2, classId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách các môn học theo lớp
     */
    public List<SubjectModel> getSubjectsByClass(int classId) {
        List<SubjectModel> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects WHERE class_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(new SubjectModel(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        rs.getInt("class_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }
}

package database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewAttendanceGUI extends JFrame {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private JTextArea textArea;

    public ViewAttendanceGUI() {
        setTitle("Xem lịch sử điểm danh");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Làm mới");
        refreshButton.addActionListener(e -> loadAttendanceHistory());
        panel.add(refreshButton, BorderLayout.SOUTH);

        add(panel);
        loadAttendanceHistory();
        setVisible(true);
    }

    private void loadAttendanceHistory() {
        textArea.setText("");
        List<String> history = attendanceDAO.getAttendanceHistoryByStudent("student_code"); // Thay "student_code" bằng mã cần thiết
        for (String record : history) {
            textArea.append(record + "\n");
        }
    }
}

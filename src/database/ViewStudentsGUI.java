package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewStudentsGUI extends JFrame {
    private StudentDAO studentDAO = new StudentDAO();

    public ViewStudentsGUI() {
        setTitle("Danh sách sinh viên");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Mã sinh viên", "Tên sinh viên", "Email", "Số điện thoại", "Địa chỉ"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            Object[] rowData = {
                    student.getStudentCode(),
                    student.getStudentName(),
                    student.getStudentEmail(),
                    student.getPhoneNumber(),
                    student.getAddress()
            };
            tableModel.addRow(rowData);
        }

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}

package database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchStudentGUI extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private StudentDAO studentDAO = new StudentDAO();

    public SearchStudentGUI() {
        setTitle("Tìm kiếm sinh viên");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        searchField = new JTextField();
        resultArea = new JTextArea();

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> searchStudent());

        add(searchField, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void searchStudent() {
        String name = searchField.getText();
        List<Student> students = studentDAO.searchStudentByName(name);
        resultArea.setText("");
        for (Student student : students) {
            resultArea.append(student.toString() + "\n");
        }
    }


}

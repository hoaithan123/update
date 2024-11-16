package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddStudentGUI extends JFrame {
    private JTextField studentCodeField, studentNameField, emailField, addressField, phoneField;
    private StudentDAO studentDAO;

    public AddStudentGUI() {
        studentDAO = new StudentDAO();

        setTitle("Thêm sinh viên");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Mã số sinh viên:"));
        studentCodeField = new JTextField();
        panel.add(studentCodeField);

        panel.add(new JLabel("Tên sinh viên:"));
        studentNameField = new JTextField();
        panel.add(studentNameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Địa chỉ:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        panel.add(addButton);
        add(panel);
        setVisible(true);
    }

    private void addStudent() {
        String studentCode = studentCodeField.getText();
        String studentName = studentNameField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        Student student = new Student(0, studentCode, studentName, email, 0, address, phone);
        if (studentDAO.addStudent(student)) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại!");
        }
    }
}

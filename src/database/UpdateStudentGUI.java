package database;

import javax.swing.*;

public class UpdateStudentGUI extends JFrame {
    private JTextField studentCodeField, nameField, emailField, addressField, phoneField;
    private StudentDAO studentDAO = new StudentDAO();

    public UpdateStudentGUI() {
        setTitle("Cập nhật thông tin sinh viên");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Mã sinh viên:"));
        studentCodeField = new JTextField();
        panel.add(studentCodeField);

        panel.add(new JLabel("Tên mới:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email mới:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Địa chỉ mới:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Số điện thoại mới:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> updateStudent());
        panel.add(updateButton);

        add(panel);
        setVisible(true);
    }

    private void updateStudent() {
        String studentCode = studentCodeField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        Student student = new Student(0, studentCode, name, email, 0, address, phone);
        if (studentDAO.updateStudentInfo(student)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
}

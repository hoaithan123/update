package database;

import javax.swing.*;

public class AddClassGUI extends JFrame {
    private JTextField classNameField, teacherIdField;
    private ClassDAO classDAO = new ClassDAO();

    public AddClassGUI() {
        setTitle("Thêm lớp học");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Tên lớp:"));
        classNameField = new JTextField();
        panel.add(classNameField);

        panel.add(new JLabel("Mã giáo viên:"));
        teacherIdField = new JTextField();
        panel.add(teacherIdField);

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addClass());
        panel.add(addButton);

        add(panel);
        setVisible(true);
    }

    private void addClass() {
        String className = classNameField.getText();
        int teacherId = Integer.parseInt(teacherIdField.getText());

        if (classDAO.addClass(className, teacherId)) {
            JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm lớp học thất bại!");
        }
    }
}

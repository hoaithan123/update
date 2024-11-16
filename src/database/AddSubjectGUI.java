package database;

import javax.swing.*;

public class AddSubjectGUI extends JFrame {
    private JTextField subjectNameField, classIdField;
    private SubjectDAO subjectDAO = new SubjectDAO();

    public AddSubjectGUI() {
        setTitle("Thêm môn học");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Tên môn học:"));
        subjectNameField = new JTextField();
        panel.add(subjectNameField);

        panel.add(new JLabel("Mã lớp:"));
        classIdField = new JTextField();
        panel.add(classIdField);

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addSubject());
        panel.add(addButton);

        add(panel);
        setVisible(true);
    }

    private void addSubject() {
        String subjectName = subjectNameField.getText();
        int classId = Integer.parseInt(classIdField.getText());

        if (subjectDAO.addSubject(subjectName, classId)) {
            JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm môn học thất bại!");
        }
    }
}

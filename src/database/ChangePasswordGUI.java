package database;

import javax.swing.*;

public class ChangePasswordGUI extends JFrame {
    private JTextField oldPasswordField, newPasswordField;
    private UserDAO userDAO = new UserDAO();
    private String username;

    public ChangePasswordGUI(String username) {
        this.username = username;
        setTitle("Đổi mật khẩu");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Mật khẩu cũ:"));
        oldPasswordField = new JTextField();
        panel.add(oldPasswordField);

        panel.add(new JLabel("Mật khẩu mới:"));
        newPasswordField = new JTextField();
        panel.add(newPasswordField);

        JButton changeButton = new JButton("Đổi mật khẩu");
        changeButton.addActionListener(e -> changePassword());
        panel.add(changeButton);

        add(panel);
        setVisible(true);
    }

    private void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (userDAO.changePassword(username, oldPassword, newPassword)) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!");
        }
    }
}

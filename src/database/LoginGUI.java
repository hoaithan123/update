package database;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private UserDAO userDAO;

    public LoginGUI() {
        userDAO = new UserDAO();

        setTitle("Đăng nhập hệ thống");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Tên đăng nhập:");
        usernameField = new JTextField(20);
        JLabel passLabel = new JLabel("Mật khẩu:");
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Đăng nhập");
        statusLabel = new JLabel("", SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loginButton, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(statusLabel, gbc);

        loginButton.addActionListener(e -> authenticate());

        add(panel);
        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Vui lòng điền đầy đủ thông tin đăng nhập!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (userDAO.loginUser(username, password)) {
            String role = userDAO.getUserRole(username);
            if ("teacher".equalsIgnoreCase(role)) {
                statusLabel.setText("Đăng nhập thành công (Giáo viên)!");
                statusLabel.setForeground(new Color(0x28a745)); // Màu xanh lá cây
                new DashboardGUI(username, role);
            } else if ("student".equalsIgnoreCase(role)) {
                statusLabel.setText("Đăng nhập thành công (Sinh viên)!");
                statusLabel.setForeground(new Color(0x28a745)); // Màu xanh lá cây
                new DashboardGUI(username, role);
            } else {
                statusLabel.setText("Vai trò không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            }
            dispose();
        } else {
            statusLabel.setText("Sai tên đăng nhập hoặc mật khẩu!");
            statusLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}

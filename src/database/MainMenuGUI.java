package database;

import javax.swing.*;

public class MainMenuGUI extends JFrame {
    public MainMenuGUI() {
        setTitle("Main Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Main Menu", SwingConstants.CENTER);
        add(welcomeLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuGUI::new);
    }
}

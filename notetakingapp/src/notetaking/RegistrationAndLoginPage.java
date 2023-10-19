package notetaking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationAndLoginPage extends JFrame {
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel registrationPanel;
    private JPanel loginPanel;

    public RegistrationAndLoginPage() {
        setTitle("User Registration and Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        registrationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(150, 131, 236), getWidth(), getHeight(), new Color(107, 31, 135));
                ((Graphics2D) g).setPaint(gradient);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        registrationPanel.setLayout(new GridLayout(4, 2));

        JLabel regUsernameLabel = new JLabel("Username:");
        JLabel regPasswordLabel = new JLabel("Password");

        regUsernameLabel.setFont(labelFont);
        regPasswordLabel.setFont(labelFont);

        regUsernameField = new JTextField(20);
        regPasswordField = new JPasswordField(20);

        regUsernameField.setFont(fieldFont);
        regPasswordField.setFont(fieldFont);

        registerButton = new JButton("Register");
        loginButton = new JButton("Go to Login");
        cancelButton = new JButton("Cancel");

        registerButton.setFont(buttonFont);
        loginButton.setFont(buttonFont);
        cancelButton.setFont(buttonFont);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
                cardLayout.show(getContentPane(), "login");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        registrationPanel.add(regUsernameLabel);
        registrationPanel.add(regUsernameField);
        registrationPanel.add(regPasswordLabel);
        registrationPanel.add(regPasswordField);
        registrationPanel.add(registerButton);
        registrationPanel.add(loginButton);
        registrationPanel.add(cancelButton);
        add(registrationPanel, "register");

        loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(150, 131, 236), getWidth(), getHeight(), new Color(107, 31, 135));
                ((Graphics2D) g).setPaint(gradient);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        loginPanel.setLayout(new GridLayout(5, 2));

        JLabel loginUsernameLabel = new JLabel("Username:");
        JLabel loginPasswordLabel = new JLabel("Password:");

        loginUsernameLabel.setFont(labelFont);
        loginPasswordLabel.setFont(labelFont);

        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);

        loginUsernameField.setFont(fieldFont);
        loginPasswordField.setFont(fieldFont);

        JButton loginUserButton = new JButton("Login");
        JButton backToRegisterButton = new JButton("Back to Registration");

        loginUserButton.setFont(buttonFont);
        backToRegisterButton.setFont(buttonFont);

        loginUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        backToRegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
                cardLayout.show(getContentPane(), "register");
            }
        });

        loginPanel.add(loginUsernameLabel);
        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginUserButton);
        loginPanel.add(backToRegisterButton);

        add(loginPanel, "login");
    }

    private void registerUser() {
        String username = regUsernameField.getText();
        char[] passwordChars = regPasswordField.getPassword();
        String password = new String(passwordChars);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/noteapp", "root", "");

            String insertQuery = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                
                MainMenu mainMenu = new MainMenu(username);
                mainMenu.setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void loginUser() {
        String username = loginUsernameField.getText();
        char[] passwordChars = loginPasswordField.getPassword();
        String password = new String(passwordChars);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/noteapp", "root", "");
            String selectQuery = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");

                // After successful login, open the MainMenu
                MainMenu mainMenu = new MainMenu(username);
                mainMenu.setVisible(true);
                dispose(); // Close the RegistrationAndLoginPage window
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Invalid username or password.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationAndLoginPage registrationAndLoginPage = new RegistrationAndLoginPage();
            registrationAndLoginPage.setVisible(true);
        });
    }
}

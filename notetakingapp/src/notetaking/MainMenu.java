package notetaking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private String username; 

    public MainMenu(String username) {
        this.username = username; 
        setTitle("Main Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(150, 131, 236);
                Color color2 = new Color(200, 162, 200);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton writeNotesButton = new JButton("Write Notes");
        JButton displayNotesButton = new JButton("Display Notes");
        JButton logoutButton = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        writeNotesButton.setFont(buttonFont);
        displayNotesButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);

        writeNotesButton.setPreferredSize(new Dimension(150, 40));
        displayNotesButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setPreferredSize(new Dimension(150, 40));

        writeNotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pass the username to WriteNotes
               new WriteNotes(username).setVisible(true);
            }
        });

        displayNotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pass the username to DisplayNotes
                new DisplayNotes(username).setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(writeNotesButton);
        buttonsPanel.add(displayNotesButton);

        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout());
        logoutPanel.add(logoutButton);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void logout() {
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            RegistrationAndLoginPage registrationAndLoginPage = new RegistrationAndLoginPage();
            registrationAndLoginPage.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu("YourUsername");
            mainMenu.setVisible(true);
        });
    }
}

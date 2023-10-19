package notetaking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteNotes extends JFrame {
    private String username; 

    public WriteNotes(String username) {
        this.username = username;
        setTitle("Write Notes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel writePanel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(150, 40));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String note = textArea.getText();
                saveNoteToDatabase(username, note);
            }
        });

        writePanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        writePanel.add(saveButton, BorderLayout.SOUTH);

        setContentPane(writePanel);
    }

    private void saveNoteToDatabase(String username, String note) {
        String url = "jdbc:mysql://localhost:3306/noteapp";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String insertQuery = "INSERT INTO note (username, content) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, note);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Note saved successfully!");
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save the note.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WriteNotes writeNotes = new WriteNotes("YourUsername");
            writeNotes.setVisible(true);
        });
    }
}

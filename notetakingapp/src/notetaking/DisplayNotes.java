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

public class DisplayNotes extends JFrame {
    private String username; // Store the username
    private JTextArea displayTextArea;

    public DisplayNotes(String username) {
        this.username = username; 
        setTitle("Display Notes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());

        displayTextArea = new JTextArea(10, 30);
        displayTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        displayTextArea.setWrapStyleWord(true);
        displayTextArea.setLineWrap(true);
        displayTextArea.setCaretPosition(0);
        displayTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayTextArea);

        JButton backButton = new JButton("Back");

        backButton.setPreferredSize(new Dimension(150, 40));

        backButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        dispose(); 
    }
});

        displayPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(backButton);

        displayPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(displayPanel);

        displayNotesFromDatabase();
    }

    private void displayNotesFromDatabase() {
        
        String url = "jdbc:mysql://localhost:3306/noteapp";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "SELECT content FROM note WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username); 
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder notesText = new StringBuilder();
            int noteNumber = 1;

            while (resultSet.next()) {
                String note = resultSet.getString("content");
                notesText.append(noteNumber++).append(". ").append(note).append("\n");
            }

            displayTextArea.setText(notesText.toString());

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            displayTextArea.setText("Failed to retrieve notes from the database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisplayNotes displayNotes = new DisplayNotes("YourUsername");
            displayNotes.setVisible(true);
        });
    }
}

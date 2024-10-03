import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegisterApp extends JFrame {
    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3008/car_rental";
    static final String USER = "root";
    static final String PASS = "tiger";

    // GUI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginRegisterApp() {
        setTitle("Car Rental System - Login & Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creating UI elements
        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Adding action listeners
        loginButton.addActionListener(new LoginHandler());
        registerButton.addActionListener(new RegisterHandler());

        // Add components to the frame
        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Action handler for Login
    class LoginHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateUser(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean validateUser(String username, String password) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                // Connecting to the database
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                // Query to check username and password
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                // If the user exists, return true
                if (rs.next()) {
                    return true;
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            return false;
        }
    }

    // Action handler for Register
    class RegisterHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new RegistrationForm();
        }
    }

    public static void main(String[] args) {
        // Load MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Create and display the form
        SwingUtilities.invokeLater(() -> new LoginRegisterApp());
    }
}

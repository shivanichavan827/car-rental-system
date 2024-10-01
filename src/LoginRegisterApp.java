import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegisterApp extends JFrame {
    
    static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    static final String USER = "root";
    static final String PASS = "password";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginRegisterApp() {
        setTitle("Car Rental System - Login & Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Login/Register", JLabel.CENTER);
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

        loginButton.addActionListener(new LoginHandler());
        registerButton.addActionListener(new RegisterHandler());

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

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
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

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

    class RegisterHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean registerUser(String username, String password) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
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

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginRegisterApp());
    }
}

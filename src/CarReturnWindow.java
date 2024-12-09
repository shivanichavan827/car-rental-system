import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;

public class CarReturnWindow extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";
    private JButton returnCarButton;
    
    public CarReturnWindow() {
        System.out.println("LINE 14 ");
        setTitle("Car Return");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        System.out.println("LINE 19 ");
        
        // Fetch and display car details
        fetchAndDisplayCarDetails();
        System.out.println("LINE 23 ");
    }
    
    public void fetchAndDisplayCarDetails() {
        System.out.println("LINE 27 ");
        // Database connection settings
    

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("LINE 32 ");
            // Step 1: Query CustomerCarMapping table to get car_id for user_id = 1
            String query = "SELECT car_id FROM CustomerCarMapping WHERE user_id = 1";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                // Step 2: For each car_id, get car details from the cars table
                while (rs.next()) {
                    String carId = rs.getString("car_id");
                    
                    // Query cars table for car details
                    String carDetailsQuery = "SELECT car_id, brand, model, price_per_day, date_added " +
                                             "FROM cars WHERE car_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(carDetailsQuery)) {
                        pstmt.setString(1, carId);
                        
                        try (ResultSet carDetailsRS = pstmt.executeQuery()) {
                            if (carDetailsRS.next()) {
                                String brand = carDetailsRS.getString("brand");
                                String model = carDetailsRS.getString("model");
                                BigDecimal pricePerDay = carDetailsRS.getBigDecimal("price_per_day");
                
                                Date dateAdded = carDetailsRS.getDate("date_added");

                                // Display car details with a "Return" button
                                JPanel carPanel = new JPanel();
                                carPanel.setLayout(new GridLayout(1, 6)); // for brand, model, and return button
                                
                                JLabel brandLabel = new JLabel(brand);
                                JLabel modelLabel = new JLabel(model);
                                JLabel priceLabel = new JLabel(pricePerDay.toString());
                                
                                JLabel dateLabel = new JLabel(dateAdded.toString());
                                
                                returnCarButton = new JButton("Return");
                                returnCarButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JOptionPane.showMessageDialog(null, brand + " " + model + " is returned.");
                                    }
                                });
                                System.out.println("LINE 72");
                                // Add the components to the panel
                                carPanel.add(brandLabel);
                                carPanel.add(modelLabel);
                                carPanel.add(priceLabel);
                               
                                carPanel.add(dateLabel);
                                carPanel.add(returnCarButton);

                                // Add the car panel to the frame
                                add(carPanel);
                                System.out.println("LINE 84");
                                
                            }
                        }
                    }
                }
            }
            
            // Refresh the layout
            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CarReturnWindow().setVisible(true);
        });
    }
}
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import javax.swing.*;

public class ConfirmationWindow extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";

    public ConfirmationWindow(String carId, String date, double pricePerDay) {
        System.out.println(date);
        double dynamic_price = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT cdemand FROM demandtable WHERE yeardate = '" + date + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                double carDemand = rs.getDouble("cdemand");
                dynamic_price = carDemand * 0.01 * pricePerDay;
                System.out.println(carDemand);
            }
            System.out.println(dynamic_price);

            System.out.print("Hello Confirmation");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Booking Confirmation");
        setSize(300, 200);
        setLayout(new BorderLayout());
        System.out.print("Here Coming");

        // Calculate price per time slot (assuming each slot is a 4-hour block)
        BigDecimal bd = new BigDecimal(dynamic_price);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // Limits to 2 decimal places
        double finalPrice = bd.doubleValue();

        JLabel infoLabel = new JLabel("Date: " + date);
        JLabel priceLabel = new JLabel("Final Price: $" + finalPrice);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(infoLabel, BorderLayout.NORTH);
        add(priceLabel, BorderLayout.CENTER);

        // Create Confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Booking confirmed", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add Confirm button to the bottom of the window
        add(confirmButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public double dynamicprice(String date) {
        return 0.000;
    }
}

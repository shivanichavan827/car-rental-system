import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RentCarPage extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";

    public RentCarPage() {
        setTitle("Available Cars");
        setSize(500, 400);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Available Cars", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel carListPanel = new JPanel();
        carListPanel.setLayout(new BoxLayout(carListPanel, BoxLayout.Y_AXIS));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM cars WHERE available = true";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                double pricePerDay = rs.getDouble("price_per_day");

                JPanel carPanel = new JPanel(new BorderLayout());
                String carInfo = "ID: " + carId + ", Brand: " + brand + ", Model: " + model + ", Price per day: $" + pricePerDay;
                JLabel carInfoLabel = new JLabel(carInfo);

                JButton bookButton = new JButton("Book the Car");
                bookButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new BookingWindow(carId, brand, model, pricePerDay);
                    }
                });

                carPanel.add(carInfoLabel, BorderLayout.CENTER);
                carPanel.add(bookButton, BorderLayout.EAST);
                carListPanel.add(carPanel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Failed to retrieve car data.");
            carListPanel.add(errorLabel);
        }

        add(new JScrollPane(carListPanel), BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new RentCarPage();
    }
}

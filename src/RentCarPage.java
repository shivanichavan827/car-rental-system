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

        JTextArea carDisplayArea = new JTextArea();
        carDisplayArea.setEditable(false);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM cars WHERE available = true";
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                double pricePerDay = rs.getDouble("price_per_day");
                //String features = rs.getString("features");

                sb.append("ID: ").append(carId)
                        .append(", Brand: ").append(brand)
                        .append(", Model: ").append(model)
                        .append(", Price per day: $").append(pricePerDay)
                        //.append(", Features: ").append(features)
                        .append("\n");
                        System.out.println(sb);

                JButton rentButton = new JButton("Rent " + brand + " " + model);
                rentButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new BookingWindow(carId, brand, model, pricePerDay);
                    }
                });
                carDisplayArea.add(rentButton);
            }
            carDisplayArea.setText(sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            carDisplayArea.setText("Failed to retrieve car data.");
        }

        add(new JScrollPane(carDisplayArea), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new RentCarPage();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class AvailableCarsPage extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";

    public AvailableCarsPage() {
        setTitle("Available Cars");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel carListPanel = new JPanel();
        carListPanel.setLayout(new GridLayout(0, 1));

        ArrayList<JButton> rentButtons = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM cars WHERE isAvailable = TRUE";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String carId = rs.getString("carId");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                double pricePerHour = rs.getDouble("pricePerHour");

                JPanel carPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                carPanel.add(new JLabel(carId + " - " + brand + " " + model + " ($" + pricePerHour + "/hr)"));

                JButton rentButton = new JButton("Rent");
                rentButton.addActionListener(new RentCarHandler(carId));
                carPanel.add(rentButton);

                carListPanel.add(carPanel);
                rentButtons.add(rentButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading available cars", "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(new JScrollPane(carListPanel), BorderLayout.CENTER);
        setVisible(true);
    }

    private class RentCarHandler implements ActionListener {
        private String carId;

        public RentCarHandler(String carId) {
            this.carId = carId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Code to rent the car based on carId
            JOptionPane.showMessageDialog(null, "Car " + carId + " rented successfully.");
        }
    }
}


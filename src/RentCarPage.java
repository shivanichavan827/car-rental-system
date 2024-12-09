import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RentCarPage extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";

    public RentCarPage() {
        setTitle("Available Cars");
        setSize(600, 500);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Available Cars", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel carListPanel = new JPanel();
        carListPanel.setLayout(new BoxLayout(carListPanel, BoxLayout.Y_AXIS));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT  * FROM cars WHERE available = true";
            ResultSet rs = stmt.executeQuery(sql);

            List<Car> carList = new ArrayList<>();

            // Collect cars into a list
            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                double pricePerDay = rs.getDouble("price_per_day");

                carList.add(new Car(carId, brand, model, pricePerDay));
            }

            for (Car car : carList) {
                JPanel carPanel = new JPanel(new BorderLayout());
                String carInfo = "ID: " + car.getCarId() + ", Brand: " + car.getBrand() + ", Model: " + car.getModel() + ", Price per day: $" + car.getPricePerDay();
                JLabel carInfoLabel = new JLabel(carInfo);

                JButton bookButton = new JButton("Book the Car");
                bookButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new BookingWindow(car.getCarId(), car.getBrand(), car.getModel(), car.getPricePerDay());

                        // Display alternative cars
                        //displayAlternativeCars(carList, car);
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
    

    /*public void displayAlternativeCars(List<Car> carList, Car selectedCar) {
        // Sort alternative cars by suitability (e.g., same brand, similar price)
        carList.sort(Comparator.comparingInt(car -> {
            int score = 0;
            if (car.getBrand().equalsIgnoreCase(selectedCar.getBrand())) {
                score -= 10; // Higher priority for same brand
            }
            double priceDifference = Math.abs(car.getPricePerDay() - selectedCar.getPricePerDay());
            score -= priceDifference; // Lower score for higher price difference
            return score*(-1);
        }));

        JFrame alternativesFrame = new JFrame("Alternative Cars");
        alternativesFrame.setSize(600, 500);
        JPanel alternativesPanel = new JPanel();
        alternativesPanel.setLayout(new BoxLayout(alternativesPanel, BoxLayout.Y_AXIS));
        int count = 0;
        for (Car car : carList) {
            if (count >= 5) break;
            if (!car.getCarId().equals(selectedCar.getCarId())) { // Exclude the selected car
                JPanel carPanel = new JPanel(new BorderLayout());
                String carInfo = "ID: " + car.getCarId() + ", Brand: " + car.getBrand() + ", Model: " + car.getModel() + ", Price per day: $" + car.getPricePerDay();
                JLabel carInfoLabel = new JLabel(carInfo);

                JButton bookButton = new JButton("Book this Car");
                bookButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new BookingWindow(car.getCarId(), car.getBrand(), car.getModel(), car.getPricePerDay());
                    }
                });

                carPanel.add(carInfoLabel, BorderLayout.CENTER);
                carPanel.add(bookButton, BorderLayout.EAST);
                alternativesPanel.add(carPanel);
                count++;
            }


        }

        alternativesFrame.add(new JScrollPane(alternativesPanel));
        alternativesFrame.setVisible(true);
    }
*/
    public static void main(String[] args) {
        new RentCarPage();
    }

    class Car {
        private String carId;
        private String brand;
        private String model;
        private double pricePerDay;

        public Car(String carId, String brand, String model, double pricePerDay) {
            this.carId = carId;
            this.brand = brand;
            this.model = model;
            this.pricePerDay = pricePerDay;
        }

        public String getCarId() {
            return carId;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public double getPricePerDay() {
            return pricePerDay;
        }
    }
}

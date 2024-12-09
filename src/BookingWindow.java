import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.List;
public class BookingWindow extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebs";
    static final String USER = "root";
    static final String PASS = "tiger";

    private String carId;
    private String brand;
    private String model;
    private double pricePerDay;
    private DatePicker datePicker;

    public BookingWindow(String carId, String brand, String model, double pricePerDay) {
        
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;

        setTitle("Book Car - " + brand + " " + model);
        setSize(400, 300);
        setLayout(new GridLayout(6, 1));

        JLabel dateLabel = new JLabel("Select Date:");
        add(dateLabel);

        // Initialize JFXPanel to allow embedding JavaFX components in Swing
        JFXPanel fxPanel = new JFXPanel();
        VBox vbox = new VBox();
        datePicker = new DatePicker();
        vbox.getChildren().add(datePicker);
        Scene scene = new Scene(vbox);
        fxPanel.setScene(scene);
        add(fxPanel);
//String sql = "SELECT date_added FROM cars WHERE car_id = ?";
        JButton submitButton = new JButton("Confirm Booking");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sql = "SELECT * FROM cars WHERE car_id = ?";
                Date rentalDate = null;
                Car selectCar=null;
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             //Statement stmt = conn.createStatement()) 
            
             PreparedStatement stmt = conn.prepareStatement(sql)) 
             {
                stmt.setString(1, carId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    double pricePerDay = rs.getDouble("price_per_day");
                    rentalDate = rs.getDate("date_added"); // Fetch the date value
                    System.out.println("Rental Date: " + rentalDate);
                    selectCar = new Car(carId, brand, model, pricePerDay);
                }
            //String sql = "SELECT date  FROM cars WHERE car_id = true";
            //stmt.setString(1, carId);
            
            //ResultSet rs = stmt.executeQuery(sql);
            }
            catch (SQLException ee) {
                ee.printStackTrace();
                JLabel errorLabel = new JLabel("Failed to retrieve car data.");
                //carListPanel.add(errorLabel);
            }
                LocalDate selectedLocalDate = datePicker.getValue();
                
                String s = selectedLocalDate.toString();
                String dbdate = rentalDate.toString(); //01-01-2000   ==  24-12-2024
                if(!s.equals(dbdate)){
                    new ConfirmationWindow(carId, s, pricePerDay);
                dispose();
                }
                else{
                    System.out.println("added selected date");   
                    RentCarPage rentCarPage = new RentCarPage();
                    JPanel carListPanel = new JPanel();
                    //rentCarPage.displayAlternativeCars(rentCarPage.carList, car);
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sqlq = "SELECT  * FROM cars WHERE available = true";
            ResultSet rs = stmt.executeQuery(sqlq);

            List<Car> carList = new ArrayList<>();

            // Collect cars into a list
            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                double pricePerDay = rs.getDouble("price_per_day");

                carList.add(new Car(carId, brand, model, pricePerDay));
                
            }

            
        displayAlternativeCars(carList,selectCar);
            

        } catch (SQLException eee) {
            eee.printStackTrace();
            JLabel errorLabel = new JLabel("Failed to retrieve car data.");
            carListPanel.add(errorLabel);
        }

                }    
            }
        });
        add(submitButton);

        // Disclaimer label
        JLabel disclaimerLabel = new JLabel("Rental price of the car is subject to change.");
        disclaimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(disclaimerLabel);

        setVisible(true);
        
    }
    
    public void displayAlternativeCars(List<Car> carList, Car selectedCar) {
        // Create a frame to prompt the user
        JFrame promptFrame = new JFrame("Car Unavailability Notification");
        promptFrame.setSize(600, 500);
        promptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel promptPanel = new JPanel();
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));
    
        JLabel promptLabel = new JLabel("The selected car is not available for the date. Please select an alternative car.");
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        promptPanel.add(promptLabel);
    
        JButton alternativeCarsButton = new JButton("Alternative Cars");
        alternativeCarsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        alternativeCarsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptFrame.dispose(); // Close the prompt frame
    
                // Display the alternatives panel
                JFrame alternativesFrame = new JFrame("Alternative Cars");
                alternativesFrame.setSize(600, 500);
                JPanel alternativesPanel = new JPanel();
                alternativesPanel.setLayout(new BoxLayout(alternativesPanel, BoxLayout.Y_AXIS));
    
                // Sort alternative cars by suitability (e.g., same brand, similar price)
                carList.sort(Comparator.comparingInt(car -> {
                    int score = 0;
                    if (car.getBrand().equalsIgnoreCase(selectedCar.getBrand())) {
                        score -= 10; // Higher priority for same brand
                    }
                    double priceDifference = Math.abs(car.getPricePerDay() - selectedCar.getPricePerDay());
                    score -= priceDifference; // Lower score for higher price difference
                    return score * (-1);
                }));
    
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
                                System.out.println("CODE CONTROL BWJAVA 164");
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
        });
    
        promptPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some spacing
        promptPanel.add(alternativeCarsButton);
    
        promptFrame.add(promptPanel);
        promptFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookingWindow("1", "Toyota", "Camry", 62.0);
        });
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

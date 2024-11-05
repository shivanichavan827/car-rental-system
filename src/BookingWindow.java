import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

public class BookingWindow extends JFrame {
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
        setLayout(new GridLayout(5, 1));

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


        JButton submitButton = new JButton("Confirm Booking");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDate selectedLocalDate = datePicker.getValue();
                String s= selectedLocalDate.toString();
                System.out.println(s);
                System.out.println("Sameed");
                System.out.print(selectedLocalDate);
                System.out.print("selectedLocalDate");
                selectedLocalDate.getClass().getSimpleName();
    
                
                new ConfirmationWindow(carId, s, pricePerDay);
                dispose();
            }
        });
        add(submitButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookingWindow("1", "Toyota", "Camry", 62.0);
        });
    }
}
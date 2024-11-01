import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingWindow extends JFrame {
    private String carId;
    private String brand;
    private String model;
    private double pricePerDay;

    public BookingWindow(String carId, String brand, String model, double pricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;

        setTitle("Book Car - " + brand + " " + model);
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JLabel dateLabel = new JLabel("Select Date:");
        JDatePickerImpl datePicker = new JDatePickerImpl(); // Ensure JDatePicker library is added
        add(dateLabel);
        add(datePicker);

        JLabel timeSlotLabel = new JLabel("Select Time Slot:");
        String[] timeSlots = {"08:00 - 12:00", "12:00 - 16:00", "16:00 - 20:00", "20:00 - 24:00"};
        JComboBox<String> timeSlotBox = new JComboBox<>(timeSlots);
        add(timeSlotLabel);
        add(timeSlotBox);

        JButton submitButton = new JButton("Confirm Booking");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date selectedDate = (Date) datePicker.getModel().getValue();
                String selectedTimeSlot = (String) timeSlotBox.getSelectedItem();
                new ConfirmationWindow(carId, selectedDate, selectedTimeSlot, pricePerDay);
                dispose();
            }
        });
        add(submitButton);

        setVisible(true);
    }
}


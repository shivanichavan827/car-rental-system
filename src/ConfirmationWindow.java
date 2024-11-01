import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationWindow extends JFrame {
    public ConfirmationWindow(String carId, Date date, String timeSlot, double pricePerDay) {
        setTitle("Booking Confirmation");
        setSize(300, 200);
        setLayout(new BorderLayout());

        // Calculate price per time slot (assuming each slot is a 4-hour block)
        double finalPrice = pricePerDay / 6;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);

        JLabel infoLabel = new JLabel("Date: " + dateStr + ", Time Slot: " + timeSlot);
        JLabel priceLabel = new JLabel("Final Price: $" + finalPrice);

        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(infoLabel, BorderLayout.NORTH);
        add(priceLabel, BorderLayout.CENTER);

        setVisible(true);
    }
}

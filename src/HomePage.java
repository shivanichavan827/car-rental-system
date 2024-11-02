import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("Car Rental System - Home");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton rentCarButton = new JButton("Rent a Car");
        JButton returnCarButton = new JButton("Return a Car");
        JButton logoutButton = new JButton("Logout");

        // Action Listeners for each button
        rentCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AvailableCarsPage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the HomePage
                new LoginRegisterApp();  // Return to login screen
            }
        });

        // Add buttons to the frame
        add(rentCarButton);
        add(returnCarButton);
        add(logoutButton);

        setVisible(true);
    }
}

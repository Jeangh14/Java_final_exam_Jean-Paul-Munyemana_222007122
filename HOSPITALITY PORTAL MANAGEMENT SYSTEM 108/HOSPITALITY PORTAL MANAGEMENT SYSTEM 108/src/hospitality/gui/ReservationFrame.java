package hospitality.gui;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservationFrame extends JFrame{
	private Guest guest;

	public ReservationFrame(Guest guest) {
		this.guest = guest;
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Make Reservation - " + guest.getFullName());
		setSize(400, 300);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel titleLabel = new JLabel("What would you like to reserve?", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

		JButton serviceButton = new JButton("Reserve Service");
		JButton roomButton = new JButton("Book Room");
		JButton backButton = new JButton("Back to Dashboard");

		buttonPanel.add(serviceButton);
		buttonPanel.add(roomButton);
		buttonPanel.add(backButton);

		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		add(mainPanel);

		serviceButton.addActionListener(e -> new ServiceReservationFrame(guest).setVisible(true));
		roomButton.addActionListener(e -> new RoomReservationFrame(guest).setVisible(true));
		backButton.addActionListener(e -> dispose());
	}
}






package hospitality.gui;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuestDashboardFrame extends JFrame{
	private Guest guest;

	public GuestDashboardFrame(Guest guest) {
		this.guest = guest;
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Guest Dashboard - Welcome " + guest.getFullName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 400);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Welcome label
		JLabel welcomeLabel = new JLabel("Welcome, " + guest.getFullName() + " (" + guest.getRole() + ")", 
				JLabel.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
		mainPanel.add(welcomeLabel, BorderLayout.NORTH);

		// Button panel
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

		JButton reservationButton = new JButton("Make Reservation");
		JButton checkReservationButton = new JButton("Check Reservations");
		JButton logoutButton = new JButton("Logout");

		buttonPanel.add(reservationButton);
		buttonPanel.add(checkReservationButton);
		buttonPanel.add(logoutButton);

		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		add(mainPanel);

		// Event listeners
		reservationButton.addActionListener(new ReservationButtonListener());
		checkReservationButton.addActionListener(e -> 
		JOptionPane.showMessageDialog(this, "Check Reservation feature coming soon!"));
		logoutButton.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
	}

	private class ReservationButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new ReservationFrame(guest).setVisible(true);
		}
	}
}






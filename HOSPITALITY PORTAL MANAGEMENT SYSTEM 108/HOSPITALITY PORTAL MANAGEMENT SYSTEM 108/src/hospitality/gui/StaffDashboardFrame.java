package hospitality.gui;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffDashboardFrame extends JFrame {

	private Guest staff;

	public StaffDashboardFrame(Guest staff) {
		this.staff = staff;
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Staff Dashboard - Welcome " + staff.getFullName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Welcome label
		JLabel welcomeLabel = new JLabel("Staff Dashboard - Welcome " + staff.getFullName(), 
				JLabel.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
		mainPanel.add(welcomeLabel, BorderLayout.NORTH);

		// Button panel
		JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));

		JButton addUserButton = new JButton("Add User");
		JButton updateUserButton = new JButton("Update User");
		JButton checkUsersButton = new JButton("Check All Users");
		JButton deleteUserButton = new JButton("Delete User");
		JButton logoutButton = new JButton("Logout");

		buttonPanel.add(addUserButton);
		buttonPanel.add(updateUserButton);
		buttonPanel.add(checkUsersButton);
		buttonPanel.add(deleteUserButton);
		buttonPanel.add(logoutButton);

		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		add(mainPanel);

		// Event listeners
		addUserButton.addActionListener(e -> new AddUserFrame().setVisible(true));
		updateUserButton.addActionListener(e -> new UpdateUserFrame().setVisible(true));
		checkUsersButton.addActionListener(e -> new CheckUsersFrame().setVisible(true));
		deleteUserButton.addActionListener(e -> new DeleteUserFrame().setVisible(true));
		logoutButton.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
	}
}




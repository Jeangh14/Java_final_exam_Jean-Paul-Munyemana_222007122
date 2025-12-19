package hospitality.gui;
import com.hospitality.GuestDAO;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateUserFrame extends JFrame {

	private JComboBox<Guest> userComboBox;
	private JTextField userIdField;
	private JTextField usernameField, emailField, fullNameField;
	private JComboBox<String> roleComboBox;
	private JButton updateButton, loadButton, loadByIdButton;
	private Guest currentGuest;

	public UpdateUserFrame() {
		initializeUI();
		loadUsers();
	}

	private void initializeUI() {
		setTitle("Update User");
		setSize(600, 500);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Create tabbed pane for different selection methods
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab 1: Select from list
		JPanel listPanel = new JPanel(new BorderLayout(10, 10));
		JPanel listSelectionPanel = new JPanel(new FlowLayout());
		listSelectionPanel.add(new JLabel("Select User:"));
		userComboBox = new JComboBox<>();
		userComboBox.setPreferredSize(new Dimension(200, 25));
		listSelectionPanel.add(userComboBox);

		loadButton = new JButton("Load User Data");
		listSelectionPanel.add(loadButton);
		listPanel.add(listSelectionPanel, BorderLayout.NORTH);
		listPanel.add(createFormPanel(), BorderLayout.CENTER);

		// Tab 2: Load by ID
		JPanel idPanel = new JPanel(new BorderLayout(10, 10));
		JPanel idSelectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		idSelectionPanel.add(new JLabel("Enter User ID:"));
		userIdField = new JTextField();
		idSelectionPanel.add(userIdField);
		idSelectionPanel.add(new JLabel(""));
		loadByIdButton = new JButton("Load by ID");
		idSelectionPanel.add(loadByIdButton);
		idPanel.add(idSelectionPanel, BorderLayout.NORTH);
		idPanel.add(createFormPanel(), BorderLayout.CENTER);

		// Add tabs
		tabbedPane.addTab("Select from List", listPanel);
		tabbedPane.addTab("Load by ID", idPanel);

		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		// Common buttons
		JPanel commonButtonPanel = new JPanel(new FlowLayout());
		updateButton = new JButton("Update User");
		JButton cancelButton = new JButton("Cancel");
		commonButtonPanel.add(updateButton);
		commonButtonPanel.add(cancelButton);
		mainPanel.add(commonButtonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		// Event listeners
		loadButton.addActionListener(new LoadButtonListener());
		loadByIdButton.addActionListener(new LoadByIdListener());
		updateButton.addActionListener(new UpdateButtonListener());
		cancelButton.addActionListener(e -> dispose());
	}

	private JPanel createFormPanel() {
		JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

		formPanel.add(new JLabel("Username:"));
		usernameField = new JTextField();
		formPanel.add(usernameField);

		formPanel.add(new JLabel("Email:"));
		emailField = new JTextField();
		formPanel.add(emailField);

		formPanel.add(new JLabel("Full Name:"));
		fullNameField = new JTextField();
		formPanel.add(fullNameField);

		formPanel.add(new JLabel("Role:"));
		String[] roles = {"Patient", "Doctor", "Nurse", "Admin"};
		roleComboBox = new JComboBox<>(roles);
		formPanel.add(roleComboBox);

		return formPanel;
	}

	private void loadUsers() {
		GuestDAO guestDAO = new GuestDAO();
		List<Guest> guests = guestDAO.getAllGuests();

		userComboBox.removeAllItems();
		for (Guest guest : guests) {
			userComboBox.addItem(guest);
		}
	}

	private void loadUserData(Guest guest) {
		if (guest != null) {
			currentGuest = guest;
			usernameField.setText(guest.getUsername());
			emailField.setText(guest.getEmail());
			fullNameField.setText(guest.getFullName());
			roleComboBox.setSelectedItem(guest.getRole());
		}
	}

	private class LoadButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Guest selectedGuest = (Guest) userComboBox.getSelectedItem();
			loadUserData(selectedGuest);
		}
	}

	private class LoadByIdListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String userIdText = userIdField.getText().trim();
			if (userIdText.isEmpty()) {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"Please enter a User ID", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int userId = Integer.parseInt(userIdText);
				GuestDAO guestDAO = new GuestDAO();
				// You'll need to add a getGuestById method to GuestDAO
				Guest guest = getGuestById(guestDAO, userId);
				if (guest != null) {
					loadUserData(guest);
				} else {
					JOptionPane.showMessageDialog(UpdateUserFrame.this,
							"User with ID " + userId + " not found", 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"Please enter a valid numeric User ID", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private Guest getGuestById(GuestDAO guestDAO, int userId) {
		// This method needs to be implemented in GuestDAO
		// For now, we'll get all guests and find by ID
		List<Guest> guests = guestDAO.getAllGuests();
		for (Guest guest : guests) {
			if (guest.getGuestID() == userId) {
				return guest;
			}
		}
		return null;
	}

	private class UpdateButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentGuest == null) {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"Please load a user first", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String username = usernameField.getText().trim();
			String email = emailField.getText().trim();
			String fullName = fullNameField.getText().trim();
			String role = (String) roleComboBox.getSelectedItem();

			if (username.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			currentGuest.setUsername(username);
			currentGuest.setEmail(email);
			currentGuest.setFullName(fullName);
			currentGuest.setRole(role);

			GuestDAO guestDAO = new GuestDAO();
			if (guestDAO.updateGuest(currentGuest)) {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				// Refresh the user list
				loadUsers();
				userIdField.setText("");
				currentGuest = null;
			} else {
				JOptionPane.showMessageDialog(UpdateUserFrame.this,
						"Failed to update user", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

package hospitality.gui;
import com.hospitality.GuestDAO;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AddUserFrame extends JFrame{
	private JTextField usernameField, emailField, fullNameField;
	private JPasswordField passwordField;
	private JComboBox<String> roleComboBox;

	public AddUserFrame() {
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Add New User");
		setSize(400, 350);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

		formPanel.add(new JLabel("Username:"));
		usernameField = new JTextField();
		formPanel.add(usernameField);

		formPanel.add(new JLabel("Password:"));
		passwordField = new JPasswordField();
		formPanel.add(passwordField);

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

		mainPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton addButton = new JButton("Add User");
		JButton cancelButton = new JButton("Cancel");

		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		addButton.addActionListener(new AddButtonListener());
		cancelButton.addActionListener(e -> dispose());
	}

	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword());
			String email = emailField.getText().trim();
			String fullName = fullNameField.getText().trim();
			String role = (String) roleComboBox.getSelectedItem();

			if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
				JOptionPane.showMessageDialog(AddUserFrame.this, 
						"Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Guest guest = new Guest(username, password, email, fullName, role);
			GuestDAO guestDAO = new GuestDAO();

			if (guestDAO.addGuest(guest)) {
				JOptionPane.showMessageDialog(AddUserFrame.this, 
						"User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(AddUserFrame.this, 
						"Failed to add user. Username or email may already exist.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


}

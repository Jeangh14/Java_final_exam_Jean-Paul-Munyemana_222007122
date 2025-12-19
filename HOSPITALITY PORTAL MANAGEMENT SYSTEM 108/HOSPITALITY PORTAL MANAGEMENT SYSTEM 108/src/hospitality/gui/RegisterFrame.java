package hospitality.gui;
import com.hospitality.GuestDAO;
import hospitality.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame{

	private JTextField usernameField, emailField, fullNameField;
	private JPasswordField passwordField, confirmPasswordField;
	private JComboBox<String> roleComboBox;
	private JButton registerButton, cancelButton;

	public RegisterFrame() {
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Register New User");
		setSize(400, 350);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

		formPanel.add(new JLabel("Username:"));
		usernameField = new JTextField();
		formPanel.add(usernameField);

		formPanel.add(new JLabel("Password:"));
		passwordField = new JPasswordField();
		formPanel.add(passwordField);

		formPanel.add(new JLabel("Confirm Password:"));
		confirmPasswordField = new JPasswordField();
		formPanel.add(confirmPasswordField);

		formPanel.add(new JLabel("Email:"));
		emailField = new JTextField();
		formPanel.add(emailField);

		formPanel.add(new JLabel("Full Name:"));
		fullNameField = new JTextField();
		formPanel.add(fullNameField);

		formPanel.add(new JLabel("Role:"));
		String[] roles = {"Patient", "Doctor", "Nurse"};
		roleComboBox = new JComboBox<>(roles);
		formPanel.add(roleComboBox);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		registerButton = new JButton("Register");
		cancelButton = new JButton("Cancel");

		buttonPanel.add(registerButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		registerButton.addActionListener(new RegisterButtonListener());
		cancelButton.addActionListener(e -> dispose());
	}

	private class RegisterButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());
			String email = emailField.getText().trim();
			String fullName = fullNameField.getText().trim();
			String role = (String) roleComboBox.getSelectedItem();

			if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
				JOptionPane.showMessageDialog(RegisterFrame.this, 
						"Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(RegisterFrame.this, 
						"Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Guest guest = new Guest(username, password, email, fullName, role);
			GuestDAO guestDAO = new GuestDAO();

			if (guestDAO.addGuest(guest)) {
				JOptionPane.showMessageDialog(RegisterFrame.this, 
						"Registration successful! Please login.", 
						"Success", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(RegisterFrame.this, 
						"Registration failed. Username or email may already exist.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}





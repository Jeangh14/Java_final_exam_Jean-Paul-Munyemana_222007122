package hospitality.gui;
import com.hospitality.ServiceDAO;
import hospitality.model.Guest;
import hospitality.model.Service;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ServiceReservationFrame extends JFrame{
	private Guest guest;
	private JComboBox<Service> serviceComboBox;
	private JTextArea descriptionArea;
	private JLabel priceLabel;

	public ServiceReservationFrame(Guest guest) {
		this.guest = guest;
		initializeUI();
		loadServices();
	}

	private void initializeUI() {
		setTitle("Reserve Service - " + guest.getFullName());
		setSize(500, 400);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Form panel
		JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

		formPanel.add(new JLabel("Select Service:"));
		serviceComboBox = new JComboBox<>();
		serviceComboBox.addActionListener(e -> updateServiceDetails());
		formPanel.add(serviceComboBox);

		formPanel.add(new JLabel("Description:"));
		descriptionArea = new JTextArea(3, 20);
		descriptionArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		formPanel.add(scrollPane);

		formPanel.add(new JLabel("Price:"));
		priceLabel = new JLabel("$0.00");
		formPanel.add(priceLabel);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton reserveButton = new JButton("Reserve Service");
		JButton payButton = new JButton("Pay Now");
		JButton backButton = new JButton("Back");

		buttonPanel.add(reserveButton);
		buttonPanel.add(payButton);
		buttonPanel.add(backButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		reserveButton.addActionListener(new ReserveButtonListener());
		payButton.addActionListener(e -> processPayment());
		backButton.addActionListener(e -> dispose());
	}

	private void loadServices() {
		ServiceDAO serviceDAO = new ServiceDAO();
		List<Service> services = serviceDAO.getAllServices();

		for (Service service : services) {
			serviceComboBox.addItem(service);
		}

		if (services.size() > 0) {
			updateServiceDetails();
		}
	}

	private void updateServiceDetails() {
		Service selectedService = (Service) serviceComboBox.getSelectedItem();
		if (selectedService != null) {
			descriptionArea.setText(selectedService.getDescription());
			priceLabel.setText("$" + selectedService.getPriceOrValue());
		}
	}

	private class ReserveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Service selectedService = (Service) serviceComboBox.getSelectedItem();
			if (selectedService != null) {
				JOptionPane.showMessageDialog(ServiceReservationFrame.this,
						"Service reserved successfully!\n" +
								"Service: " + selectedService.getName() + "\n" +
								"Price: $" + selectedService.getPriceOrValue(),
								"Reservation Confirmed", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void processPayment() {
		Service selectedService = (Service) serviceComboBox.getSelectedItem();
		if (selectedService != null) {
			int option = JOptionPane.showConfirmDialog(this,
					"Total Amount: $" + selectedService.getPriceOrValue() + "\n" +
							"Proceed with payment?",
							"Payment Confirmation", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(this,
						"Payment processed successfully!\n" +
								"Thank you for your payment.",
								"Payment Successful", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		}
	}
}





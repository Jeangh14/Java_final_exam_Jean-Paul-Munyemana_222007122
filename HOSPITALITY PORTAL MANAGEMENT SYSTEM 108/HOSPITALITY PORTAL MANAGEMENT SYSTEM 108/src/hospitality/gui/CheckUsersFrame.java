package hospitality.gui;
import com.hospitality.GuestDAO;
import hospitality.model.Guest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
public class CheckUsersFrame extends JFrame {
	private JTable usersTable;
	private DefaultTableModel tableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTextField searchField;

	public CheckUsersFrame() {
		initializeUI();
		loadUsersData();
	}

	private void initializeUI() {
		setTitle("All Users Information");
		setSize(900, 600);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Title and search panel
		JPanel topPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("All Registered Users", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		topPanel.add(titleLabel, BorderLayout.NORTH);

		// Search panel
		JPanel searchPanel = new JPanel(new FlowLayout());
		searchPanel.add(new JLabel("Search:"));
		searchField = new JTextField(20);
		searchPanel.add(searchField);
		JButton searchButton = new JButton("Search");
		searchPanel.add(searchButton);
		JButton clearButton = new JButton("Clear");
		searchPanel.add(clearButton);
		JButton refreshButton = new JButton("Refresh");
		searchPanel.add(refreshButton);

		topPanel.add(searchPanel, BorderLayout.SOUTH);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// Table
		String[] columns = {"User ID", "Username", "Email", "Full Name", "Role", "Created At", "Last Login"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				return column == 0 ? Integer.class : String.class; // User ID is Integer
			}
		};
		usersTable = new JTable(tableModel);
		usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersTable.getTableHeader().setReorderingAllowed(false);

		// Add row sorter for searching
		sorter = new TableRowSorter<>(tableModel);
		usersTable.setRowSorter(sorter);

		JScrollPane scrollPane = new JScrollPane(usersTable);
		scrollPane.setPreferredSize(new Dimension(800, 400));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Info panel
		JLabel infoLabel = new JLabel("Total Users: 0");
		mainPanel.add(infoLabel, BorderLayout.SOUTH);

		// Close button
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(e -> dispose());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		// Event listeners for search
		searchButton.addActionListener(e -> performSearch());
		clearButton.addActionListener(e -> {
			searchField.setText("");
			sorter.setRowFilter(null);
		});
		refreshButton.addActionListener(e -> loadUsersData());

		// Real-time search as user types
		searchField.addActionListener(e -> performSearch());
	}

	private void performSearch() {
		String text = searchField.getText();
		if (text.trim().length() == 0) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Case insensitive
		}
	}

	private void loadUsersData() {
		GuestDAO guestDAO = new GuestDAO();
		List<Guest> guests = guestDAO.getAllGuests();

		tableModel.setRowCount(0); // Clear existing data

		if (guests.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No users found in the database.", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		for (Guest guest : guests) {
			Object[] rowData = {
					guest.getGuestID(),
					guest.getUsername(),
					guest.getEmail(),
					guest.getFullName(),
					guest.getRole(),
					guest.getCreatedAt() != null ? guest.getCreatedAt().toString() : "N/A",
							guest.getLastLogin() != null ? guest.getLastLogin().toString() : "Never"
			};
			tableModel.addRow(rowData);
		}

		// Update user count
		updateUserCount(guests.size());

		// Auto-adjust column widths
		for (int i = 0; i < usersTable.getColumnCount(); i++) {
			usersTable.getColumnModel().getColumn(i).setPreferredWidth(120);
		}
	}

	private void updateUserCount(int count) {
		// Find and update the info label
		Component[] components = getContentPane().getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel) {
				Component[] subComps = ((JPanel) comp).getComponents();
				for (Component subComp : subComps) {
					if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Total Users:")) {
						((JLabel) subComp).setText("Total Users: " + count);
						return;
					}
				}
			}
		}
	}
}




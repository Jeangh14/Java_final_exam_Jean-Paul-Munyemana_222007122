package com.hospitality;
import hospitality.db.DBConnection;
import hospitality.model.Guest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
	// Authenticate user by username and password
	public Guest authenticate(String username, String password) {
		String sql = "SELECT * FROM guest WHERE Username = ? AND PasswordHash = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Guest guest = extractGuestFromResultSet(rs);

				// Update last login timestamp
				updateLastLogin(guest.getGuestID());

				return guest;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Get all guests from the database
	public List<Guest> getAllGuests() {
		List<Guest> guests = new ArrayList<>();
		String sql = "SELECT * FROM guest ORDER BY GuestID";

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Guest guest = extractGuestFromResultSet(rs);
				guests.add(guest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return guests;
	}

	// Get guest by ID
	public Guest getGuestById(int guestId) {
		String sql = "SELECT * FROM guest WHERE GuestID = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, guestId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return extractGuestFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Get guest by username
	public Guest getGuestByUsername(String username) {
		String sql = "SELECT * FROM guest WHERE Username = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return extractGuestFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Get guest by email
	public Guest getGuestByEmail(String email) {
		String sql = "SELECT * FROM guest WHERE Email = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return extractGuestFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Add new guest to database
	public boolean addGuest(Guest guest) {
		// Check if username or email already exists
		if (isUsernameExists(guest.getUsername())) {
			return false;
		}
		if (isEmailExists(guest.getEmail())) {
			return false;
		}

		String sql = "INSERT INTO guest (Username, PasswordHash, Email, FullName, Role, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, guest.getUsername());
			stmt.setString(2, guest.getPasswordHash());
			stmt.setString(3, guest.getEmail());
			stmt.setString(4, guest.getFullName());
			stmt.setString(5, guest.getRole());
			stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Update guest information
	public boolean updateGuest(Guest guest) {
		// Check if username already exists for other users
		if (isUsernameExists(guest.getUsername(), guest.getGuestID())) {
			return false;
		}
		// Check if email already exists for other users
		if (isEmailExists(guest.getEmail(), guest.getGuestID())) {
			return false;
		}

		String sql = "UPDATE guest SET Username=?, Email=?, FullName=?, Role=? WHERE GuestID=?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, guest.getUsername());
			stmt.setString(2, guest.getEmail());
			stmt.setString(3, guest.getFullName());
			stmt.setString(4, guest.getRole());
			stmt.setInt(5, guest.getGuestID());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Update guest password
	public boolean updatePassword(int guestId, String newPassword) {
		String sql = "UPDATE guest SET PasswordHash = ? WHERE GuestID = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, newPassword);
			stmt.setInt(2, guestId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Delete guest by ID
	public boolean deleteGuest(int guestID) {
		String sql = "DELETE FROM guest WHERE GuestID=?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, guestID);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Search guests by name, username, or email
	public List<Guest> searchGuests(String searchTerm) {
		List<Guest> guests = new ArrayList<>();
		String sql = "SELECT * FROM guest WHERE Username LIKE ? OR FullName LIKE ? OR Email LIKE ? ORDER BY GuestID";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			String likeTerm = "%" + searchTerm + "%";
			stmt.setString(1, likeTerm);
			stmt.setString(2, likeTerm);
			stmt.setString(3, likeTerm);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Guest guest = extractGuestFromResultSet(rs);
				guests.add(guest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return guests;
	}

	// Get guests by role
	public List<Guest> getGuestsByRole(String role) {
		List<Guest> guests = new ArrayList<>();
		String sql = "SELECT * FROM guest WHERE Role = ? ORDER BY FullName";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, role);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Guest guest = extractGuestFromResultSet(rs);
				guests.add(guest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return guests;
	}

	// Get total count of guests
	public int getTotalGuestsCount() {
		String sql = "SELECT COUNT(*) FROM guest";

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Get count of guests by role
	public int getGuestsCountByRole(String role) {
		String sql = "SELECT COUNT(*) FROM guest WHERE Role = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, role);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Check if username exists (for new users)
	private boolean isUsernameExists(String username) {
		return getGuestByUsername(username) != null;
	}

	// Check if username exists for other users (for updates)
	private boolean isUsernameExists(String username, int excludeGuestId) {
		String sql = "SELECT COUNT(*) FROM guest WHERE Username = ? AND GuestID != ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, username);
			stmt.setInt(2, excludeGuestId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Check if email exists (for new users)
	private boolean isEmailExists(String email) {
		return getGuestByEmail(email) != null;
	}

	// Check if email exists for other users (for updates)
	private boolean isEmailExists(String email, int excludeGuestId) {
		String sql = "SELECT COUNT(*) FROM guest WHERE Email = ? AND GuestID != ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setInt(2, excludeGuestId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Update last login timestamp
	private boolean updateLastLogin(int guestId) {
		String sql = "UPDATE guest SET LastLogin = ? WHERE GuestID = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			stmt.setInt(2, guestId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Helper method to extract Guest object from ResultSet
	private Guest extractGuestFromResultSet(ResultSet rs) throws SQLException {
		Guest guest = new Guest();
		guest.setGuestID(rs.getInt("GuestID"));
		guest.setUsername(rs.getString("Username"));
		guest.setPasswordHash(rs.getString("PasswordHash"));
		guest.setEmail(rs.getString("Email"));
		guest.setFullName(rs.getString("FullName"));
		guest.setRole(rs.getString("Role"));
		guest.setCreatedAt(rs.getTimestamp("CreatedAt"));
		guest.setLastLogin(rs.getTimestamp("LastLogin"));
		return guest;
	}

	// Validate guest data before saving
	public boolean validateGuestData(Guest guest) {
		if (guest.getUsername() == null || guest.getUsername().trim().isEmpty()) {
			return false;
		}
		if (guest.getPasswordHash() == null || guest.getPasswordHash().trim().isEmpty()) {
			return false;
		}
		if (guest.getEmail() == null || guest.getEmail().trim().isEmpty()) {
			return false;
		}
		if (guest.getFullName() == null || guest.getFullName().trim().isEmpty()) {
			return false;
		}
		if (guest.getRole() == null || guest.getRole().trim().isEmpty()) {
			return false;
		}
		return true;
	}

	// Get all available roles (for combo boxes)
	public String[] getAvailableRoles() {
		return new String[]{"Patient", "Doctor", "Nurse", "Admin"};
	}
}




package com.hospitality;
import hospitality.db.DBConnection;
import hospitality.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

	public List<Room> getAllAvailableRooms() {
		List<Room> rooms = new ArrayList<>();
		String sql = "SELECT * FROM room WHERE Status = 'Available'";

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Room room = new Room();
				room.setRoomID(rs.getInt("RoomID"));
				room.setName(rs.getString("Name"));
				room.setDescription(rs.getString("Description"));
				room.setCategory(rs.getString("Category"));
				room.setPriceOrValue(rs.getDouble("PriceOrValue"));
				room.setStatus(rs.getString("Status"));
				rooms.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rooms;
	}
}


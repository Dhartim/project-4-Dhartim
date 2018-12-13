package cs601.project4.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs601.project4.Database.ConnectionDB;
import cs601.project4.Database.DatabaseFields;

public class GetUserMetaData {

	PreparedStatement preparedStatement;
	ResultSet result;
	Connection conn = ConnectionDB.getInstance().connectDatabase();

	//get event id from tickets table
	public List<Integer> getUserTickets(int userId) {

		List<Integer> userTickets = new ArrayList<Integer>();
		
		String sql = "select " + DatabaseFields.eventId + " from " + DatabaseFields.ticketTable + " where "
				+ DatabaseFields.userId + " = ?";

		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, userId);

			result = preparedStatement.executeQuery();

			while (result.next()) {
				userTickets.add(result.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userTickets;

	}
    
	//gives user id exist or not 
	public boolean isUserExist(int userId) {

		String sql = "select count(*) from " + DatabaseFields.userTable + " where " + DatabaseFields.userId + " = ?";

		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, userId);

			result = preparedStatement.executeQuery();

			if (result.next() && result.getInt(1) > 0) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

}

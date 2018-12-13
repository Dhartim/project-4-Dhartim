package cs601.project4.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.CreateEventResponse;
import cs601.project4.Events.EventList;
import cs601.project4.Events.EventPurchaseBean;
import cs601.project4.Events.EventResponseWithEventId;
import cs601.project4.Events.Events;
import cs601.project4.Tickets.AddTicketBean;
import cs601.project4.Tickets.Tickets;
import cs601.project4.Tickets.TransferTicketBuffer;
import cs601.project4.Users.Users;
import javafx.scene.chart.PieChart.Data;
public class DBManager 
{

	PreparedStatement stmt;
	ResultSet result;
	Users user = null;
	Events events;
	Gson gson = new Gson();
	Connection con = ConnectionDB.getInstance().connectDatabase();

	public synchronized Users getUser(int userId) 
	{
		Users user = new Users();
		String sql = "select * from " + DatabaseFields.userTable + " where " + DatabaseFields.userId + " = ?";
		System.out.println(sql);

		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, userId);
			result = stmt.executeQuery();

			if (result.next()) {
				user = new Users(result.getInt(DatabaseFields.userId), result.getString(DatabaseFields.userName));
			} else {
				user = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;

	}

	//list of all events
	public synchronized List<EventList> getEventList() 
	{
		List<EventList> resultlist = new ArrayList<EventList>();
		EventList listEvent;
		String sql = "SELECT " + DatabaseFields.eventId + " , " + DatabaseFields.eventName + " , " + DatabaseFields.userId + " , " +
				DatabaseFields.availTickets + " , " + DatabaseFields.purchaseTicekts + " FROM " + DatabaseFields.eventTable + ";";
		System.out.println("sql " + sql);
		try 
		{
			stmt = con.prepareStatement(sql);
			result = stmt.executeQuery();
			//			if(result == null)
			//			{
			//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//			}
			while(result.next())
			{
				//System.out.println(resultSet.getString(1));
				listEvent = new EventList(result.getInt(DatabaseFields.eventId), result.getString(DatabaseFields.eventName)
						, result.getInt(DatabaseFields.userId), result.getInt(DatabaseFields.availTickets), result.getInt(DatabaseFields.purchaseTicekts));
				resultlist.add(listEvent);
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultlist;
	}
	//details of specific events
	public synchronized EventResponseWithEventId getEventDetailsWithId(int eventId, HttpServletResponse resp)
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		EventResponseWithEventId listEvent = null;// + " , " + DatabaseFields.numTicket +
		String sql = "SELECT " + DatabaseFields.eventId + " , " + DatabaseFields.eventName + " , "
				+ DatabaseFields.userId  +  " , " + DatabaseFields.numTicket + " , "+ DatabaseFields.availTickets + " , " + DatabaseFields.purchaseTicekts
				+ " FROM " + DatabaseFields.eventTable + " where " + DatabaseFields.eventId + " = ?";
		System.out.println("sql " + sql);
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, eventId);
			result = stmt.executeQuery();
			if (result == null) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			boolean flag = result.next();
			if (!(flag)) 
			{
				try {
					resp.getWriter().append("Event not found");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				// {
				// data from database
//result.getInt(DatabaseFields.numTicket),
				listEvent =  new EventResponseWithEventId (result.getInt(DatabaseFields.eventId), 
						result.getString(DatabaseFields.eventName), result.getInt(DatabaseFields.userId), 
						result.getInt(DatabaseFields.availTickets), result.getInt(DatabaseFields.purchaseTicekts));
//				listEvent = new Events(result.getInt(DatabaseFields.eventId),
//						result.getString(DatabaseFields.eventName), result.getInt(DatabaseFields.userId),
//						result.getInt(DatabaseFields.numTicket), 
//						 result.getInt(DatabaseFields.availTickets),
//						result.getInt(DatabaseFields.purchaseTicekts));
				// resultlist.add(listEvent);
				// }
			}
		} catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
		return listEvent;
		//		resp.setStatus(HttpServletResponse.SC_OK);
		//		Events listEvent= null;
		//		String sql = "SELECT " + DatabaseFields.eventId + " , " + DatabaseFields.eventName + " , " + DatabaseFields.userId + " , " +
		//				DatabaseFields.availTickets + " , " + DatabaseFields.purchaseTicekts + " FROM "
		//				+ DatabaseFields.eventTable + " where "+ DatabaseFields.eventId + " = ?";
		//		System.out.println("sql " + sql);
		//		try 
		//		{
		//			stmt = con.prepareStatement(sql);
		//			stmt.setInt(1, eventId);
		//			result = stmt.executeQuery();
		//			if(result == null)
		//			{
		//				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		//			}
		//			boolean flag = result.next();
		//			if(!(flag))
		//			{
		//				try {
		//					resp.getWriter().append("Event not found");
		//				} catch (IOException e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		//			}
		//			else {
		//				//{
		//				//data from database
		//				listEvent = new Events(result.getInt(DatabaseFields.eventId), result.getString(DatabaseFields.eventName)
		//						, result.getInt(DatabaseFields.userId), result.getInt(DatabaseFields.availTickets), 
		//						result.getInt(DatabaseFields.purchaseTicekts));
		//				//resultlist.add(listEvent);
		//				//}
		//			}
		//		}
		//		catch (SQLException e) 
		//		{
		//			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		//			e.printStackTrace();
		//		}
		//		return listEvent;
	}


	//create user
	public synchronized Users createUser(Users userDetail) {
		int record = 0;
		String sql = "insert into " + DatabaseFields.userTable + " (" + DatabaseFields.userName + ") values (?)";
		System.out.println("sql : " + sql);
		try {
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, userDetail.getUsername());
			record = stmt.executeUpdate();
			if (record > 0) {
				result = stmt.getGeneratedKeys();
				if (result.next() && result != null) {
					user = new Users(result.getInt(1)); // pass userid to user class
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	//add tickets
	public synchronized boolean addTickets(int userId, int eventId, int tickets) 
	{
		String sql = "select " + DatabaseFields.ticketCount +  " from " + DatabaseFields.ticketTable + " where " + DatabaseFields.userId + " = ?"
				+ " AND " + DatabaseFields.eventId + " = ?";
		try {
			stmt = con.prepareStatement(sql);
			System.out.println(sql);
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);
			result = stmt.executeQuery();
			if(result.next())
			{
				System.out.println("Going to update table");
				sql =  "update " + DatabaseFields.ticketTable + " set " + DatabaseFields.ticketCount + " = ?" 
						+ " where " + DatabaseFields.eventId + " = ?" + " AND " + DatabaseFields.userId + " = ?" ;
				try {
					stmt = con.prepareStatement(sql);
					System.out.println(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt.setInt(1,(tickets + result.getInt(DatabaseFields.ticketCount))); //increase ticket count;
				stmt.setInt(2, eventId);
				stmt.setInt(3, userId);
				int record = stmt.executeUpdate();
				System.out.println(record);
				if(record > 0)
				{
					return true; //update is success
				}
				else
				{
					return false; //can't update it
				}
			}
			else
			{
				int result = 0;
				sql = "insert into " + DatabaseFields.ticketTable + " (" + DatabaseFields.eventId + ","
						+ DatabaseFields.userId + "," + DatabaseFields.ticketCount + ") values (?,?,?)";

				try {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, eventId); 
					stmt.setInt(2, userId);
					stmt.setInt(3, tickets);
					result = stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (result > 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	}

	//get list of users
	public synchronized List<Users> getAllUsers() {
		List<Users> allUsers = new ArrayList<Users>();
		Users user = new Users();
		String sql = "select * from " + DatabaseFields.userTable;
		System.out.println(sql);

		try {
			stmt = con.prepareStatement(sql);
			result = stmt.executeQuery();

			if (result.next()) {
				user = new Users(result.getInt(DatabaseFields.userId), result.getString(DatabaseFields.userName));
				allUsers.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allUsers;
	}
	//get list of user tickets
	public synchronized List<HashMap<String, Integer>> getUserTickets(int userId) {

		List<HashMap<String, Integer>> userTickets = new ArrayList<HashMap<String, Integer>>();

		String sql = "select " + DatabaseFields.eventId + ", " + DatabaseFields.ticketCount + " from "
				+ DatabaseFields.ticketTable + " where " + DatabaseFields.userId + " = ?";
		System.out.println(sql);
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, userId);

			result = stmt.executeQuery();

			while (result.next()) {
				int counter = result.getInt(DatabaseFields.ticketCount);
				while (counter != 0) {
					HashMap<String, Integer> userTicketList = new HashMap<String, Integer>();
					userTicketList.put("eventid", result.getInt(DatabaseFields.eventId));
					userTickets.add(userTicketList);
					counter--;
					System.out.println(userTicketList.toString());
				}
				System.out.println("/////" +userTickets.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userTickets;

	}

	//user exist
	public synchronized boolean isUserExist(int userId) 
	{
		String sql = "select count(*) from " + DatabaseFields.userTable + " where " + DatabaseFields.userId + " = ?";
		System.out.println(sql);
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			if (result.next() && result.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	//create event
	public synchronized CreateEventResponse createEvent(CreateEventBuffer createEventDetails)
	{
		CreateEventResponse events = null;
		int record = 0;
		String sql = "insert into " + DatabaseFields.eventTable + " ("
				+ "" + DatabaseFields.eventName +"," + DatabaseFields.numTicket
				+ "," +DatabaseFields.availTickets + "," +DatabaseFields.purchaseTicekts + ", "
				+ DatabaseFields.userId + ") values (?,?,?,?,?)";
		System.out.println("sql : " + sql);
		try {
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, createEventDetails.getEventname());
			stmt.setInt(2, createEventDetails.getNumtickets());
			stmt.setInt(3, createEventDetails.getNumtickets());
			stmt.setInt(4, 0);
			stmt.setInt(5, createEventDetails.getUserid());
			record = stmt.executeUpdate();
			if (record > 0) {
				result = stmt.getGeneratedKeys();
				if (result.next() && result != null) {
					events = new CreateEventResponse(result.getInt(1)); // pass eventid to events class
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	//update event table after add or purchase of tickets
	public synchronized boolean updateEvents(EventResponseWithEventId event, EventPurchaseBean addtickets)
	{
		boolean result = false;
		//sql to update event table
		String sqlEventUpdate = "update " + DatabaseFields.eventTable + " set " + DatabaseFields.availTickets + " = ?,"
				+ DatabaseFields.purchaseTicekts + " = ?" + " where " + DatabaseFields.eventId + " = ?";
		try {
			stmt = con.prepareStatement(sqlEventUpdate);
			stmt.setInt(1, (event.getAvail() - addtickets.getTickets())); //subtract avail
			stmt.setInt(2, (event.getPurchased() + addtickets.getTickets())); //add in purchased
			stmt.setInt(3, addtickets.getEventid());
			System.out.println(sqlEventUpdate);
			int record = stmt.executeUpdate();
			if(record > 0)
			{
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	//transfer ticket
	public synchronized boolean transferTicket(TransferTicketBuffer transferTickets, int userid,
			HttpServletResponse resp) {
		// check source user exist in tickets table or not
		int record = 0;
		String userexistsql = "select " + DatabaseFields.ticketCount + " from " + DatabaseFields.ticketTable + " where "
				+ DatabaseFields.userId + " = ?" + " AND " + DatabaseFields.eventId + " = ?";
		try {
			stmt = con.prepareStatement(userexistsql);
			stmt.setInt(1, userid);
			stmt.setInt(2, transferTickets.getEventid());
			result = stmt.executeQuery();
			System.out.println(stmt);
			//get source user ticket count
			if (result.next() && (transferTickets.getTickets() > 0) && (result.getInt(DatabaseFields.ticketCount) > 0)
					&& (result.getInt(DatabaseFields.ticketCount) >= transferTickets.getTickets())
					&& ((result.getInt(DatabaseFields.ticketCount) - transferTickets.getTickets()) >= 0)) {
				int sourceTicketCount = result.getInt(DatabaseFields.ticketCount);
				// check for target user
				String usersql = "Select " + DatabaseFields.ticketCount + " from " + DatabaseFields.ticketTable
						+ " where " + DatabaseFields.userId + "= ? " + "AND " + DatabaseFields.eventId + "= ? ";
				stmt = con.prepareStatement(usersql);
				stmt.setInt(1, transferTickets.getTargetuser());
				stmt.setInt(2, transferTickets.getEventid());
				System.out.println(stmt);
				result = stmt.executeQuery();
				if (result.next()) {
					//get targetuser ticket count
					int targetTicketCount = result.getInt(DatabaseFields.ticketCount);
					// if exist den update target user
					record = updateTicketCount(transferTickets.getTargetuser(), transferTickets.getEventid(),
							(targetTicketCount + transferTickets.getTickets()));
				} else { // insert into table
					usersql = "insert into " + DatabaseFields.ticketTable + "( " + DatabaseFields.eventId + ", "
							+ DatabaseFields.userId + ", " + DatabaseFields.ticketCount + ") values (?,?,?)";
					stmt = con.prepareStatement(usersql);
					stmt.setInt(1, transferTickets.getEventid());
					stmt.setInt(2, transferTickets.getTargetuser());
					stmt.setInt(3, transferTickets.getTickets());
					System.out.println(stmt);
					record = stmt.executeUpdate();
				}
				if (record > 0) {
					// update source user
					record = updateTicketCount(userid, transferTickets.getEventid(),
							(sourceTicketCount - transferTickets.getTickets()));
					if (record > 0) {
						resp.setStatus(HttpServletResponse.SC_OK);
					}
				}
				return true;
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	// Update table for Ticket Transfer
		public int updateTicketCount(int userid, int eventid, int ticketCount) {
			int record = 0;
			String updtUser = "update " + DatabaseFields.ticketTable + " set " + DatabaseFields.ticketCount + "= ?"
					+ " where " + DatabaseFields.userId + "= ?" + " AND " + DatabaseFields.eventId + "= ?";
			try {
				stmt = con.prepareStatement(updtUser);
				stmt.setInt(1, ticketCount);
				stmt.setInt(2, userid);
				stmt.setInt(3, eventid);
				System.out.println(stmt);
				record = stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return record;

		}
}

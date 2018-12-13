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
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.CreateEventResponse;
import cs601.project4.Events.EventList;
import cs601.project4.Events.EventPurchaseBean;
import cs601.project4.Events.EventResponseWithEventId;

import cs601.project4.Tickets.TransferTicketBuffer;
import cs601.project4.Users.Users;
/**
 * DBManager - stores all sql method with database
 * @author dhartimadeka
 *
 */
public class DBManager 
{

	private PreparedStatement stmt;
	private ResultSet result;
	private Users user = null;
	private Connection con = ConnectionDB.getInstance().connectDatabase();
	/**
	 * getUser - it will return details users
	 * @param userId - holds user id to be passed in user table.
	 * @return
	 */
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
	/**
	 * getEventList - it will get list of events from database
	 * @return list of events from database
	 */
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
			while(result.next())
			{
				listEvent = new EventList(result.getInt(DatabaseFields.eventId), result.getString(DatabaseFields.eventName)
						, result.getInt(DatabaseFields.userId), result.getInt(DatabaseFields.availTickets), result.getInt(DatabaseFields.purchaseTicekts));
				resultlist.add(listEvent);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultlist;
	}
	/**
	 * getEventDetailsWithId - gives details list of events in front end.
	 * @param eventId - pass eventid into event service to get list of events in detail
	 * @param resp - gives response of http servlet response
	 * @return
	 */
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
				listEvent =  new EventResponseWithEventId (result.getInt(DatabaseFields.eventId), 
						result.getString(DatabaseFields.eventName), result.getInt(DatabaseFields.userId), 
						result.getInt(DatabaseFields.availTickets), result.getInt(DatabaseFields.purchaseTicekts));
			}
		} catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
		return listEvent;
	}
	/**
	 * createUser - used to create user in user table. 
	 * @param userDetail - pass user request body to table with username in it
	 * @return
	 */
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
	/**
	 * addTickets - it will add tickets in tickets table
	 * @param userId - holds userid of user who will purchase ticket
	 * @param eventId - holds event id to pass it to ticket table
	 * @param tickets - holds number of tickets to be bought
	 * @return
	 */
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
	/**
	 * getAllUsers - it gives list of users from user table
	 * @return
	 */
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
	/**
	 * getUserTickets - it gives list of tickets a user has from ticket's table
	 * @param userId - pass user id to fetch data
	 * @return
	 */
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
	/**
	 * createEvent - to create event in event't table
	 * @param createEventDetails passes event's details
	 * @return
	 */
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
	/**
	 * updateEvents - update eventTable and ticket table when ticket is purchased
	 * @param event - pass event details
	 * @param addtickets -passes purchase body
	 * @return
	 */
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
	/**
	 * transferTicket - transfer ticket from one user to another
	 * @param transferTickets - it holds details of tickets to transfer
	 * @param userid - holds userid from to fetch data
	 * @param resp - hold's respective response code
	 * @return
	 */
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
	/**
	 * updateTicketCount - update ticket if user already exists here. 
	 * @param userid - holds userid to fetch data
	 * @param eventid - holds eventid to fetch data
	 * @param ticketCount - holds number of tickets of which place
	 * @return
	 */
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

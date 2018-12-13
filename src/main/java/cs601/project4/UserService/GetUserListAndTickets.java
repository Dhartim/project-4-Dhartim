package cs601.project4.UserService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;
import cs601.project4.Database.DBManager;
import cs601.project4.Events.EventResponseWithEventId;
import cs601.project4.Tickets.AddTicketBean;
import cs601.project4.Tickets.TransferTicketBuffer;
import cs601.project4.Users.UserDetails;
import cs601.project4.Users.Users;

/**
 * handles following api's
 * 	//GET /{userid}
	//POST /{userid}/tickets/add
	//POST /{userid}/tickets/transfer
 * @author dhartimadeka
 *
 */
//GET /{userid}
//POST /{userid}/tickets/add
//POST /{userid}/tickets/transfer
@SuppressWarnings("serial")
public class GetUserListAndTickets extends HttpServlet
{

	private DBManager dbMngr = new DBManager();
	private int userId = 0;
	private Users user;
	private UserDetails userDetail;
	private String json;
	private Gson gson = new Gson();
	private Configuration config = Configuration.getInstance();
	private HttpURLConnection httpConn;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		// set response and content type
		resp.setStatus(HttpServletResponse.SC_OK);
		//httpConn.setRequestMethod("GET");
		resp.setContentType("application/json");
		String[] re = req.getRequestURI().split("/");
		if (re.length == 2 && re[1].matches("\\d+")) 
		{
			//get user details
			getUser(req, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	/**
	 * getUser - it gets details of user from database 
	 * @param req - http servlet request
	 * @param resp http servlet response
	 * @throws IOException
	 */
	//get user
	public void getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		// gives user id from uri
		userId = Integer.parseInt(req.getRequestURI().split("/")[1]);
		boolean flag = dbMngr.isUserExist(userId);
		System.out.println(flag);
		if(!flag)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("invalid user");
			return;
		}
		user = dbMngr.getUser(userId);
		//pass it to getusertickets and get list of tickets .
		List<HashMap<String, Integer>> ticketList = dbMngr.getUserTickets(user.getUserId());
		//pass it to userdetails class to set it into getter /setter methods
		userDetail = new UserDetails(user.getUserId(), user.getUsername(), ticketList);
		//convert it into json
		json = gson.toJson(userDetail);
		// send response back to front api
		resp.getWriter().append(json);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		// set response and content type
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		httpConn.setRequestMethod("POST");
		String[] re = req.getRequestURI().split("/");
		//checks uri
		if (re.length == 4 && re[1].matches("\\d+") && re[2].equals("tickets")) 
		{
			if (re[3].equals("add")) 
			{
				// POST /{userid}/tickets/add ---1
				System.out.println("add");
				addTicket(req, resp, Integer.parseInt(re[1]));
			} else if (re[3].equals("transfer")) {
				// POST /{userid}/tickets/transfer ---2
				System.out.println("transfer");
				transferTicket(req, resp, Integer.parseInt(re[1]));
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	/**
	 * addTicket - add ticket to ticket table.
	 * @param req - http servlet request
	 * @param resp - http servlet response
	 * @param userId - user id to send request it.
	 */
	//add ticket
	public void addTicket(HttpServletRequest req, HttpServletResponse resp, int userId)
	{
		//check if userid exist or not
		boolean flag = dbMngr.isUserExist(userId);
		if(!flag)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		boolean isDataUpdated = false;
		//DBManagerUser dbManagerUser = new DBManagerUser();
		Gson gson = new Gson();
		RequestResponseManager reManager = new RequestResponseManager();
		AddTicketBean addTicketBean = new AddTicketBean();
		//read request body
		StringBuffer reqBody = reManager.readRequestBody(req);
		//convert from json to java objects
		System.out.println("request  body :- \n " + reqBody.toString());
		addTicketBean = gson.fromJson(reqBody.toString(), AddTicketBean.class);
		//get event details from eventservice
		String eventData = reManager.getUrlContents(config.getEventserviceurl() + addTicketBean.getEventid());
		System.out.println("Data : " + eventData);
		//convert to json
		EventResponseWithEventId eventBean = gson.fromJson(eventData, EventResponseWithEventId.class);
		System.out.println("event name : " + eventBean.getEventname());
		System.out.println("avail : " + eventBean.getAvail() + ", purchased : " + eventBean.getPurchased()
		+ ", total : " + (eventBean.getAvail() + eventBean.getPurchased()));
		if (eventBean.getAvail() > 0
				&& (addTicketBean.getTickets() > 0)
				&& ((eventBean.getAvail() + eventBean.getPurchased()) >= (eventBean.getPurchased() + addTicketBean.getTickets()))
				&& ((eventBean.getAvail() - addTicketBean.getTickets()) >= 0))
		{
			isDataUpdated = dbMngr.addTickets(userId, addTicketBean.getEventid(), addTicketBean.getTickets());
			//now pass request to event table to update that table
			eventBean.setAvail(eventBean.getAvail() - addTicketBean.getTickets());
			eventBean.setPurchased(eventBean.getPurchased() + addTicketBean.getTickets());

			String updatedEvent = gson.toJson(eventBean);
			System.out.println("updated : " + updatedEvent);
		} else {
			//System.out.println("Ticket cannot be added");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	// transfer ticket
	/**
	 * trtransferTicket - handles transfer of ticket from one user to another
	 * @param req - http servlet request
	 * @param resp - http servlet response
	 * @param userid - user id to send to user service to check for user exist or not
	 */
	public void transferTicket(HttpServletRequest req, HttpServletResponse resp, int userid) 
	{
		//System.out.println(userid);
		Gson gson = new Gson();
		// Tickets tickets = new Tickets();
		RequestResponseManager reManager = new RequestResponseManager();
		TransferTicketBuffer tBuffer = new TransferTicketBuffer();
		StringBuffer reqBody = reManager.readRequestBody(req);
		try
		{
			tBuffer = gson.fromJson(reqBody.toString(), TransferTicketBuffer.class);
		}catch(JsonSyntaxException jse)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// check uerid exist and targetuser
		boolean userflag = dbMngr.isUserExist(userid);
		boolean targetUserFlag = dbMngr.isUserExist(tBuffer.getTargetuser());
		if (!userflag || !targetUserFlag) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// check eventid exist or not
		reManager.createRequest(config.getEventserviceurl(), tBuffer.getEventid(), resp);
		System.out.println(resp.getStatus());
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		dbMngr.transferTicket(tBuffer, userid, resp);
	}

}

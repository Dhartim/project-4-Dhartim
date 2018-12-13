package cs601.project4.EventService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.events.EventException;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;
import cs601.project4.Database.DBManager;
import cs601.project4.Events.EventList;
import cs601.project4.Events.EventPurchaseBean;
import cs601.project4.Events.EventResponseWithEventId;
import cs601.project4.Events.Events;

//POST /purchase/{eventid}
@SuppressWarnings("serial")
public class PurchaseEventTickets extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		Configuration config =  Configuration.getInstance();
		Gson gson = new Gson();
		DBManager dbMngr = new DBManager();
		// set response snd content type
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		RequestResponseManager reManager = new RequestResponseManager();
		EventPurchaseBean eventPurchaseBean = new EventPurchaseBean();
		//read request body
		StringBuffer reqBody = reManager.readRequestBody(req);
		//convert from json into string
		eventPurchaseBean = gson.fromJson(reqBody.toString(), EventPurchaseBean.class);
		//pass eventid from event purchase body to get event details of that event id.
		
		//event id exist or not
		EventResponseWithEventId event = dbMngr.getEventDetailsWithId(eventPurchaseBean.getEventid(), resp);
		System.out.println("event id exists");
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		System.out.println(config.getUserserviceurl() + eventPurchaseBean.getUserid());
		//check userid exist or not and make user request
		reManager.createRequest(config.getUserserviceurl(), eventPurchaseBean.getUserid(), resp);
		System.out.println("user id exist");
		System.out.println(resp.getStatus());
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		//check for avail tickets, num of tickets and purchase tickets and update ticket tables
		if (event.getAvail() > 0 
				&& (eventPurchaseBean.getTickets() > 0)
				&& (event.getAvail() >= eventPurchaseBean.getTickets())
				&& ((event.getAvail() + event.getPurchased()) >= (event.getPurchased() + eventPurchaseBean.getTickets()))
				&& ((event.getAvail() - eventPurchaseBean.getTickets()) >= 0))
				//&& ((event.getNumticket()) == (event.getAvail() + event.getPurchased()))) 
//		if (event.getAvail() > 0 
//				&& (eventPurchaseBean.getTickets() > 0)
//				&& (event.getAvail() >= eventPurchaseBean.getTickets())
//				&& ((event.getNumticket()) >= (event.getPurchased() + eventPurchaseBean.getTickets()))
//				&& ((event.getAvail() - eventPurchaseBean.getTickets()) >= 0)
//				&& ((event.getNumticket()) == (event.getAvail() + event.getPurchased()))) 
		{
			//now move to user service ....call post/userid/ticekts/add
			reManager.createAddTicketRequest(config.getUserserviceurl(), eventPurchaseBean, req ,resp);
			System.out.println(resp.getStatus());
			if(resp.getStatus() != HttpServletResponse.SC_OK)
			{
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			//update event table
			dbMngr.updateEvents(event, eventPurchaseBean);
			resp.getStatus();
			System.out.println(resp.getStatus());
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}

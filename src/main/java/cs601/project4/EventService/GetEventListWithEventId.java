package cs601.project4.EventService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.Database.ConnectionDB;
import cs601.project4.Database.DBManager;

//GET /{eventid}
@SuppressWarnings("serial")
public class GetEventListWithEventId extends HttpServlet
{
	private PreparedStatement statement;
	private ResultSet resultSet;
	Gson gson = new Gson();
	//connection to database
	ConnectionDB conn = ConnectionDB.getInstance();
	Connection connection = conn.connectDatabase();
	DBManager dbMngr = new DBManager();
	//GET /{eventid}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		System.out.println("comes here");
		//set response snd content type
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		//to get eventid from uri
		int eventId=0;
		//check eventid from uri
		checkRequestURL(req, resp);
		//gives event id from uri
		if((req.getRequestURI().split("/").length == 2) && (req.getRequestURI().split("/")[1].matches("\\d+")) && !(req.getRequestURI().split("/")[1].isEmpty()))
		{
			eventId =Integer.parseInt(req.getRequestURI().split("/")[1]);
		}
		//pass response back;
		String json = gson.toJson(dbMngr.getEventDetailsWithId(eventId, resp));
		//send response back to front api
		resp.getWriter().append(json.toLowerCase() + "\n");
		//writer.println();
		//resp.getWriter().append(resp.getStatus());
	}

	public void checkRequestURL(HttpServletRequest req, HttpServletResponse resp)
	{
		if(req.getRequestURI().split("/").length != 2)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		else if(req.getRequestURI().split("/").length == 2)
		{
			if((req.getRequestURI().split("/")[1] == null) || !(req.getRequestURI().split("/")[1].matches("\\d+")))
			{
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
}

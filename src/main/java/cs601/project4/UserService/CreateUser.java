package cs601.project4.UserService;


import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Database.DBManager;
import cs601.project4.Users.Users;

//POST /create
@SuppressWarnings("serial")
public class CreateUser extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter writer;
		StringBuffer readbodyBuffer;
		Users user;
		Gson gson = new Gson();
		DBManager dbManager = new DBManager();
		RequestResponseManager reqRespMgr = new RequestResponseManager();
		String pattern = "^(?=.{3,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		// get data from request body
		readbodyBuffer = reqRespMgr.readRequestBody(req);
		// convert json object into java string;
		user = gson.fromJson(readbodyBuffer.toString(), Users.class);
		// check if username is empty or null or length has to be 4-15
		if (user.getUsername() == null || user.getUsername().isEmpty() || !(user.getUsername().matches(pattern))) 
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		user = dbManager.createUser(user);
		// convert it into json body
		String json = gson.toJson(user, Users.class);
		// send response back to front api
		writer = resp.getWriter().append(json.toLowerCase());
		writer.println();
	}

}

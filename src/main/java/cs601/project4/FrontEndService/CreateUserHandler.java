package cs601.project4.FrontEndService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.project4.Configuration.Configuration;
/**
 *  CreateUserHandler - handles create user api
 * @author dhartimadeka
 *
 */
//POST /users/create
@SuppressWarnings("serial")
public class CreateUserHandler extends HttpServlet
{
	private HttpURLConnection httpConn;
	private InputStream input;
	private Configuration config = Configuration.getInstance();
	//StringBuffer jb;
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		String line = "";
		StringBuffer readerbuffer = new StringBuffer();
		//get body data from request
		BufferedReader reader = req.getReader();
		while ((line = reader.readLine()) != null)
		{	System.out.println(line);
			readerbuffer.append(line);
		}
		URL url = new URL(config.getUserserviceurl()+"create");
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setRequestProperty("Content-Type", "application/json");
		httpConn.setRequestProperty("Accept", "application/json");
		httpConn.setDoOutput(true); // false indicates this is a GET request
		//pass body with request
		OutputStream os = httpConn.getOutputStream();
	    os.write(readerbuffer.toString().getBytes());
	    os.flush();
	    os.close();
		//httpConn.getOutputStream().write(Integer.parseInt(readerbuffer.toString()));
		httpConn.connect();
		resp.setStatus(httpConn.getResponseCode());
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		input = httpConn.getInputStream();
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		StringBuffer responsebuffer = new StringBuffer();
		while ((line = bufferreader.readLine()) != null) 
		{
			responsebuffer.append(line);
		}
		bufferreader.close();
		PrintWriter writer = resp.getWriter().append(responsebuffer);
		//writer.println();
	}
}

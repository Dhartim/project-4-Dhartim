package cs601.project4Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class CreateUserTest 
{
	String[] args = new String[]{"configuration.json"};
	HttpServletRequest request;
	HttpServletResponse response;
	
	@Test
	public void getConnectionTest()
	{
		
	}
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse()  throws Exception 
	{
	    String strUrl = "http://localhost:9090/create";

	    try {
	        URL url = new URL(strUrl);
	        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	        urlConn.setRequestMethod("POST");
	        urlConn.setRequestProperty("Content-Type", "application/json");
	      	assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
	    } catch (IOException e) 
	    {
	        System.err.println("Error creating HTTP connection");
	        e.printStackTrace();
	        throw e;
	    }
	}
//	@Test
//	public void testDoPostHttpServletRequestHttpServletResponse() 
//	{
//		fail("Not yet implemented");
//	}

}

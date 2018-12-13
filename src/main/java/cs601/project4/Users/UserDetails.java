package cs601.project4.Users;

import java.util.HashMap;
import java.util.List;

/**
 * UserDetails - class for details of user request from front end to back end
 * @author dhartimadeka
 *
 */
public class UserDetails {
	private int userid;
	private String username;
	private List<HashMap<String, Integer>> tickets;

	public UserDetails(int userid, String username, List<HashMap<String, Integer>> tickets) {
		super();
		this.userid = userid;
		this.username = username;
		this.tickets = tickets;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<HashMap<String, Integer>> getTickets() {
		return tickets;
	}

	public void setTickets(List<HashMap<String, Integer>> tickets) {
		this.tickets = tickets;
	}

}

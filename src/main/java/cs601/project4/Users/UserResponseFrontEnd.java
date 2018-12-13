package cs601.project4.Users;

import java.util.List;

import cs601.project4.Events.EventResponseWithEventId;

public class UserResponseFrontEnd {
	private int userid;
	private String username;
	private List<EventResponseWithEventId> tickets;

	public UserResponseFrontEnd() {
		super();
	}

	public UserResponseFrontEnd(int userid, String username, List<EventResponseWithEventId> tickets) {
		super();
		this.userid = userid;
		this.username = username;
		this.tickets = tickets;
	}

	public int getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public List<EventResponseWithEventId> getTickets() {
		return tickets;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setTicekts(List<EventResponseWithEventId> tickets) {
		this.tickets = tickets;
	}

}

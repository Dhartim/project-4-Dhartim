package cs601.project4.Tickets;

public class AddTicketBean 
{
	private int userid;
	private int eventid;
	private int tickets;

	public AddTicketBean() {
		
	}

	public AddTicketBean(int eventid, int tickets) {
		this.eventid = eventid;
		this.tickets = tickets;
	}
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getEventid() {
		return eventid;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

}

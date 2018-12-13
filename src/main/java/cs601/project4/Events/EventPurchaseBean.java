package cs601.project4.Events;

/**
 * EventPurchaseBean  - holds purchase ticket request
 * @author dhartimadeka
 *
 */
public class EventPurchaseBean 
{
	private int userid;
	private int eventid;
	private int tickets;
	
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

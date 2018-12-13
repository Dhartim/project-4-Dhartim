package cs601.project4.Tickets;

/**
 * Tickets  - it holds tickets request body
 * @author dhartimadeka
 *
 */
public class Tickets 
{
	private int userId;
	private int eventId;
	private int ticketCount;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}
	
	
}

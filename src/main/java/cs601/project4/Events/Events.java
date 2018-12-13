package cs601.project4.Events;

/**
 * events - holds get list of events response
 * @author dhartimadeka
 *
 */
public class Events 
{
	// column_name = event_id
	// column_no = 1
	private int eventid;
	// column_name = event_name
	// column_no = 2
	private String eventname;
	//userid
	private int userid;
	// column_name = total_ticket
	// column_no = 3
	private int numticket;
	// column_name = available_ticket
	// column_no = 4
	private int avail;
	// column_name = purchased_ticket
	// column_no = 5
	private int purchased;

	public Events(int eventid)
	{
		super();
		this.eventid = eventid;
	}
	

	public Events(int eventId, String eventName, int userid, int numTickets, int avail, int purchased) {
		this.eventid = eventId;
		this.eventname = eventName;
		this.userid = userid;
		this.numticket = numTickets;
		this.avail = avail;
		this.purchased = purchased;
	}

	
//	public Events(int eventid, String eventname, int userid, int avail, int purchased)
//	{
//		super();
//		this.eventId = eventid;
//		this.eventName = eventname;
//		this.userid = userid;
//		this.avail = avail;
//		this.purchased = purchased;
//	}
	
//	public Events(int eventId, String eventName, int numTickets, int availTicket, int purchasedTicket) {
//		//super();
//		this.eventId = eventId;
//		this.eventName = eventName;
//		this.numticket = numTickets;
//		this.avail = availTicket;
//		this.purchased = purchasedTicket;
//	}
	
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


	public String getEventname() {
		return eventname;
	}


	public void setEventname(String eventname) {
		this.eventname = eventname;
	}


	public int getNumticket() {
		return numticket;
	}

	public void setNumticket(int numticket) {
		this.numticket = numticket;
	}

	public int getAvail() {
		return avail;
	}
	public void setAvail(int avail) {
		this.avail = avail;
	}
	public int getPurchased() {
		return purchased;
	}
	public void setPurchased(int purchased) {
		this.purchased = purchased;
	}
	
}

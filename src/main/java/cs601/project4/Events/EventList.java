package cs601.project4.Events;

/**
 * EventList - holds list of events
 * @author dhartimadeka
 *
 */
public class EventList 
{
	private int eventid;
	private String eventname;
	private int userid;
	private int avail;
	private int purchased;

	public EventList(int eventid, String eventname, int userid, int avail, int purchased) {
		//super();
		this.eventid = eventid;
		this.eventname = eventname;
		this.userid = userid;
		this.avail = avail;
		this.purchased = purchased;
	}

	public int getEventid() {
		return eventid;
	}

	public String getEventname() {
		return eventname;
	}

	public int getUserid() {
		return userid;
	}

	public int getAvail() {
		return avail;
	}

	public int getPurchased() {
		return purchased;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public void setAvail(int avail) {
		this.avail = avail;
	}

	public void setPurchased(int purchased) {
		this.purchased = purchased;
	}


}

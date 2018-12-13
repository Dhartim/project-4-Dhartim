package cs601.project4.Events;

public class EventResponseWithEventId 
{
	private int eventid;
	private String eventname;
	private int userid;
	private int avail;
	private int purchased;
	
	public EventResponseWithEventId()
	{
		
	}
	public EventResponseWithEventId(int eventid, String eventname, int userid, int avail, int purchase)
	{
		this.eventid = eventid;
		this.eventname = eventname;
		this.userid = userid;
		this.avail = avail;
		this.purchased = purchase;
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
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public void setPurchased(int purchase) {
		this.purchased = purchase;
	}
	
	
}

package cs601.project4.Events;

public class CreateEventResponse 
{
	private int eventid;
	
	public CreateEventResponse(int eventid)
	{
		this.eventid = eventid;
	}

	public int getEventid() {
		return eventid;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	
}

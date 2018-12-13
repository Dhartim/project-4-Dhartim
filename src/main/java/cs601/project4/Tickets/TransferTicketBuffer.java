package cs601.project4.Tickets;

public class TransferTicketBuffer 
{
	private int eventid;
	private int tickets;
	private int targetuser;
	public TransferTicketBuffer() {
	}

	public TransferTicketBuffer(int eventid, int tickets, int targetuser) {
		this.eventid = eventid;
		this.tickets = tickets;
		this.targetuser = targetuser;
	}
	public int getEventid() 
	{
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
	public int getTargetuser() {
		return targetuser;
	}
	public void setTargetuser(int targetuser) {
		this.targetuser = targetuser;
	}
	
	

}

package cs601.project4.Users;

public class Users {
	private int userId;
	private String username;

	public Users() {
		super();
	}

	public Users(int userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

	public Users(int userId) {
		this.userId = userId;
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

package base.encryption.beans;

public class UserDetail {

	private int id;
	private String username;
	private String password;
	private String landmark;
	private String field;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}

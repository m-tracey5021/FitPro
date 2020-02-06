
import java.util.*;

public class User implements CycleComponent {
	
	private String userId;
	private String userName;
	private String userPass;
	private String userEmail;
	private int membershipType;
	
	
	public User() {
		
	}
	public User(String userName, String userPass, String email) {
		this.userId = UUID.randomUUID().toString();
		this.userName = userName;
		this.userPass = userPass;
		this.userEmail = email;
		this.membershipType = 0;
		
	}
	public User(String userName, String userPass, String email, int membershipType) {
		this.userId = UUID.randomUUID().toString();
		this.userName = userName;
		this.userPass = userPass;
		this.userEmail = email;
		this.membershipType = membershipType;
	}
	public String getId() {
		return this.userId;
	}
	public String getUserName() {
		return this.userName;
	}
	public String getUserPass() {
		return this.userPass;
	}
	public String getEmail() {
		return this.userEmail;
	}
	public int getMembershipType() {
		return this.membershipType;
	}
	public void generateNewId() {
		this.userId = UUID.randomUUID().toString();
	}
	public void setId(String Id){
		this.userId = Id;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public void setUserPass(String pass) {
		this.userPass = pass;	
	}
	public void setEmail(String email) {
		this.userEmail = email;
	}
	public void setMembershipType(int type) {
		this.membershipType = type;
	}
}

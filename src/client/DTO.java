package client;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class DTO implements Serializable {
	private String id=null;
	private String password=null;
	private transient String pass=null;
	private String name=null;
	private int age=0;
	private int ava=0;
	private int point=0;
	private UserStatus.Location location = UserStatus.Location.OTHER;
	private UserStatus.Status status = UserStatus.Status.DEAD;
	private int locationValue=5;
	private int statusValue=0;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void hidePassword() {
		this.pass = this.password;
		this.password = null;
	}
	public int getAvatar() {
		return ava;
	}
	public void setAvatar(int ava) {
		this.ava = ava;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	
	public int getPoint() {
		return point;
	}
	
	public void setLocation(UserStatus.Location location) {
		this.location = location;
	}
	
	public void setLocationValue(int value) {
		this.locationValue = value;
	}
	
	public UserStatus.Location getLocation() {
		return location;
	}
	
	public int getLocationValue() {
		return locationValue;
	}
	
	public void setStatus(UserStatus.Status status) {
		this.status = status;
	}
	
	public void setStatusValue(int value) {
		this.statusValue = value;
	}
	
	public UserStatus.Status getStatus() {
		return status;
	}
	
	public int getStatusValue() {
		return statusValue;
	}
}
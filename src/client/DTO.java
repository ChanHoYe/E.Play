package client;

public class DTO {
	String id=null;
	String password=null;
	String name=null;
	int age=0;
	int ava=0;
	
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
	public int getAvatar() {
		return ava;
	}
	public void setAvatar(int ava) {
		this.ava = ava;
	}
}
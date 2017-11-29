package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import client.Client;

public class Room {
	ArrayList<String> userList = new ArrayList<String>();
	HashMap<String, String> user = new HashMap<String, String>();
	String roomName;
	
	public Room(String id, String name, String roomName) {
		userList.add(id);
		user.put(id, name);
		this.roomName = roomName;
	}
	
	public void EnterRoom(String id, String name) {
		userList.add(id);
		user.put(id, name);
	}
	
	public void ExitRoom(String id) {
		userList.remove(id);
		user.remove(id);
		if(userList.size() < 1) {
			RoomManager.removeRoom(this);
			return;
		}
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomName() {
		return roomName;
	}
}

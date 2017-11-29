package server;

import java.io.Serializable;
import java.util.List;

import client.Client;
import game.Room;

public class RoomData implements Serializable {
	List<Room> roomList;
	String segment;
	Room room;
	String ID;
	
	public RoomData(String segment) {
		this.segment = segment;
	}
	
	public RoomData(List<Room> roomList) {
		this.roomList = roomList;
	}
	
	public RoomData(Room room) {
		this.room = room;
	}
	
	public List<Room> getRoomList() {
		return roomList;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public String getID() {
		return ID;
	}
}

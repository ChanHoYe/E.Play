package game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import client.Client;
import server.RoomData;

public class RoomManager extends JFrame {
	static List<Room> roomList;
	
	public RoomManager() {
		roomList = new ArrayList<Room>();
	}
	
	public static Room createRoom(String ID, String name, String roomName) {
		Room room = new Room(ID, name, roomName);
		roomList.add(room);
		
		return room;
	}
	
	public static void removeRoom(Room room) {
		roomList.remove(room);
	}
	
	public static int roomCount() {
		return roomList.size();
	}
	
	public static RoomData getRoomInfo() {
		RoomData roomData = new RoomData(roomList);
				
		return roomData;
	}
}

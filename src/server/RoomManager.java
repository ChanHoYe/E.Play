package server;

import java.util.Hashtable;

import client.DTO;

public final class RoomManager {
	private static final Hashtable<Integer, Room> roomList = new Hashtable<Integer, Room>();
	
	public void createRoom(DTO dto, String roomName, int roomNum) {
		Room room = new Room(dto, roomName, roomNum);
		roomList.put(roomNum, room);
	}
	
	public void enterRoom(DTO dto, int roomNum) {
		roomList.get(roomNum).enterRoom(dto);
	}
	
	public void exitRoom(int roomNum, String id) {
		roomList.get(roomNum).exitRoom(id);
	}
	
	public static void removeRoom(int roomNum) {
		roomList.remove(roomNum);
	}
	
	public int roomCount() {
		return roomList.size();
	}
	
	public boolean isExist(int roomNum) {
		return roomList.containsKey(roomNum);
	}
	
	public Hashtable<Integer, Room> getList() {
		return roomList;
	}
	
	public Hashtable<Integer, DTO> getUser(int roomNum) {
		return roomList.get(roomNum).getUserList();
	}
}
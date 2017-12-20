package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import client.DTO;
import client.UserStatus;

@SuppressWarnings("serial")
public class GameData implements Serializable {
	DTO dto;
	String type;
	String segment;
	String id;
	String word;
	UserStatus.Location location;
	int roomNum;
	String roomName;
	Hashtable<Integer, Room> roomList;
	Hashtable<Integer, DTO> userList;
	ArrayList<String> usedList;
	int locationValue;
	int statusValue;
		
	public GameData(String type, String segment) {
		this.type = type;
		this.segment = segment;
	}
	
	public GameData(String type, String segment, DTO dto) {
		this.type = type;
		this.segment = segment;
		this.dto = dto;
	}
	
	public GameData(String type, String segment, int roomNum) {
		this.type = type;
		this.segment = segment;
		this.roomNum = roomNum;
	}
	
	public GameData(String type, String segment, int locationValue, int statusValue) {
		this.type = type;
		this.segment = segment;
		this.locationValue = locationValue;
		this.statusValue = statusValue;
	}
	
	public GameData(String type, String segment, DTO dto, String roomName, int roomNum) {
		this.type = type;
		this.segment = segment;
		this.dto = dto;
		this.roomName = roomName;
		this.roomNum = roomNum;
	}
	
	public GameData(String type, String segment, UserStatus.Location location) {
		this.type = type;
		this.segment = segment;
		this.location = location;
	}
	
	public GameData(String type, String segment, Hashtable<Integer, Room> roomList) {
		this.type = type;
		this.segment = segment;
		this.roomList = roomList;
	}
	
	public GameData(String type, Hashtable<Integer, DTO> userList, String segment) {
		this.type = type;
		this.segment = segment;
		this.userList = userList;
	}
	
	public GameData(String type, String segment, String id, String word, int roomNum) {
		this.type = type;
		this.segment = segment;
		this.id = id;
		this.word = word;
		this.roomNum = roomNum;
	}
	
	public GameData(String type, String segment, String id, ArrayList<String> usedList) {
		this.type = type;
		this.segment = segment;
		this.id = id;
		this.usedList = usedList;
	}
	
	public GameData(String type, String segment, String id) {
		this.type = type;
		this.segment = segment;
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSegment() {
		return segment;
	}
	
	public DTO getDTO() {
		return dto;
	}
	
	public UserStatus.Location getLocation() {
		return location;
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public Hashtable<Integer, Room> getRoomList() {
		return roomList;
	}
	
	public Hashtable<Integer, DTO> getUserList() {
		return userList;
	}
	
	public int getLocationValue() {
		return locationValue;
	}
	
	public int getStatusValue() {
		return statusValue;
	}
	
	public String getID() {
		return id;
	}
	
	public String getWord() {
		return word;
	}
	
	public ArrayList<String> getUsedList() {
		return usedList;
	}
}

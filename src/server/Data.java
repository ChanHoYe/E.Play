package server;

import java.io.Serializable;
import java.util.ArrayList;
import client.DTO;
import client.UserStatus;

@SuppressWarnings("serial")
public class Data implements Serializable {
	private String type;
	private String segment;
	private String message;
	private String packet;
	private String roomName;
	private int roomNum;
	private DTO dto;
	private ArrayList<String> name;
	private ArrayList<String> point;
	private ArrayList<String> rank;
	private ArrayList<String> word;
	private ArrayList<String> mean;
	private UserStatus.Location location;
	private int locationValue;
	private int statusValue;
		
	public Data(String type, String segment) {
		this.type = type;
		this.segment = segment;
	}
	
	// 아이디, 비번 찾기에 이용
	public Data(String type, String segment, String packet) {
		this.type = type;
		this.segment = segment;
		this.packet = packet;
	}
	
	// 로그인에 이용
	public Data(String type, String segment, DTO dto) {
		this.type = type;
		this.segment = segment;
		this.dto = dto;
	}
	
	public Data(String type, String segment, String message, DTO dto) {
		this.type = type;
		this.segment = segment;
		this.message = message;
		this.dto = dto;
	}
	
	public Data(String type, String segment, String name, String message) {
		this.type = type;
		this.segment = segment;
		this.packet = name;
		this.message = message;
	}
	
	public Data(String type, String segment, int locationValue, int statusValue) {
		this.type = type;
		this.segment = segment;
		this.locationValue = locationValue;
		this.statusValue = statusValue;
	}
	
	// 랭킹에 이용
	public Data(String type, ArrayList<String> name, ArrayList<String> point, ArrayList<String> rank) {
		this.type = type;
		this.name = name;
		this.point = point;
		this.rank = rank;
	}
	
	// 단어장에 이용
	public Data(String type, ArrayList<String> word, ArrayList<String> mean) {
		this.type = type;
		this.word = word;
		this.mean = mean;
	}
	
	public Data(String type, String segment, String roomName, int roomNum, DTO dto) {
		this.type = type;
		this.segment = segment;
		this.roomName = roomName;
		this.roomNum = roomNum;
		this.dto = dto;
	}
	
	public Data(String type, String segment, int roomNum) {
		this.type = type;
		this.segment = segment;
		this.roomNum = roomNum;
	}
	
	public Data(String type, String segment, int roomNum, DTO dto) {
		this.type = type;
		this.segment = segment;
		this.roomNum = roomNum;
		this.dto = dto;
	}
	
	public Data(String type, String segment, DTO dto, UserStatus.Location location) {
		this.type = type;
		this.segment = segment;
		this.dto = dto;
		this.location = location;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSegment() {
		return segment;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getPacket() {
		return packet;
	}
	
	public DTO getDTO() {
		return dto;
	}
	
	public String getRoomname() {
		return roomName;
	}
	
	public int getRoomnum() {
		return roomNum;
	}
	
	public ArrayList<String> getName() {
		return name;
	}
	
	public ArrayList<String> getPoint() {
		return point;
	}
	
	public ArrayList<String> getRank() {
		return rank;
	}
	
	public ArrayList<String> getWordData() {
		return word;
	}
	
	public ArrayList<String> getMeanData() {
		return mean;
	}
	
	public UserStatus.Location getLocation() {
		return location;
	}
	
	public int getLocationValue() {
		return locationValue;
	}
	
	public int getStatusValue() {
		return statusValue;
	}
}

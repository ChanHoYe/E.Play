package server;

import java.io.Serializable;
import java.util.ArrayList;

import game.Room;

public class Data implements Serializable {
	private String segment;
	private String packet;
	public ArrayList<String> name;
	public ArrayList<String> point;
	public ArrayList<String> rank;
	private Room room;
	
	public Data(String segment) {
		this.segment = segment;
	}
	
	public Data(String segment, String packet) {
		this.segment = segment;
		this.packet = packet;
	}
	
	public Data(ArrayList<String> name, ArrayList<String> point, ArrayList<String> rank) {
		this.name = name;
		this.point = point;
		this.rank = rank;
	}
	
	public Data(String segment, String ID, Room room) {
		this.segment = segment;
		this.packet = ID;
		this.room = room;
	}
	
	public String getSegment() {
		return segment;
	}
	
	public String getData() {
		return packet;
	}
	
	public Room getRoom() {
		return room;
	}
}

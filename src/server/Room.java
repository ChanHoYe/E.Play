package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import client.DTO;
import client.UserStatus;

@SuppressWarnings("serial")
public final class Room implements Serializable {
	private final Hashtable<Integer, DTO> userList = new Hashtable<Integer, DTO>();
	private final Hashtable<String, String> wordList = new Hashtable<String, String>();
	private final ArrayList<String> usedList = new ArrayList<String>();
	private UserStatus.Status status = UserStatus.Status.IDLE;
	private String roomName;
	private int roomNum;
	private volatile int count = 1;
	
	public Room(DTO dto, String roomName, int roomNum) {
		synchronized (userList) {
			userList.put(count++, dto);
		}
		System.out.println(userList.size());
		this.roomName = roomName;
		this.roomNum = roomNum;
	}
	
	public void enterRoom(DTO dto) {
		synchronized (userList) {
			userList.put(count++, dto);
		}
	}
	
	public void exitRoom(String id) {
		int remove=0;
		
		synchronized (userList) {
			for (int temp : userList.keySet()) {
				if (userList.get(temp).getId().equals(id)) {
					userList.remove(temp);
					remove = temp;
					break;
				}
			}

			for (int i = remove + 1; i < count; i++) {
				DTO temp = userList.get(i);
				userList.remove(i);
				userList.put(i - 1, temp);
			}
			count--;
		}
		if(userList.size() < 1) {
			RoomManager.removeRoom(roomNum);
			return;
		}
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public void setStatus(UserStatus.Status status) {
		this.status = status;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public Hashtable<Integer, DTO> getUserList() {
		return userList;
	}
	
	public UserStatus.Status getStatus() {
		return status;
	}
	
	public int getStatusValue() {
		return status.getValue();
	}
	
	public ArrayList<String> getUsedList() {
		return usedList;
	}
	
	public boolean checkAnswer(String word, String id) {
		boolean valid = true;
		
		if(!usedList.isEmpty()) {
			Iterator<String> iter = usedList.iterator();
			
			while(iter.hasNext()) {
				if(word.equalsIgnoreCase(iter.next())) {
					valid = false;
					break;
				}				
			}
			
			if(valid) {
				String temp = usedList.get(usedList.size()-1).toLowerCase();
				if(word.toLowerCase().charAt(0) == temp.charAt(temp.length()-1)) {
					usedList.add(word);
				} else {
					valid = false;
				}
			}
		} else {
			usedList.add(word);
		}
		
		if(!valid) {
			int temp=0;
			synchronized (userList) {
				for (int tmp : userList.keySet()) {
					if(userList.get(tmp).getId().equals(id)) {
						userList.remove(tmp);
						temp = tmp;
					}
				}
				for(int i=temp+1;i<count;i++) {
					DTO dto = userList.get(i);
					userList.remove(i);
					userList.put(i-1, dto);
				}
			}
			count--;
		}
		
		return valid;
	}
	
	public void startGame() {
		this.status = UserStatus.Status.GAME;
	}
	
	public void stopGame() {
		this.status = UserStatus.Status.IDLE;
	}
}
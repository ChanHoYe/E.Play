package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import client.DTO;
import client.UserStatus;

public class GameThread extends Thread {	
	private static final Hashtable<ObjectOutputStream, DTO> userList = new Hashtable<ObjectOutputStream, DTO>();
	private static final RoomManager roomManager = new RoomManager();
	private static ArrayList<String> room1 = new ArrayList<String>();
	private static ArrayList<String> room2 = new ArrayList<String>();
	private static ArrayList<String> room3 = new ArrayList<String>();
	private static ArrayList<String> room4 = new ArrayList<String>();
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public GameThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
				GameData data = (GameData)in.readObject();
				String type = data.getType();
				String segment = data.getSegment();
				
				if(type.equals("ROOM")) {
					getRoom(segment, data);
				} else if(type.equals("GAME")) {
					getGame(segment, data);
				} else if(type.equals("ENTER")) {
					synchronized(userList) {
						userList.put(out, data.getDTO());
					}
				} else if(type.equals("UPDATE")) {
					int locationValue = data.getLocationValue();
					int statusValue = data.getStatusValue();
					synchronized(userList) {
						userList.get(out).setLocationValue(locationValue);
						userList.get(out).setStatusValue(statusValue);
					}
				} else if(type.equals("OUT")) {
					synchronized(userList) {
						if(userList.containsKey(out))
							userList.remove(out);
					}
					sleepThread();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sleepThread() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void getGame(String segment, GameData data) {
		GameData writeData;
		try {
			if(segment.equals("START")) {
				int roomNum = userList.get(out).getLocationValue();
				roomManager.getList().get(roomNum).setStatus(UserStatus.Status.GAME);
				synchronized(userList) {
					for(ObjectOutputStream stream : userList.keySet()) {
						if(userList.get(stream).getLocationValue() == userList.get(out).getLocationValue()) {
							writeData = new GameData("GAME", "START");
							stream.writeObject(writeData);
							stream.flush();
						}
					}
				}
			} else if(segment.equals("STOP")) {
				synchronized(userList) {
					switch (userList.get(out).getLocationValue()) {
					case 1:
						room1.clear();
						break;
					case 2:
						room2.clear();
						break;
					case 3:
						room3.clear();
						break;
					case 4:
						room4.clear();
						break;
					default:
						break;
					}
					
					
				}
			} else if(segment.equals("CHECK")) {
				String word = data.getWord();
				int roomNum = data.getRoomNum();
				String id = data.getID();
				
				boolean valid = roomManager.getList().get(roomNum).checkAnswer(word, id);
				
				if(valid) {
					synchronized (userList) {
						writeData = new GameData("GAME", "LIST", id, roomManager.getList().get(roomNum).getUsedList());
						for (ObjectOutputStream stream : userList.keySet()) {
							if (userList.get(stream).getLocationValue() == roomNum) {
								stream.writeObject(writeData);
								stream.flush();
							}
						}
					}
				} else {
					synchronized (userList) {
						writeData = new GameData("GAME", "FAIL", id);
						for (ObjectOutputStream stream : userList.keySet()) {
							if (userList.get(stream).getLocationValue() == roomNum) {
								stream.writeObject(writeData);
								stream.flush();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void getRoom(String segment, GameData data) {
		GameData writeData;
		try {
			if (segment.equals("CREATE")) {
				int roomNum = data.getRoomNum();
				String roomName = data.getRoomName();

				if (!roomManager.isExist(roomNum)) {
					roomManager.createRoom(data.getDTO(), roomName, roomNum);
					System.out.println(roomNum +" " +1);
					synchronized (userList) {
						for (ObjectOutputStream stream : userList.keySet()) {
							if (userList.get(stream).getLocationValue() == 0) {
								if (stream != out) {
									writeData = new GameData("ROOM", "LIST", roomManager.getList());
									stream.writeObject(writeData);
									stream.flush();
								}
							}
						}
					}
				}
			} else if (segment.equals("ENTER")) {
				int roomNum = data.getDTO().getLocationValue();
				
				if (roomManager.isExist(roomNum)) {
					roomManager.enterRoom(data.getDTO(), roomNum);
					
					synchronized (userList) {
						for (ObjectOutputStream stream : userList.keySet()) {
							if (userList.get(stream).getLocationValue() == roomNum) {
								if (!stream.equals(out)) {
									writeData = new GameData("ROOM", roomManager.getUser(roomNum), "USER");
									stream.writeObject(writeData);
									stream.flush();
								}
							}
						}
					}
				} else {
					writeData = new GameData("ROOM", "FAIL");
					out.writeObject(writeData);
					out.flush();
				}				
			} else if (segment.equals("EXIT")) {
				int roomNum = data.getDTO().getLocationValue();
				
				if (roomManager.isExist(roomNum)) {
					roomManager.getList().get(roomNum).exitRoom(data.getDTO().getId());
				}
				synchronized(userList) {
					for (ObjectOutputStream stream : userList.keySet()) {
						if (stream != out) {
							if (userList.get(stream).getLocationValue() == roomNum) {
								writeData = new GameData("ROOM", roomManager.getUser(roomNum), "USER");
								stream.writeObject(writeData);
								stream.flush();
							}
							if (userList.get(stream).getLocationValue() == 0) {
								writeData = new GameData("ROOM", "LIST", roomManager.getList());
								stream.writeObject(writeData);
								stream.flush();
							}
						}
					}
				}
			} else if (segment.equals("UPDATELIST")) {
				writeData = new GameData("ROOM", "LIST", roomManager.getList());
				out.writeObject(writeData);
				out.flush();
			} else if (segment.equals("UPDATEUSER")) {
				int roomNum = data.getRoomNum();
				
				writeData = new GameData("ROOM", roomManager.getUser(roomNum), "USER");
				System.out.println(roomManager.getUser(roomNum).size() + " " + roomNum);
				out.writeObject(writeData);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

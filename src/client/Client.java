package client;

import java.io.IOException;

import game.RoomUI;
import game.Wait;
import login.Avatar;
import login.Find;
import login.Join;
import login.Login;
import main.Main;
import main.Rank;
import main.WordBook;
import server.Data;
import server.GameData;

public class Client {
	private Login login;
	private Main main;
	private WordBook wordbook;
	private Wait wait;
	private RoomUI roomUI;
	private Avatar avatar;
	private Find find;
	private Join join;
	private Rank rank;
	
	private DTO dto;
	
	private Thread dataThread;
	private Thread gameThread;
	
	private SocketInfo info;
	
	LoginInterface loginI = new Interface();
	DataInterface dataI = new Interface();
	ChatInterface chatI = new Interface();
	RoomInterface roomI = new Interface();
	
	public static void main(String[] args) {
		Client client = new Client();
		client.init();
		client.startThread();
	}
	
	private void init() {
		info = new SocketInfo();
		login = new Login(this);
	}
	
	private void startThread() {
		dataThread = new Thread(new dataThread());
		gameThread = new Thread(new gameThread());
		dataThread.start();
		gameThread.start();
	}
	
	@SuppressWarnings("deprecation")
	private void stopThread() {
		dataThread.stop();
		gameThread.stop();
	}
	
	public void terminate(String seg) {
		this.stopThread();
		this.closeSocket(seg);
		System.exit(0);
	}
	
	public void setDTO(DTO dto) {
		this.dto = dto;
	}
	
	public DTO getDTO() {
		return dto;
	}
	
	public void exeLogin() {
		login = new Login(this);
	}
	
	public void exeMain() {
		main = new Main(this);
	}
	
	public void exeWordbook() {
		wordbook = new WordBook(this);
	}
	
	public void exeWait() {
		dto.setLocation(UserStatus.Location.WAIT);
		dto.setLocationValue(0);
		dto.setStatus(UserStatus.Status.IDLE);
		dto.setStatusValue(1);
		dataI.updateUser(info, 0, 1);
		wait = new Wait(this);
		chatI.notifyEnter(info, dto);
		roomI.getRoomList(info);
	}
	
	public void exeRoomUI(boolean valid, String roomName, UserStatus.Location location, int locationValue) {
		dto.setLocation(location);
		dto.setLocationValue(locationValue);
		dataI.updateUser(info, locationValue, 1);
		if(valid)
			roomI.createRoom(info, roomName, dto);
		else
			roomI.enterRoom(info, dto);
		roomUI = new RoomUI(this);
		chatI.notifyEnter(info, dto);
		roomI.getUserList(info, locationValue);
	}
	
	public void exeAvatar() {
		avatar = new Avatar(this);
	}
	
	public void exeFind() {
		find = new Find(this);
	}
	
	public void exeJoin() {
		join = new Join(this);
	}
	
	public void exeRank() {
		rank = new Rank(this);
	}
	
	public void termLogin() {
		login.dispose();
		dataI.notifyServer(info, dto);
	}
	
	public void termMain() {
		main.dispose();
	}
	
	public void termWordbook() {
		wordbook.dispose();
	}
	
	public void termWait(boolean valid) {
		wait.dispose();
		chatI.notifyExit(info, dto);
		if(valid) {
			dto.setLocation(UserStatus.Location.OTHER);
			dto.setStatus(UserStatus.Status.DEAD);
			dataI.updateUser(info, 5, 0);
		}
	}
	
	public void termRoomUI() {
		roomUI.dispose();
		chatI.notifyExit(info, dto);
		roomI.exitRoom(info, dto);
	}
	
	public void termAvatar() {
		avatar.dispose();
	}
	
	public void termFind() {
		find.dispose();
	}
	
	public void termJoin() {
		join.dispose();
	}
	
	public void termRank() {
		rank.dispose();
	}
	
	public void setInfo(SocketInfo info) {
		this.info = info;
	}
	
	public SocketInfo getInfo() {
		return info;
	}
	
	private void closeSocket(String seg) {
		if (seg.equals("LOGIN")) {
			info.closeSocket();
		} else {
			dataI.notifyTerm(info, dto);
			info.closeSocket();
		}
	}
	
	class dataThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Data data = (Data) info.getDataInput().readObject();
					String type = data.getType();
					if(type.equals("LOGIN")) {
						login.setData(data);
					} else if(type.equals("JOIN")) {
						join.setData(data);
					} else if(type.equals("FIND")) {
						find.setData(data);
					} else if(type.equals("RANK")) {
						rank.setData(data);
					} else if(type.equals("WORD")) {
						wordbook.setListData(data);
					} else if(type.equals("CHAT")) {
						switch(dto.getLocation()) {
						case WAIT:
							if(wait != null)
								wait.setMessage(data);
							break;
						case OTHER:
							break;
						default:
							if(roomUI != null)
								roomUI.setMessage(data);
							break;
						}
					} else if(type.equals("OUT")) {
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class gameThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					GameData data = (GameData) info.getGameInput().readObject();
					String type = data.getType();
					if(type.equals("ROOM")) {
						switch(dto.getLocation()) {
						case WAIT:
							if(wait != null)
								wait.setRoomList(data);
							break;
						case OTHER:
							break;
						default:
							if(roomUI != null)
								roomUI.setData(data);
							break;
						}
					} else if(type.equals("GAME")) {
						roomUI.setData(data);
					} else if(type.equals("OUT")) {
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				
			}
			
		}
		
	}
}
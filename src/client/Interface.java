package client;

import java.io.IOException;
import server.Data;
import server.GameData;

public class Interface implements ChatInterface, DataInterface, LoginInterface, RoomInterface, GameInterface {
	/* Chat Interface */
	@Override
	public void toMessage(SocketInfo info, DTO dto, String message) {
		Data data = new Data("CHAT", "MESSAGE", message, dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyEnter(SocketInfo info, DTO dto) {
		Data data = new Data("CHAT", "ENTER", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyExit(SocketInfo info, DTO dto) {
		Data data = new Data("CHAT", "EXIT", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Login Interface */	
	@Override
	public void login(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "LOGIN", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void join(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "JOIN", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void checkID(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "CHECK", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void findID(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "FINDID", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void findPW(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "FINDPW", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setAvatar(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "AVATAR", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Data Interface */
	@Override
	public void getRank(SocketInfo info) {
		Data data = new Data("DATA", "RANK");
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getWord(SocketInfo info) {
		Data data = new Data("DATA", "WORD");
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateUser(SocketInfo info, int locationValue, int statusValue) {
		Data data = new Data("DATA", "UPDATE", locationValue, statusValue);
		GameData gamedata = new GameData("UPDATE", "UPDATE", locationValue, statusValue);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
			info.getGameOutput().writeObject(gamedata);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyServer(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "ENTER", dto);
		GameData gamedata = new GameData("ENTER", "ENTER", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
			info.getGameOutput().writeObject(gamedata);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyTerm(SocketInfo info, DTO dto) {
		Data data = new Data("DATA", "OUT", dto);
		GameData gamedata = new GameData("OUT", "OUT", dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
			info.getGameOutput().writeObject(gamedata);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Room Interface */	
	@Override
	public void enterRoom(SocketInfo info, DTO dto) {
		GameData data = new GameData("ROOM", "ENTER", dto);

		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void createRoom(SocketInfo info, String roomName, DTO dto) {
		GameData data = new GameData("ROOM", "CREATE", dto, roomName, dto.getLocationValue());
		
		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exitRoom(SocketInfo info, DTO dto) {
		GameData data = new GameData("ROOM", "EXIT", dto);

		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void terminateRoom(SocketInfo info, DTO dto, int roomNum) {
		Data data = new Data("ROOM", "TERMINATE", roomNum, dto);
		
		try {
			info.getDataOutput().writeObject(data);
			info.getDataOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getUserList(SocketInfo info, int roomNum) {
		GameData data = new GameData("ROOM", "UPDATEUSER", roomNum);
		
		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void getRoomList(SocketInfo info) {
		GameData data = new GameData("ROOM", "UPDATELIST");
		
		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Game Interface */
	@Override
	public void checkWord(SocketInfo info, String word, int roomNum, DTO dto) {
		GameData data = new GameData("GAME", "CHECK", dto.getId(), word, roomNum);
		
		try {
			info.getGameOutput().writeObject(data);
			info.getGameOutput().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startGame(SocketInfo info) {
		
	}

	@Override
	public void endGame(SocketInfo info) {
		
	}
}

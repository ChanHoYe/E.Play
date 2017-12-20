package client;

public interface RoomInterface {
	void enterRoom(SocketInfo info, DTO dto);
	void createRoom(SocketInfo info, String roomName, DTO dto);
	void exitRoom(SocketInfo info, DTO dto);
	void terminateRoom(SocketInfo info, DTO dto, int roomNum);
	void getUserList(SocketInfo info, int roomNum);
	void getRoomList(SocketInfo info);
}

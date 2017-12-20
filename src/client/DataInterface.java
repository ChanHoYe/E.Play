package client;

public interface DataInterface {
	void getRank(SocketInfo info);
	void getWord(SocketInfo info);
	void updateUser(SocketInfo info, int locationValue, int statusValue);
	void notifyServer(SocketInfo info, DTO dto);
	void notifyTerm(SocketInfo info, DTO dto);
}

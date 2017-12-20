package client;

public interface LoginInterface {
	void login(SocketInfo info, DTO dto);
	void join(SocketInfo info, DTO dto);
	void checkID(SocketInfo info, DTO dto);
	void findID(SocketInfo info, DTO dto);
	void findPW(SocketInfo info, DTO dto);
	void setAvatar(SocketInfo info, DTO dto);
}

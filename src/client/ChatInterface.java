package client;

public interface ChatInterface {
	void toMessage(SocketInfo info, DTO dto, String message);
	void notifyEnter(SocketInfo info, DTO dto);
	void notifyExit(SocketInfo info, DTO dto);
}
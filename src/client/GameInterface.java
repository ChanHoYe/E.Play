package client;

public interface GameInterface {
	void startGame(SocketInfo info);
	void checkWord(SocketInfo info, String word, int roomNum, DTO dto);
	void endGame(SocketInfo info);
}

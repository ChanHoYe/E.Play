package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

import game.Room;
import login.Login;
import server.Data;
import server.RoomData;
import server.WordData;

public class Client {
	public static Socket Socket = null;
	public static Socket ChatSocket = null;
	static ObjectOutputStream outToServer = null;
	static ObjectInputStream inFromServer = null;
	public static PrintWriter out = null;
	public static BufferedReader in = null;
	
	public static void main(String[] args) {
		run();
		System.out.println("클라이언트 동작");
		Login login = new Login();
	}
	
	private static void run() {
		String serverAddress;
		
		try {
			serverAddress = java.net.InetAddress.getLocalHost().getHostAddress();
			Socket = new Socket(serverAddress, 9001);
			ChatSocket = new Socket(serverAddress, 9002);
			outToServer = new ObjectOutputStream(Socket.getOutputStream());
			inFromServer = new ObjectInputStream(Socket.getInputStream());
			out = new PrintWriter(ChatSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(ChatSocket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void toMessage(DTO dto, int roomNum, String message) {
		out.println("MESSAGE " + roomNum + " " + dto.getName() + " " + message);
	}
	
	public static void toWhisper(DTO dto, String dest, String message) {
		out.println("WHISPER " + dto.getName() + " " + dest + " " + message);
	}
	
	public static void enterRoom(DTO dto, int roomNum) {
		out.println("ENTER " + roomNum + " " + dto.getId() + " " + dto.getName());
	}
	
	public static void exitRoom(DTO dto, int roomNum) {
		out.println("EXIT " + roomNum + " " + dto.getId() + " " + dto.getName());
	}
	
	public static void exit(DTO dto, int roomNum) {
		out.println("TERMINATE " + roomNum + " " + dto.getId() + " " + dto.getName());
	}
	
	public static Data checkID(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getId() + " " + dto.getPassword();
		Data data = new Data("LOGIN", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static Data repuID(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getId();
		Data data = new Data("CHECK", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static Data logOut(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getId();
		Data data = new Data("OUT", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static Data insertInfo(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getName() + " " + dto.getAge() + " " + dto.getId() + " " + dto.getPassword();
		Data data = new Data("JOIN", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static Data findID(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getName() + " " + dto.getAge();
		Data data = new Data("FIND", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static Data findPW(DTO dto) throws IOException, ClassNotFoundException {
		String str = dto.getName() + " " + dto.getAge() + " " + dto.getId();
		Data data = new Data("FIND", str);
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static void insertAvatar(DTO dto) throws IOException {
		String str = dto.getId() + " " + dto.getPassword() + " " +dto.getAvatar();
		Data data = new Data("AVATAR", str);
		outToServer.writeObject(data);
		outToServer.flush();
	}
	
	public static Data getRank() throws IOException, ClassNotFoundException {
		Data data = new Data("RANK");
		outToServer.writeObject(data);
		outToServer.flush();
		data = (Data)inFromServer.readObject();
		
		return data;
	}
	
	public static RoomData getRoomList() throws IOException, ClassNotFoundException {
		Data data = new Data("LIST", "NULL");
		outToServer.writeObject(data);
		outToServer.flush();
		RoomData roomData = (RoomData)inFromServer.readObject();
		
		return roomData;
	}
	
	public static WordData getWord() throws IOException, ClassNotFoundException {
		Data data = new Data("WORDBOOK");
		outToServer.writeObject(data);
		outToServer.flush();
		WordData wordData = (WordData)inFromServer.readObject();
		return wordData;
	}
	
	public static void enterRoom(DTO dto, Room room) throws IOException {
		Data data = new Data("ENTERROOM", dto.getId() + " " + dto.getName(), room);
		outToServer.writeObject(data);
		outToServer.flush();
	}
	
	public static RoomData createRoom(DTO dto, String roomName) throws IOException, ClassNotFoundException {
		Data data = new Data("CREATEROOM", dto.getId() + " " + dto.getName() + " " + roomName);
		
		outToServer.writeObject(data);
		outToServer.flush();
		RoomData roomData = (RoomData)inFromServer.readObject();
		
		return roomData;
	}
	
	public static void exitRoom(DTO dto, Room room) throws IOException {
		Data data = new Data("EXITROOM", dto.getId(), room);
		
		outToServer.writeObject(data);
		outToServer.flush();
	}
}

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

public final class SocketInfo {
	String serverAddress = null;
	private Socket dataSocket = null;
	private Socket gameSocket = null;
	private ObjectOutputStream dataOut = null;
	private ObjectInputStream dataIn = null;
	private ObjectOutputStream gameOut = null;
	private ObjectInputStream gameIn = null;
	
	public SocketInfo() {
		try {
			serverAddress = java.net.InetAddress.getLocalHost().getHostAddress();
			//serverAddress = "172.20.10.3";
			dataSocket = new Socket(serverAddress, 9001);
			gameSocket = new Socket(serverAddress, 9002);
			dataOut = new ObjectOutputStream(dataSocket.getOutputStream());
			dataIn = new ObjectInputStream(dataSocket.getInputStream());
			gameOut = new ObjectOutputStream(gameSocket.getOutputStream());
			gameIn = new ObjectInputStream(gameSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObjectOutputStream getDataOutput() {
		return dataOut;
	}
	
	public ObjectInputStream getDataInput() {
		return dataIn;
	}
	
	public ObjectOutputStream getGameOutput() {
		return gameOut;
	}
	
	public ObjectInputStream getGameInput() {
		return gameIn;
	}
	
	public void closeSocket() {
		try {
			dataIn.close();
			dataOut.close();
			gameIn.close();
			gameOut.close();
			dataSocket.close();
			gameSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

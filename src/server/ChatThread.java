package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ChatThread extends Thread {
	private static final int PORT = 9002;
	
	private static HashMap<String, String> user = new HashMap<String, String>();
	private static HashSet<PrintWriter> user_socket = new HashSet<PrintWriter>();
	private static HashMap[] room;
	private static HashSet<PrintWriter> room_socket[];
	
	private String id;
	private String name;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public static void createMap() {
		for(int i=0;i<5;i++) {
			room[i] = new HashMap<String, String>();
			room_socket[i] = new HashSet<PrintWriter>();
		}
	}
	
	public ChatThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			String temp = null;
			Boolean valid = false;
			// Create character streams for the socket.
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			while (true) {
				String info[] = in.readLine().split(" ");
				
				if (info.length != 2) {
					return;
				}
				
				this.id = info[0];
				this.name = info[1];
				
				synchronized (user) {
					if (!user.containsKey(info[0])) {
						user.put(info[0], info[1]);
						break;
					}
				}
			}
			
			user_socket.add(out);
			
			while (true) {
				String input = in.readLine();

				if (input == null) {
					return;
				}
				
				String tok[] = input.split(" ");
				
				if(tok[0].equals("ENTER")) {
					int roomNum = Integer.parseInt(tok[1]);
					
					room[roomNum].put(tok[2], tok[3]);
					room_socket[roomNum].add(out);
					
					for(PrintWriter writer : room_socket[roomNum]) {
						writer.println(tok[3] + "님이 입장하셨습니다.");
					}
				}
				else if(tok[0].equals("EXIT")) {
					int roomNum = Integer.parseInt(tok[1]);
					
					room[roomNum].remove(tok[2]);
					room_socket[roomNum].remove(out);
					
					for(PrintWriter writer : room_socket[roomNum]) {
						writer.println(tok[3] + "님께서 퇴장하셨습니다.");
					}
				}
				else if(tok[0].equals("TERMINATE")) {
					int roomNum = Integer.parseInt(tok[1]);
					
					room[roomNum].remove(tok[2]);
					user.remove(tok[2]);
					room_socket[roomNum].remove(out);
					user_socket.remove(out);
					
					for(PrintWriter writer : room_socket[roomNum]) {
						writer.println(tok[3] + "님께서 게임을 종료하셨습니다.");
					}
				}
				else if(tok[0].equals("MESSAGE")) {
					int roomNum = Integer.parseInt(tok[1]);
					for(PrintWriter writer : room_socket[roomNum]) {
						writer.println(tok[2] + " : " + tok[3]);
					}
				}
				else if(tok[0].equals("WHISPER")) {
					if (!tok[1].equalsIgnoreCase(tok[2])) {
						for (PrintWriter writer : user_socket) {
							writer.println("WHISPER " + tok[1] + " " + tok[2] + " " + tok[3]);
						}
					}
					else {
						
					}
				}
				
				if (valid) {
					if (!temp.contains(id)) {
						out.println("OWN " + id + " -> " + temp.substring(1, temp.length() - 2) + ": "
								+ input.substring(temp.length() + 1));
						
						for (PrintWriter writer : user_socket) {
							writer.println("WHISPER " + temp.substring(1, temp.length() - 2) + id + ": "
									+ input.substring(temp.length() + 1));
						}
					} else {
						out.println("OWN 자신에게 보낼 수 없습니다.");
					}
					valid = false;
				} else {
					if (input.substring(0, 1).equals("<") && input.contains("/>")) {
						out.println("ERROR " + "상대가 존재하지 않습니다.");
					}

					else {
						for (PrintWriter writer : user_socket) {
							writer.println("MESSAGE " + id + ": " + input);
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			for (PrintWriter writer : user_socket) {
				writer.println("EXIT " + "<" + id + "> 님이 퇴장하셨습니다.");
			}
			if (id != null) {
				String s = null;
				Iterator<String> iter = IDs.iterator();
				while (iter.hasNext()) {
					s = iter.next();
					if (s.contains(id)) {
						break;
					}
				}
				IDs.remove(s);
				IDs.remove(id);
			}
			if (out != null) {
				user_socket.remove(out);
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
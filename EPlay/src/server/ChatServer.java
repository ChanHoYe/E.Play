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

public class ChatServer {
	private static final int PORT = 9002;
	
	private static HashSet<String> IDs = new HashSet<String>();
	private static HashSet<String> wait = new HashSet<String>();
	private static HashSet<String> room1 = new HashSet<String>();
	private static HashSet<String> room2 = new HashSet<String>();
	private static HashSet<String> room3 = new HashSet<String>();
	private static HashSet<String> room4 = new HashSet<String>();
	private static HashMap<String, String> map = new HashMap<String, String>();
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	
	public static void main(String[] args) throws Exception {
		System.out.println("채팅서버 동작 중..");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * A handler thread class. Handlers are spawned from the listening loop and are
	 * responsible for a dealing with a single client and broadcasting its messages.
	 */
	private static class Handler extends Thread {
		private String id;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		
		public Handler(Socket socket) {
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
					id = in.readLine();
					if (id == null) {
						return;
					}
					synchronized (IDs) {
						if (!IDs.contains(id)) {
							IDs.add(id);
							break;
						}
					}
				}
				
				writers.add(out);
				
				for (PrintWriter writer : writers) {
					writer.println("ENTRANCE " + "<" + id + "> 님이 입장하셨습니다.");
				}
				
				while (true) {
					String input = in.readLine();

					if (input == null) {
						return;
					}
				
					Iterator<String> iter = IDs.iterator();
					while (iter.hasNext()) {
						String s = iter.next();
						if (input.startsWith(s)) {
							temp = s;
							valid = true;
						}
					}

					if (valid) {
						if (!temp.contains(id)) {
							out.println("OWN " + id + " -> " + temp.substring(1, temp.length() - 2) + ": "
									+ input.substring(temp.length() + 1));
							
							for (PrintWriter writer : writers) {
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
							for (PrintWriter writer : writers) {
								writer.println("MESSAGE " + id + ": " + input);
							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				for (PrintWriter writer : writers) {
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
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
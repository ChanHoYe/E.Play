package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	public static void main(String[] args) {
		/*ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("데이터베이스 서버 동작 중...");
			while(true) {
				Socket socket;
				
				socket = serverSocket.accept();
				threadPool.execute(new ServerThread(socket));
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			threadPool.shutdown();
			while(!threadPool.isTerminated()) {}
			System.out.println("서버가 종료되었습니다.");
		}*/
		JFrame serverFrame = new ServerFrame();
		
	}	
}

class ServerFrame extends JFrame implements ActionListener {
	private static final int PORT = 9001;
	private static final int CHATPORT = 9002;
	private static final int THREAD_CNT = 10;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
	private static ServerSocket dataSocket;
	private static ServerSocket chatSocket;
	
	public static JTextArea textArea = new JTextArea(10, 40);
	
	DataServer dataServer;
	ChatServer chatServer;
	JButton dataStart;
	JButton dataStop;
	JButton chatStart;
	JButton chatStop;
	
	Thread data;
	Thread chat;
	
	ServerFrame() {
		JPanel btnPanel = new JPanel(new GridLayout(2,2));
		JPanel textPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		setSize(600,300);
		setResizable(false);
		dataStart = new JButton("데이터서버 시작");
		dataStop = new JButton("데이터서버 중단");
		chatStart = new JButton("채팅서버 시작");
		chatStop = new JButton("채팅서버 중단");
		dataStart.addActionListener(this);
		dataStop.addActionListener(this);
		chatStart.addActionListener(this);
		chatStop.addActionListener(this);
		dataStop.setEnabled(false);
		chatStop.setEnabled(false);
		scrollPane.setViewportView(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		btnPanel.add(dataStart);
		btnPanel.add(dataStop);
		btnPanel.add(chatStart);
		btnPanel.add(chatStop);
		textArea.setText("");
		add(btnPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
		
		setVisible(true);
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == dataStart) {
			StartServer("Data");
			dataStart.setEnabled(false);
			dataStop.setEnabled(true);
		}
		
		else if(obj == dataStop) {
			StopServer("Data");
			dataStop.setEnabled(false);
			dataStart.setEnabled(true);
		}
		
		else if(obj == chatStart) {
			StartServer("Chat");
			chatStart.setEnabled(false);
			chatStop.setEnabled(true);
		}
		
		else if(obj == chatStop) {
			StopServer("Chat");
			chatStop.setEnabled(false);
			chatStart.setEnabled(true);
		}
	}
	
	public void StartServer(String type) {
		if (type.equals("Data")) {
			dataServer = new DataServer();
			data = new Thread(dataServer);
			data.start();
		} else if (type.equals("Chat")) {
			chatServer = new ChatServer();
			chat = new Thread(chatServer);
			chat.start();
		}
	}
	
	public void StopServer(String type) {
		if (type.equals("Data")) {
			data.interrupt();
			//dataServer.exit();
			//data.stop();
		} else if (type.equals("Chat")) {
			chatServer.exit();
			chat.stop();	
		}
	}
	
	class DataServer implements Runnable {
		public List<ServerThread> clients = new ArrayList<>();
		private volatile boolean isExit = false;
		@Override
		public void run() {
			try {
				dataSocket = new ServerSocket(PORT);
				textArea.append("데이터베이스 서버 동작 중...\n");
				while(true) {
					synchronized(this) {
						if(isExit) {
							break;
						}
					}
					Socket socket = dataSocket.accept();
					threadPool.execute(new ServerThread(socket));
					textArea.append(socket.toString() + "이 데이터서버에 연결되었습니다.\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void exit() {
			threadPool.shutdown();
			while(!threadPool.isTerminated()) {}
			try {
				dataSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("서버가 종료되었습니다.\n");
		}
	}
	
	class ChatServer extends Thread {
		private boolean valid = true;
		
		@Override
		public void run() {
			try {
				chatSocket = new ServerSocket(CHATPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("채팅서버 동작 중\n");
			ChatThread.createMap();
			while (!chatSocket.isClosed()) {
				try {
					Socket socket = chatSocket.accept();
					new ChatThread(socket);
					/*if(valid) {
						ChatThread.createMap();
						valid = false;
					}*/
					textArea.append(socket.toString() + "이 채팅서버에 연결되었습니다.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void exit() {
			try {
				chatSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("채팅서버 종료\n");
		}
	}
}


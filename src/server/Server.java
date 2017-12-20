package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	public static void main(String[] args) {
		new ServerFrame();
	}	
}

@SuppressWarnings("serial")
class ServerFrame extends JFrame implements ActionListener {
	private static final int DATAPORT = 9001;
	private static final int GAMEPORT = 9002;
	private static final int THREAD_CNT = 10;
	private ExecutorService dataThreadPool = null;
	private ExecutorService gameThreadPool = null;
	private ServerSocket dataSocket = null;
	private ServerSocket gameSocket = null;
	
	public static JTextArea textArea = new JTextArea(10, 40);
	
	DataServer dataServer = null;
	GameServer gameServer = null;
	JButton dataStart = null;
	JButton dataStop = null;
	JButton gameStart = null;
	JButton gameStop = null;
	
	Thread data = null;
	Thread game = null;
	
	ServerFrame() {
		JPanel btnPanel = new JPanel(new GridLayout(2,2));
		JScrollPane scrollPane = new JScrollPane();
		setSize(600,300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		dataStart = new JButton("�����ͼ��� ����");
		dataStop = new JButton("�����ͼ��� �ߴ�");
		gameStart = new JButton("���Ӽ��� ����");
		gameStop = new JButton("���Ӽ��� �ߴ�");
		dataStart.addActionListener(this);
		dataStop.addActionListener(this);
		gameStart.addActionListener(this);
		gameStop.addActionListener(this);
		dataStop.setEnabled(false);
		gameStop.setEnabled(false);
		scrollPane.setViewportView(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		btnPanel.add(dataStart);
		btnPanel.add(dataStop);
		btnPanel.add(gameStart);
		btnPanel.add(gameStop);
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
		else if(obj == gameStart) {
			StartServer("Game");
			gameStart.setEnabled(false);
			gameStop.setEnabled(true);
		}
		
		else if(obj == gameStop) {
			StopServer("Game");
			gameStop.setEnabled(false);
			gameStart.setEnabled(true);
		}
	}
	
	public void StartServer(String type) {
		if (type.equals("Data")) {
			dataThreadPool = Executors.newFixedThreadPool(THREAD_CNT);
			dataServer = new DataServer();
			data = new Thread(dataServer);
			data.start();
		} else if (type.equals("Game")) {
			gameThreadPool = Executors.newFixedThreadPool(THREAD_CNT);
			gameServer = new GameServer();
			game = new Thread(gameServer);
			game.start();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void StopServer(String type) {
		if (type.equals("Data")) {
			data.stop();
			dataThreadPool.shutdown();
			while(!dataThreadPool.isTerminated()) {}
			try {
				dataSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("�����ͼ��� ����\n");
		} else if (type.equals("Game")) {
			game.stop();
			gameThreadPool.shutdown();
			while(!gameThreadPool.isTerminated()) {}
			try {
				gameSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append("���Ӽ��� ����\n");
		}
	}
	
	class DataServer implements Runnable {
		@Override
		public void run() {
			try {
				dataSocket = new ServerSocket(DATAPORT);
				textArea.append("�����ͺ��̽� ���� ���� ��\n");
				while(true) {
					Socket socket = dataSocket.accept();
					dataThreadPool.execute(new DataThread(socket));
					textArea.append(socket.toString() + "�� �����ͼ����� ����Ǿ����ϴ�.\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class GameServer implements Runnable {
		@Override
		public void run() {
			try {
				gameSocket = new ServerSocket(GAMEPORT);
				textArea.append("���Ӽ��� ���� ��\n");
				
				while (true) {
					Socket socket = gameSocket.accept();
					gameThreadPool.execute(new GameThread(socket));
					textArea.append(socket.toString() + "�� ���Ӽ����� ����Ǿ����ϴ�.\n");
				}
			} catch (IOException e) {
					e.printStackTrace();
			}
		}
	}
}


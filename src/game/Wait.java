package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ChatInterface;
import client.Client;
import client.DataInterface;
import client.Exit;
import client.InfoPanel;
import client.Interface;
import client.RoomInterface;
import client.UserStatus;
import server.Data;
import server.GameData;
import server.Room;

@SuppressWarnings("serial")
public class Wait extends JFrame {
	private Hashtable<Integer, Room> roomList = new Hashtable<Integer, Room>();
	private ChatInterface chatI = new Interface();
	private JButton[] room = new JButton[4];
	private Button button;
	private JTextArea messageArea;
	private JScrollPane scrollPane;
	private Client client;
	
	public Wait(Client client) {
		this.client = client;
		setTitle("E.Play");
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("WAIT", client));
		
		button = new Button(client);
		JPanel chatPanel = new JPanel();
		InfoPanel info = new InfoPanel(client, "WAIT");
		
		setChat(chatPanel);
				
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		northPanel.add(button, BorderLayout.CENTER);
		northPanel.setSize(1100, 350);
		southPanel.setSize(1100, 350);
		southPanel.add(chatPanel, BorderLayout.CENTER);

		ImageIcon icon = new ImageIcon("img/background3.png");
		Image originImg = icon.getImage();
		Image changedImg = originImg.getScaledInstance(1100, 700, Image.SCALE_SMOOTH);
		ImageIcon Icon = new ImageIcon(changedImg);
		
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(Icon.getImage(), 0, 0, null);
				setOpaque(false);// 배경 띄워주기
				super.paintComponent(g);
			}
		};
		
		panel.add(info, BorderLayout.NORTH);
		panel.add(button, BorderLayout.CENTER);
		panel.add(chatPanel, BorderLayout.SOUTH);
		add(panel);
		
		setVisible(true);
	}
	
	private void setChat(JPanel chatPanel) {
		JTextField userText;
		JButton send = new JButton("전송");
		JPanel south = new JPanel();
		chatPanel.setPreferredSize(new Dimension(900, 270));
		chatPanel.setOpaque(false);
		scrollPane = new JScrollPane();
		Dimension messageSize = new Dimension(890, 200);
		Dimension textSize = new Dimension(830, 20);
		scrollPane.setPreferredSize(messageSize);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatPanel.add(scrollPane, BorderLayout.CENTER);
		messageArea = new JTextArea();
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);
		userText = new JTextField();
		userText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					chatI.toMessage(client.getInfo(), client.getDTO(), userText.getText());
					userText.setText("");
				}
			}
		});
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((JButton)e.getSource() == send) {
					chatI.toMessage(client.getInfo(), client.getDTO(), userText.getText());
					userText.setText("");
				}
			}
		});
		userText.setPreferredSize(textSize);
		userText.setEditable(true);
		south.add(userText, BorderLayout.CENTER);
		south.add(send, BorderLayout.EAST);
		south.setOpaque(false);
		chatPanel.add(south, BorderLayout.SOUTH);
	}
	
	class Button extends JPanel {
		public Button(Client client) {
			setLayout(new GridLayout(2, 2));
			setOpaque(false);
			setRoom();			
			add(room[0]);
			add(room[1]);
			add(room[2]);
			add(room[3]);
			
			setPreferredSize(new Dimension(900, 350));
		}
		
		public void updateButton() {
			this.repaint();
		}
	}
		
	public void setMessage(Data data) {
		String seg = data.getSegment();
		if(seg.equals("MESSAGE")) {
			messageArea.append(data.getPacket() + " : " + data.getMessage() + "\n");
		} else if(seg.equals("WHISPHER")) {
			messageArea.append(data.getPacket() + " -> " + client.getDTO().getName() + data.getMessage() + "\n");
		} else if(seg.equals("ENTER")) {
			messageArea.append(data.getPacket() + "님이 입장하셨습니다.\n");
		} else if(seg.equals("EXIT")) {
			messageArea.append(data.getPacket() + "님이 퇴장하셨습니다.\n");
		}
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	public void setRoomList(GameData data) {
		if(data.getSegment().equals("LIST")) {
			this.roomList = data.getRoomList();
			setRoom();
			button.updateButton();
			button.updateUI();
		}
	}
	
	private void setRoom() {
		for(int i=0;i<4;i++) {
			if(roomList.containsKey(i+1))
				room[i] = new RoomButton(roomList.get(i+1).getRoomName());
			else
				room[i] = new RoomButton();
			room[i].addActionListener(new WaitButton(i+1, roomList.containsKey(i+1), client));
			room[i].setSize(400, 130);	
		}
	}
	
	class RoomButton extends JButton {
		public RoomButton(String roomName) {
			setIcon(new ImageIcon("img/user.png"));
			setText(roomName);
			setBorderPainted(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
		}
		
		public RoomButton() {
			setIcon(new ImageIcon("img/userEmpty.png"));
			setBorderPainted(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
		}
	}
}

class WaitButton implements ActionListener {
	RoomInterface roomI = new Interface();
	DataInterface dataI = new Interface();
	int roomNum;
	boolean state;
	Client client;
	
	WaitButton(int roomNum, boolean state, Client client) {
		this.state = state;
		this.roomNum = roomNum;
		this.client = client;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UserStatus.Location location = null;
		String roomName = null;
		boolean valid = false;
		switch(roomNum) {
		case 1:
			location = UserStatus.Location.ROOM1;
			break;
		case 2:
			location = UserStatus.Location.ROOM2;
			break;
		case 3:
			location = UserStatus.Location.ROOM3;
			break;
		case 4:
			location = UserStatus.Location.ROOM4;
			break;
		}
		if(!state) {
			roomName = JOptionPane.showInputDialog("방 이름을 입력해주세요.");
			valid = true;
		}
		client.termWait(false);
		client.exeRoomUI(valid, roomName, location, roomNum);
	}
}
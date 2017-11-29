package game;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import client.DTO;
import login.Find;
import main.Chat;
import server.RoomData;

public class Game extends JFrame {
	List<Room> roomList = new ArrayList<Room>();
	JButton[] room = new JButton[4];
	
	public static DTO dto = new DTO();
	
	public static void main(String[] args) throws IOException, AWTException {
		dto.setAvatar(1);
		dto.setName("예찬호");
		dto.setAge(24);
		dto.setId("koreannet");
		dto.setPassword("asdf10");
		new Game(dto);
	}
	
	public Game (DTO dto) throws IOException, AWTException {
		this.dto = dto;
		setTitle("E.Play");
		setSize(1100, 700);
		setResizable(false);
		setLocation(0, 0);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new gameExit(this));
		
		JPanel btnPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		JPanel chatPanel = new Chat(dto.getId(), dto.getName(), 0);
		JPanel listPanel = new JPanel();
		
		Dimension leftSize = new Dimension(340,345);
		Dimension rightSize = new Dimension(740,345);
		
		btnPanel.setLayout(new GridLayout(2,2));
		room[0] = new JButton("Empty");
		room[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == room[0]){
					if(roomList.size() > 1) {
						try {
							Client.enterRoom(dto, roomList.get(0));
							new RoomUI(dto, roomList.get(0));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else {
						try {
							String roomName = JOptionPane.showInputDialog("방 이름을 입력해주세요.");
							RoomData room = Client.createRoom(dto, roomName);
							new RoomUI(dto, room.getRoom());
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		room[1] = new JButton("Empty");
		room[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == room[1] && roomList.size() > 2){
					try {
						Client.enterRoom(dto, roomList.get(1));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		room[2] = new JButton("Empty");
		room[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == room[2] && roomList.size() > 3){
					try {
						Client.enterRoom(dto, roomList.get(2));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		room[3] = new JButton("Empty");
		room[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == room[3] && roomList.size() > 4){
					try {
						Client.enterRoom(dto, roomList.get(3));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		room[0].setSize(400, 175);
		room[1].setSize(400, 175);
		room[2].setSize(400, 175);
		room[3].setSize(400, 175);
		btnPanel.add(room[0]);
		btnPanel.add(room[1]);
		btnPanel.add(room[2]);
		btnPanel.add(room[3]);
		btnPanel.setPreferredSize(rightSize);
		
		String avaSrc = null;
		switch(dto.getAvatar()) {
		case 1:	
			avaSrc = "img/ava/muzi.png";
			break;
		case 2:
			avaSrc = "img/ava/apeach.png";
			break;
		case 3:
			avaSrc = "img/ava/frodo.png";
			break;
		case 4:
			avaSrc = "img/ava/neo.png";
			break;
		case 5:
			avaSrc = "img/ava/tube.png";
			break;
		case 6:
			avaSrc = "img/ava/jayg.png";
			break;
		}
		
		JButton avatar = new JButton(new ImageIcon(avaSrc));
		JLabel nameLabel = new JLabel(dto.getName());
		JLabel pointLabel = new JLabel();
		avatar.setSize(75, 100);
		infoPanel.add(avatar);
		infoPanel.add(nameLabel);
		infoPanel.add(pointLabel);
		infoPanel.setPreferredSize(leftSize);
		infoPanel.setBackground(new Color(0,255,0));
		
		JTextArea messageArea = new JTextArea(12, 60);
		JTextField chat = new JTextField(53);
		JButton send = new JButton("전송");
		JPanel txt = new JPanel();
		messageArea.setEditable(false);
		txt.add(chat,BorderLayout.CENTER);
		txt.add(send,BorderLayout.EAST);
		
		JLabel connect = new JLabel("접속자 목록");
		JList curList = new JList();
		connect.setPreferredSize(new Dimension(330, 35));
		curList.setPreferredSize(new Dimension(330, 300));
		
		listPanel.add(connect,BorderLayout.NORTH);
		listPanel.add(curList,BorderLayout.CENTER);
		listPanel.setPreferredSize(leftSize);
		listPanel.setBackground(new Color(0,0,255));
		
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		northPanel.add(infoPanel,BorderLayout.WEST);
		northPanel.add(btnPanel,BorderLayout.CENTER);
		northPanel.setSize(1100, 350);
		southPanel.setSize(1100, 350);
		southPanel.add(chatPanel,BorderLayout.WEST);
		southPanel.add(listPanel, BorderLayout.CENTER);
		
		getContentPane().add(northPanel, BorderLayout.NORTH);
		getContentPane().add(southPanel, BorderLayout.CENTER);

		setVisible(true);
		
		updateInfo();
	}
	
	private void updateInfo() {
		RoomData roomData;
		try {
			roomData = Client.getRoomList();
			roomList = roomData.getRoomList();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<roomList.size();i++) {
			room[i].setText(roomList.get(i).getRoomName());	
		}		
	}
}

class gameExit implements WindowListener {
	private Game game;
	private DTO dto;
	
	gameExit(Game game) {
		this.game = game;
		this.dto = game.dto;
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(game, "종료하시겠습니까?", "Log Out", JOptionPane.YES_NO_OPTION);
		if (exit == 0) {
			/*try {
				if(Client.logOut(dto).equals("FIN")) {
					Client.Socket.close();
					Client.ChatSocket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}*/
			System.exit(0);
		}
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}
}

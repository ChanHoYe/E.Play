package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ChatInterface;
import client.Client;
import client.DTO;
import client.Exit;
import client.GameInterface;
import client.InfoPanel;
import client.Interface;
import client.RoomInterface;
import client.UserStatus;
import server.Data;
import server.GameData;

public class RoomUI extends JFrame {
	ChatInterface chatI = new Interface();
	RoomInterface roomI = new Interface();
	GameInterface gameI = new Interface();
	private JScrollPane scrollPane;
	private JTextArea messageArea;
	private Client client;
	private Hashtable<Integer, DTO> userList = new Hashtable<Integer, DTO>();
	private ArrayList<String> usedList = new ArrayList<String>();
	private int roomNum;
	User user;
	GamePanel game;
	
	public RoomUI(Client client) {
		this.client=client;
		this.roomNum = client.getDTO().getLocationValue();
		
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
		
		
		setTitle("E.Play");
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("ROOM", client));
		
		JPanel info = new InfoPanel(client, "ROOM");
		JPanel chatPanel = new JPanel();
		game = new GamePanel(roomNum);
		user = new User(game);
		initChatPanel(chatPanel);
		
		chatPanel.setOpaque(false);
		
		panel.add(info);
		panel.add(user);
		panel.add(chatPanel);
		add(panel);
		
		setVisible(true);
	}
	
	private void initChatPanel(JPanel chatPanel) {
		JTextField userText;
		JButton send = new JButton("전송");
		JPanel south = new JPanel();
		chatPanel.setPreferredSize(new Dimension(900, 270));
		scrollPane = new JScrollPane();
		Dimension messageSize = new Dimension(890, 200);
		scrollPane.setPreferredSize(messageSize);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatPanel.add(scrollPane, BorderLayout.CENTER);
		messageArea = new JTextArea();
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);
		userText = new JTextField();
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == send) {
					if(userText.getText().length() != 0) {
						chatI.toMessage(client.getInfo(), client.getDTO(), userText.getText());
						userText.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "메시지를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		userText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send.doClick();
				}
			}
		});
		userText.setPreferredSize(new Dimension(830, 20));
		userText.setEditable(true);
		south.add(userText, BorderLayout.CENTER);
		south.add(send, BorderLayout.EAST);
		south.setOpaque(false);
		chatPanel.add(south, BorderLayout.SOUTH);
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
	
	public void setData(GameData data) {
		if(data.getSegment().equals("USER")) {
			userList = data.getUserList();
			
			user.update();
			//updatePanel();
			//placeRoomPanel();
		} else if(data.getSegment().equals("CHECK")) {
			usedList = data.getUsedList();
			game.updateList();
		}
	}
	
	class GamePanel extends JPanel {
		int locationValue;
		JButton btnStart = new JButton("게임 시작");
		JTextField answerField = new JTextField();
		JTextArea wordArea = new JTextArea();
		public GamePanel(int locationValue) {
			this.locationValue = locationValue;
			
			setOpaque(false);
			setPreferredSize(new Dimension(470,320));
			
			wordArea.setEditable(false);
			wordArea.setText("");
			wordArea.setPreferredSize(new Dimension(460,250));
			btnStart.setEnabled(false);
			btnStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if((JButton)e.getSource() == btnStart) {
						gameI.startGame(client.getInfo());
						btnStart.setEnabled(false);
					}
				}
			});
			
			answerField.setPreferredSize(new Dimension(430, 20));
			answerField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(answerField.getText() != null) {
							gameI.checkWord(client.getInfo(), answerField.getText(), roomNum, client.getDTO());
						}
					}
				}
			});
			
			add(btnStart, BorderLayout.NORTH);
			add(wordArea, BorderLayout.CENTER);
			add(answerField, BorderLayout.SOUTH);
		}
		
		public void updateList() {
			wordArea.setText("");
			
			Iterator<String> iter = usedList.iterator();
			
			while(iter.hasNext()) {
				wordArea.append(iter.next());
			}
		}
		
		public void setStart() {
			this.btnStart.setEnabled(true);
		}
	}
	
	class User extends JPanel {
		JPanel user[] = new JPanel[4];
		GamePanel game;
		public User(GamePanel game) {
			JPanel leftUser = new JPanel();
			JPanel rightUser = new JPanel();
			this.game = game;
			
			setOpaque(false);
			setPreferredSize(new Dimension(1080, 350));
			//setLayout(new GridLayout(1, 3));
						
			leftUser = new JPanel(new GridLayout(2, 1));
			rightUser = new JPanel(new GridLayout(2, 1));
			
			leftUser.setOpaque(false);
			rightUser.setOpaque(false);
			
			leftUser.setPreferredSize(new Dimension(272, 350));
			rightUser.setPreferredSize(new Dimension(272, 350));
			leftUser.setBounds(22, 82, 272, 350);
			game.setBounds(310, 92, 470, 320);
			rightUser.setBounds(808, 82, 272, 350);
			
			update();
			
			leftUser.add(user[0]);
			leftUser.add(user[2]);
			rightUser.add(user[1]);
			rightUser.add(user[3]);
			add(leftUser);
			add(game, BorderLayout.CENTER);
			add(rightUser);
		}
		
		private void update() {
			int idx=0;
			
			ImageIcon icon = new ImageIcon("img/user.png");
			Image originImg = icon.getImage();
			Image changedImg = originImg.getScaledInstance(271, 164, Image.SCALE_SMOOTH);
			ImageIcon btnIcon = new ImageIcon(changedImg);
			for(DTO dto : userList.values()) {
				user[idx] = new userPanel(dto.getName(), dto.getPoint(), dto.getAvatar(), btnIcon);
				idx++;
			}
			
			icon = new ImageIcon("img/userEmpty.png");
			originImg = icon.getImage();
			changedImg = originImg.getScaledInstance(271, 164, Image.SCALE_SMOOTH);
			btnIcon = new ImageIcon(changedImg);
			for(int i=idx; i<4; i++) {
				user[i] = new userPanel(btnIcon);
			}
			
			if (!userList.isEmpty()) {
				if (userList.get(1).getId().equalsIgnoreCase(client.getDTO().getId())) {
					game.btnStart.setEnabled(true);
				}
			}
			
			revalidate();
			repaint();
		}
	}

	class userPanel extends JPanel {
		ImageIcon btnIcon;
		
		userPanel(String name, int point, int ava, ImageIcon btnIcon) {
			this.btnIcon = btnIcon;
			setLayout(new GridLayout(1,2));
			setOpaque(false);			
			
			JLabel name_Lb = new JLabel(name);
			JLabel point_Lb = new JLabel(String.valueOf(point));
			JButton avatar;
			JPanel lb_panel = new JPanel(new GridLayout(2,1));
			
			String avaSrc = null;
			switch(ava) {
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
			
			avatar = new JButton(new ImageIcon(avaSrc));
			
			lb_panel.add(name_Lb);
			lb_panel.add(point_Lb);
			lb_panel.setOpaque(false);
			
			add(avatar);
			add(lb_panel);
		}
		
		userPanel(ImageIcon btnIcon) {
			this.btnIcon = btnIcon;
			JLabel label = new JLabel("입장 대기중입니다.");
			add(label);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(btnIcon.getImage(), 0, 0, null);
			setOpaque(false);// 배경 띄워주기
			super.paintComponent(g);
		}
	}
}
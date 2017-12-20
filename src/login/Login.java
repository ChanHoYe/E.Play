package login;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import client.Client;
import client.DTO;
import client.Exit;
import client.Interface;
import client.LoginInterface;
import client.SocketInfo;
import login.Login;
import server.Data;

public class Login extends JFrame {
	private Client client;
	private SocketInfo info = null;
	
	private volatile boolean Status = true;
	private Data data;
	
	public Login(Client client) {
		this.info = client.getInfo();
		this.client = client;
		
		setTitle("Welcome to E.Play!!!");
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("LOGIN", client));
		ImageIcon icon = new ImageIcon("img/background.png");
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
		
		placeLoginPanel(panel);
		add(panel);
		setVisible(true);
	}
	
	private void placeLoginPanel(JPanel panel) {
		LoginInterface loginI = new Interface();
		panel.setLayout(null);	
		JPanel total = new JPanel();
		JPanel text = new JPanel();
		JPanel label = new JPanel();
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);
		total.setBounds(370, 500, 500, 500);
		total.setOpaque(false);
		text.setOpaque(false);
		total.setLayout(null);
		text.setLayout(null);
		label.setLayout(new GridLayout(2,1,100,110));
		panel.add(total);
		
		JLabel idLabel = new JLabel(new ImageIcon("img/log/id.png"),JLabel.CENTER);
		idLabel.setBounds(0, 0, 100,50);
		total.add(idLabel);

		JLabel passLabel = new JLabel(new ImageIcon("img/log/pw.png"),JLabel.CENTER);
		passLabel.setBounds(0,60, 100, 50);
		total.add(passLabel);

		JTextField idText = new JTextField(20);
		idText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idText.setBounds(0, 0, 160, 25);
		idText.setFocusable(true);
		text.add(idText);
		
		JPasswordField passText = new JPasswordField(20);
		passText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		passText.setBounds(0, 60, 160, 25);
		text.add(passText);
		text.setBounds(110,13,160,85);
		total.add(text);
		
		JButton btnFind = new JButton(new ImageIcon("img/log/find.png"));
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnFind){
					client.exeFind();
				}
			}
		});
		
		btnFind.setBounds(0, 120, 200, 40);
		btnFind.setBorderPainted(false);
		btnFind.setContentAreaFilled(false);
		btnFind.setFocusPainted(false);
		total.add(btnFind);
		
		JButton btnJoin = new JButton(new ImageIcon("img/log/join.png"));
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnJoin) {
					client.exeJoin();
				}
			}
		});
		
		btnJoin.setBounds(200, 120, 120, 40);
		btnJoin.setBorderPainted(false);
		btnJoin.setContentAreaFilled(false);
		btnJoin.setFocusPainted(false);
		total.add(btnJoin);	
		
		JButton btnLogin = new JButton(new ImageIcon("img/log/login.png"));
		btnLogin.setActionCommand("login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnLogin || e.getActionCommand() == "login") {
					DTO dto = new DTO();
					dto.setId(idText.getText());
					dto.setPassword(passText.getText());
					idText.setText("");
					passText.setText("");
					if(dto.getId().length()==0)
						JOptionPane.showMessageDialog(null, "ID를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else {
						try {
							loginI.login(client.getInfo(), dto);
							while (Status) {
							}
							Status = true;
							String seg = data.getSegment();
							if(seg.equals("NOID"))
								JOptionPane.showMessageDialog(null, "ID가 존재하지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else if(seg.equals("NOPASS"))
								JOptionPane.showMessageDialog(null, "Password가 틀립니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else if(seg.equals("DUPLOGIN"))
								JOptionPane.showMessageDialog(null, "이미 로그인 되어 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else {
								DTO tmp = data.getDTO();
								dto.setName(tmp.getName());
								dto.setAge(tmp.getAge());
								dto.setAvatar(tmp.getAvatar());
								dto.setPoint(tmp.getPoint());
								dto.hidePassword();
								client.setDTO(dto);
								if(dto.getAvatar()==0){
									client.exeAvatar();
									client.termLogin();
								}
								else{
									client.exeMain();
									client.termLogin();
								}
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}});
		btnLogin.setBounds(290, 30, 150, 40);
		btnLogin.setBorderPainted(false);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setFocusPainted(false);
		idText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
					idText.requestFocus();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		passText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
					idText.requestFocus();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		total.add(btnLogin);
	}
	
	public void setData(Data data) {
		this.data = data;
		this.Status = false;
	}
}
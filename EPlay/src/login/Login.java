package login;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

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
import login.Avatar;
import login.Find;
import login.Join;
import login.Login;
import login.MyListener;
import main.Main;
import server.Data;

public class Login extends JFrame {
	DTO dto = new DTO();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Login() {
		setTitle("Welcome to E.Play!!!");
		setSize(1100, 700);
		setResizable(false);
		setLocation(0, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new MyListener(this));
		ImageIcon icon = new ImageIcon("img/background.png");
		Image originImg = icon.getImage();
		Image changedImg = originImg.getScaledInstance(1100, 700, Image.SCALE_SMOOTH);
		ImageIcon Icon = new ImageIcon(changedImg);
		// panel
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(Icon.getImage(), 0, 0, null);
				setOpaque(false);// 배경 띄워주기
				super.paintComponent(g);
			}
		};
		
		placeLoginPanel(panel);
		getContentPane().add(panel);
		setVisible(true);
	}
	
	public void placeLoginPanel(JPanel panel){
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
					new Find("ID/PW 찾기");
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
					Join join = new Join();
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
				if((JButton)obj == btnLogin || e.getActionCommand() == "login"){
					dto.setId(idText.getText());
					dto.setPassword(passText.getText());
					idText.setText("");
					passText.setText("");
					if(dto.getId().length()==0)
						JOptionPane.showMessageDialog(null, "ID를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else {
						try {
							Data data = Client.checkID(dto);
							String result = data.getSegment();
							if(result.equals("NOID"))
								JOptionPane.showMessageDialog(null, "ID가 존재하지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else if(result.equals("NOPASS"))
								JOptionPane.showMessageDialog(null, "Password가 틀립니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else if(result.equals("DUPLOGIN"))
								JOptionPane.showMessageDialog(null, "이미 로그인 되어 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
							else{
								String[] arr = data.getData().split(" ");
								dto.setName(arr[0]);
								dto.setAge(Integer.parseInt(arr[1]));
								dto.setAvatar(Integer.parseInt(arr[2]));
								if(dto.getAvatar()==0){
									new Avatar(dto);
									dispose();
								}
								else{
									new Main(dto);
									dispose();
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
}

class MyListener implements WindowListener {
	private Login login;
	
	MyListener(Login login) {
		this.login = login;
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(login, "종료하시겠습니까?", "Log Out", JOptionPane.YES_NO_OPTION);
		if (exit == 0) {
			try {
				Client.Socket.close();
				//Client.ChatSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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

package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;
import client.Exit;
import client.Interface;
import client.LoginInterface;
import server.Data;

public class Avatar extends JFrame {
	public Avatar(Client client) {		
		setTitle("아바타 설정");
		setSize(600, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("AVATAR", client));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new GridLayout(2, 3));
		
		JButton btnMuzi = new JButton(new ImageIcon("img/ava/muzi.png"));
		btnMuzi.setBorderPainted(false);
		btnMuzi.setContentAreaFilled(false);
		btnMuzi.setFocusPainted(false);
		btnMuzi.addActionListener(new AvaButton(1, client));
		panel.add(btnMuzi);
		
		JButton btnApeach = new JButton(new ImageIcon("img/ava/apeach.png"));
		btnApeach.setBorderPainted(false);
		btnApeach.setContentAreaFilled(false);
		btnApeach.setFocusPainted(false);
		btnApeach.addActionListener(new AvaButton(2, client));
		panel.add(btnApeach);
		
		JButton btnFrodo = new JButton(new ImageIcon("img/ava/frodo.png"));
		btnFrodo.setBorderPainted(false);
		btnFrodo.setContentAreaFilled(false);
		btnFrodo.setFocusPainted(false);
		btnFrodo.addActionListener(new AvaButton(3, client));
		panel.add(btnFrodo);
		
		JButton btnNeo = new JButton(new ImageIcon("img/ava/neo.png"));
		btnNeo.setBorderPainted(false);
		btnNeo.setContentAreaFilled(false);
		btnNeo.setFocusPainted(false);
		btnNeo.addActionListener(new AvaButton(4, client));
		panel.add(btnNeo);
		
		JButton btnTube = new JButton(new ImageIcon("img/ava/tube.png"));
		btnTube.setBorderPainted(false);
		btnTube.setContentAreaFilled(false);
		btnTube.setFocusPainted(false);
		btnTube.addActionListener(new AvaButton(5, client));
		panel.add(btnTube);
		
		JButton btnJayG = new JButton(new ImageIcon("img/ava/jayg.png"));
		btnJayG.setBorderPainted(false);
		btnJayG.setContentAreaFilled(false);
		btnJayG.setFocusPainted(false);
		btnJayG.addActionListener(new AvaButton(6, client));
		panel.add(btnJayG);
		
		setVisible(true);
	}
}

class AvaButton implements ActionListener {
	LoginInterface loginI = new Interface();
	int btn;
	Client client;
	
	public AvaButton(int btn, Client client) {
		this.btn = btn;
		this.client = client;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		client.getDTO().setAvatar(btn);
		loginI.setAvatar(client.getInfo(), client.getDTO());
		client.exeMain();
		client.termAvatar();
	}
}

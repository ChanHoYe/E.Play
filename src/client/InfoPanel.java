package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class InfoPanel extends JPanel {
	public InfoPanel(Client client, String seg) {
		JPanel infoPanel = new JPanel();
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(1080,50));
		JButton btnBack = new JButton(new ImageIcon("img/btnBack.png"));
		btnBack.addActionListener(new Back(seg, client, btnBack));
		btnBack.setPreferredSize(new Dimension(130, 44));
		btnBack.setOpaque(false);
		setOpaque(false);
		
		JPanel trans = new JPanel();
		trans.setPreferredSize(new Dimension(600,30));
		trans.setOpaque(false);
		
		String avaSrc = null;
		switch (client.getDTO().getAvatar()) {
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
		
		ImageIcon icon = new ImageIcon(new ImageIcon(avaSrc).getImage().getScaledInstance(35, 50, Image.SCALE_SMOOTH));
		
		JLabel avatar = new JLabel(icon);
		avatar.setOpaque(false);
		JLabel nameLabel = new JLabel("이름 : " + client.getDTO().getName());
		//JLabel name = new JLabel(client.getDTO().getName());
		JLabel pointLabel = new JLabel("포인트 : " + String.valueOf(client.getDTO().getPoint()));
		//JLabel point = new JLabel(String.valueOf(client.getDTO().getPoint()));
		//infoPanel.setOpaque(false);
		infoPanel.setLayout(new GridLayout(1,3));
		infoPanel.setPreferredSize(new Dimension(300, 45));
		infoPanel.add(avatar);
		infoPanel.add(nameLabel);
		//add(name);
		infoPanel.add(pointLabel);
		infoPanel.setOpaque(false);
		//add(point);
		
		//infoPanel.setBounds(10, 5, 300, 45);
		
		add(infoPanel);
		add(trans);
		add(btnBack);
		
		setVisible(true);
	}
}

class Back implements ActionListener {
	String seg;
	Client client;
	JButton btnBack;
	public Back(String seg, Client client, JButton btnBack) {
		this.seg = seg;
		this.client = client;
		this.btnBack = btnBack;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if((JButton)e.getSource() == btnBack) {
			if(seg.equals("WAIT")) {
				client.termWait(true);
				client.exeMain();
			} else if(seg.equals("ROOM")) {
				client.termRoomUI();
				client.exeWait();
			} else if(seg.equals("WORD")) {
				client.termWordbook();
				client.exeMain();
			}
		}
	}
	
}

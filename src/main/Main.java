package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;
import client.DataInterface;
import client.Exit;
import client.Interface;
import client.UserStatus;

@SuppressWarnings("serial")
public class Main extends JFrame {
	public Main(Client client) {
		setTitle("E.Play!!! - " + client.getDTO().getName());
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("MAIN", client));
		ImageIcon icon = new ImageIcon("img/background1.png");
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
		add(panel);
		panel.setLayout(null);

		JButton btnGame = new JButton(new ImageIcon("img/game.png"));
		btnGame.addActionListener(new MainButton("GAME", client));
		btnGame.setBounds(50, 180, 300, 430);
		btnGame.setOpaque(false);
		btnGame.setBorderPainted(false);
		btnGame.setContentAreaFilled(false);
		btnGame.setFocusPainted(false);
		panel.add(btnGame);

		JButton btnWord = new JButton(new ImageIcon("img/word.png"));
		btnWord.addActionListener(new MainButton("WORD", client));
		btnWord.setBounds(400, 180, 300, 430);
		btnWord.setOpaque(false);
		btnWord.setBorderPainted(false);
		btnWord.setContentAreaFilled(false);
		btnWord.setFocusPainted(false);
		panel.add(btnWord);

		JButton btnRank = new JButton(new ImageIcon("img/rank.png"));
		btnRank.addActionListener(new MainButton("RANK", client));
		btnRank.setBounds(750, 180, 300, 430);
		btnRank.setOpaque(false);
		btnRank.setBorderPainted(false);
		btnRank.setContentAreaFilled(false);
		btnRank.setFocusPainted(false);
		panel.add(btnRank);
		setVisible(true);
	}
}

class MainButton implements ActionListener {
	DataInterface dataI = new Interface();
	String seg;
	Client client;

	MainButton(String seg, Client client) {
		this.seg = seg;
		this.client = client;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (seg.equals("GAME")) {
			client.exeWait();
			client.termMain();
		} else if (seg.equals("WORD")) {
			client.exeWordbook();
			client.termMain();
		} else if (seg.equals("RANK")) {
			client.exeRank();
		}
	}
}

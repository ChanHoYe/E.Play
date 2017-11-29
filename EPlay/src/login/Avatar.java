package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;
import client.DTO;
import main.Main;

public class Avatar extends JFrame {
	DTO dto = new DTO();
	static String name;
	static int age;
	static String id;
	static String password;
	static int ava;
	public Avatar(DTO dto) {
		name = dto.getName();
		age = dto.getAge();
		id = dto.getId();
		password = dto.getPassword();
		ava = dto.getAvatar();
		setTitle("아바타 설정");
		setSize(600, 500);
		setResizable(false);
		setLocation(800, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(2,3));	
		
		JButton btnMuzi = new JButton(new ImageIcon("img/ava/muzi.png"));
		btnMuzi.setBorderPainted(false);
		btnMuzi.setContentAreaFilled(false);
		btnMuzi.setFocusPainted(false);
		btnMuzi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnMuzi){
					dto.setAvatar(1);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}		
				}
			}
		});
		panel.add(btnMuzi);

		JButton btnApeach = new JButton(new ImageIcon("img/ava/apeach.png"));
		btnApeach.setBorderPainted(false);
		btnApeach.setContentAreaFilled(false);
		btnApeach.setFocusPainted(false);
		btnApeach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnApeach){
					dto.setAvatar(2);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}		
				}
			}
		});
		panel.add(btnApeach);

		JButton btnFrodo = new JButton(new ImageIcon("img/ava/frodo.png"));
		btnFrodo.setBorderPainted(false);
		btnFrodo.setContentAreaFilled(false);
		btnFrodo.setFocusPainted(false);
		btnFrodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnFrodo){
					dto.setAvatar(3);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}		
				}
			}
		});
		panel.add(btnFrodo);

		JButton btnNeo = new JButton(new ImageIcon("img/ava/neo.png"));
		btnNeo.setBorderPainted(false);
		btnNeo.setContentAreaFilled(false);
		btnNeo.setFocusPainted(false);
		btnNeo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnNeo){
					dto.setAvatar(4);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(btnNeo);	

		JButton btnTube = new JButton(new ImageIcon("img/ava/tube.png"));
		btnTube.setBorderPainted(false);
		btnTube.setContentAreaFilled(false);
		btnTube.setFocusPainted(false);
		btnTube.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnTube){
					dto.setAvatar(5);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}			
				}
			}
		});
		panel.add(btnTube);
		
		JButton btnJayG = new JButton(new ImageIcon("img/ava/jayg.png"));
		btnJayG.setBorderPainted(false);
		btnJayG.setContentAreaFilled(false);
		btnJayG.setFocusPainted(false);
		btnJayG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnJayG){
					dto.setAvatar(6);
					try {
						Client.insertAvatar(dto);
						new Main(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}			
				}
			}
		});
		panel.add(btnJayG);
		setVisible(true);
	}
}

package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.Client;
import client.DTO;

public class Main extends JFrame {
	public DTO dto;
	public Main (DTO dto) throws IOException {
		this.dto = dto;
		setTitle("E.Play");
		setSize(1100, 700);
		setResizable(false);
		setLocation(0, 0);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new MyListener(this));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnGame = new JButton("Game");
		btnGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnGame) {
					/*try {
						new Game(dto);
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}*/		
				}
			}
		});
		
		btnGame.setBounds(50, 100, 300, 500);
		panel.add(btnGame);
		
		JButton btnWord = new JButton("Word");
		btnWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnWord) {
					new WordBook(dto);
					dispose();
				}
			}
		});
		btnWord.setBounds(400, 100, 300, 500);
		panel.add(btnWord);
		
		JButton btnRank = new JButton("Rank");
		btnRank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnRank) {
					new Rank();
				}
			}
		});
		btnRank.setBounds(750, 100, 300, 500);
		panel.add(btnRank);
		setVisible(true);
	}
}

class MyListener implements WindowListener {
	private Main main;
	private DTO dto;
	
	MyListener(Main main) {
		this.main = main;
		this.dto = main.dto;
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(main, "종료하시겠습니까?", "Log Out", JOptionPane.YES_NO_OPTION);
		if (exit == 0) {
			try {
				if(Client.logOut(dto).getSegment().equals("FINISH")) {
					Client.Socket.close();
					Client.ChatSocket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
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

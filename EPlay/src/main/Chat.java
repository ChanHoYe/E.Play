package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;

public class Chat extends JPanel {
	String id;
	String name;
	int roomNum;
	JTextField userText;
	JTextArea messageArea;
	
	public Chat(String id, String name, int roomNum) {
		this.id = id;
		this.name = name;
		this.roomNum = roomNum;
		setBounds(270, 107, 553, 603);
		setLayout(null);
		setOpaque(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12,10,530,500);
		this.add(scrollPane);
		messageArea = new JTextArea();
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);

		
		Client.out.println(id + " " + name);
		userText = new JTextField();
		userText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Client.out.println(userText.getText());
					userText.setText("");
				}
			}

		});
		userText.setBounds(12, 532, 530, 50);
		this.add(userText); 
	}
	public void run() {
		while(true) {
			String line;
			try {
				line = Client.in.readLine();
				if (line.startsWith("SUBMITNAME")) 
					Client.out.println(roomNum + " " + id + " " + name);
				else if (line.startsWith("NAMEACCEPTED"))
					userText.setEditable(true);

				else if (line.startsWith("MESSAGE")) {
					int num = Integer.parseInt(line.substring(8, 10).trim());
					line = line.substring(10);
					if(roomNum == num) {
						messageArea.append(line + "\n");
					}
				}
				
				else if (line.startsWith("WHISPER")) {
					line = line.substring(8);
					String tok[] = line.split(" ");
					
					if(tok[0].equals(name)) {
						messageArea.append(tok[1] + " -> " + tok[0] + " : " + tok[2]);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

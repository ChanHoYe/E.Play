package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Client;
import client.DTO;
import server.WordData;

public class WordBook extends JFrame implements ListSelectionListener {
	DTO dto;
	private ArrayList<String> word = new ArrayList<String>();
	private ArrayList<String> mean = new ArrayList<String>();
	private JList wordList = new JList();
	private JTextArea meanField = new JTextArea(10,40);
	
	public WordBook(DTO dto) {
		this.dto = dto;
				
		setTitle("E.Play - " + dto.getName() + "'s WordBook");
		setSize(1100, 700);
		setResizable(false);
		setLocation(0, 0);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WordExit(this));
		JPanel northPanel = new JPanel();
		JButton btnBack = new JButton("뒤로 가기");
		JPanel panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(wordList);
		scrollPane.setPreferredSize(new Dimension(300,600));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnBack) {
					try {
						new Main(dto);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			}
		});
		northPanel.add(btnBack, BorderLayout.EAST);
		getListData();
		
		wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListModel model = new DefaultListModel();
		wordList.setModel(model);
		wordList.addListSelectionListener((ListSelectionListener) this);
		for(int i=0;i<word.size();i++)
			model.addElement(word.get(i));
		wordList.setFont(new Font("DX빨간우체통B", Font.PLAIN, 14));
		meanField.setEditable(false);
		meanField.setText("");
		meanField.setPreferredSize(new Dimension(600,600));
		meanField.setFont(new Font("DX빨간우체통B", Font.PLAIN, 20));
		panel.add(scrollPane, BorderLayout.WEST);
		panel.add(meanField, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == wordList) {
			int idx = word.indexOf(wordList.getSelectedValue());
			
			meanField.setText(mean.get(idx).toString());
		}
	}
	
	public void getListData() {
		try {
			WordData data = Client.getWord();
			this.word = data.getWordData();
			this.mean = data.getMeanData();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}

class WordExit implements WindowListener {
	private WordBook wordBook;
	private DTO dto;
	
	WordExit(WordBook wordBook) {
		this.wordBook = wordBook;
		this.dto = wordBook.dto;
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(wordBook, "종료하시겠습니까?", "Log Out", JOptionPane.YES_NO_OPTION);
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

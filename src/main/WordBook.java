package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Client;
import client.DataInterface;
import client.Exit;
import client.InfoPanel;
import client.Interface;
import client.LoginInterface;
import server.Data;

public class WordBook extends JFrame implements ListSelectionListener {
	private ArrayList<String> word = new ArrayList<String>();
	private ArrayList<String> mean = new ArrayList<String>();
	private JList wordList = new JList();
	private JTextArea meanField = new JTextArea(10,40);
	private DefaultListModel model;
	
	public WordBook(Client client) {
		DataInterface dataI = new Interface();
		
		setTitle("E.Play - " + client.getDTO().getName() + "'s WordBook");
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new Exit("WORD", client));
		
		InfoPanel info = new InfoPanel(client, "WORD");
		
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
		
		JPanel northPanel = new JPanel();
		JButton btnBack = new JButton("뒤로 가기");
		JPanel center = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(wordList);
		scrollPane.setPreferredSize(new Dimension(300,600));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnBack) {
					client.exeMain();
					client.termWordbook();
				}
			}
		});
		northPanel.add(btnBack, BorderLayout.EAST);
		
		wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultListModel();
		wordList.setModel(model);
		wordList.addListSelectionListener((ListSelectionListener) this);
		wordList.setFont(new Font("DX빨간우체통B", Font.PLAIN, 14));
		meanField.setEditable(false);
		meanField.setText("");
		meanField.setPreferredSize(new Dimension(600,600));
		meanField.setFont(new Font("DX빨간우체통B", Font.PLAIN, 20));
		center.add(scrollPane, BorderLayout.WEST);
		center.add(meanField, BorderLayout.CENTER);
		center.setOpaque(false);
		panel.add(info, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		add(panel);
		setVisible(true);
		
		dataI.getWord(client.getInfo());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == wordList) {
			int idx = word.indexOf(wordList.getSelectedValue());
			
			meanField.setText(mean.get(idx).toString());
		}
	}
	
	public void setListData(Data data) {
		this.word = data.getWordData();
		this.mean = data.getMeanData();
		for(int i=0;i<word.size();i++)
			model.addElement(word.get(i));
	}
}
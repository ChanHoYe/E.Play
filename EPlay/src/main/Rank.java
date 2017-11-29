package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import client.Client;
import login.Avatar;
import server.Data;

public class Rank extends JFrame {
	public Rank() {
		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		JLabel label = new JLabel("이름 : ");
		JTextField searchText = new JTextField(10);
		JButton btnSearch = new JButton("검색");
		JPanel panel = new JPanel();
		panel.add(label);
		panel.add(searchText);
		panel.add(btnSearch);
		Vector<String> userColumn = new Vector<String>();
		userColumn.addElement("순위");
		userColumn.addElement("이름");
		userColumn.addElement("점수");
		Vector<String> userRow;
		setTitle("랭킹");
		setResizable(false);
		setLocation(800, 350);
		setVisible(true);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultTableModel model = new DefaultTableModel(userColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> point = new ArrayList<String>();
		ArrayList<String> rank = new ArrayList<String>();
		Data data;
		
		try {
			data = Client.getRank();
			name = data.name;
			point = data.point;
			rank = data.rank;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		
		for(int i=0;i<name.size();++i) {
				userRow = new Vector<String>();
				userRow.addElement(rank.get(i));
				userRow.addElement(name.get(i));
				userRow.addElement(point.get(i));
				model.addRow(userRow);
		}
		table.setModel(model);
		add(panel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		pack();
		setSize(300, 200);
		
		searchText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnSearch.doClick();
					searchText.requestFocus();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		btnSearch.setActionCommand("search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnSearch || e.getActionCommand() == "search"){
					if(searchText.getText().trim().length()==0)
						JOptionPane.showMessageDialog(null, "이름을 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else {
						String str = searchText.getText().trim();
						int schValue = -1;
						for(int idx=0;idx<table.getRowCount();idx++) {
							if(table.getValueAt(idx, 1).toString().trim().equals(str)) {
								schValue = idx;
							}
						}
						
						if(schValue == -1)
							JOptionPane.showMessageDialog(null, "이름이 존재하지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
						
						else {
							table.changeSelection(schValue, 1, false, false);
						}
					}
				}
			}
		});
	}
}

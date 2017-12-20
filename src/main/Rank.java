package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
import client.DataInterface;
import client.Interface;
import client.LoginInterface;
import server.Data;

public class Rank extends JFrame {
	private JTable table;
	private DefaultTableModel model;
	
	public Rank(Client client) {
		DataInterface dataI = new Interface();
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		JLabel label = new JLabel("�̸� : ");
		JTextField searchText = new JTextField(10);
		JButton btnSearch = new JButton("�˻�");
		JPanel panel = new JPanel();
		panel.add(label);
		panel.add(searchText);
		panel.add(btnSearch);
		Vector<String> userColumn = new Vector<String>();
		userColumn.addElement("����");
		userColumn.addElement("�̸�");
		userColumn.addElement("����");
		Vector<String> userRow;
		setTitle("��ŷ");
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultTableModel(userColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
				
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
						JOptionPane.showMessageDialog(null, "�̸��� �Է��ϼ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					else {
						String str = searchText.getText().trim();
						int schValue = -1;
						for(int idx=0;idx<table.getRowCount();idx++) {
							if(table.getValueAt(idx, 1).toString().trim().equals(str)) {
								schValue = idx;
							}
						}
						
						if(schValue == -1)
							JOptionPane.showMessageDialog(null, "�̸��� �������� �ʽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
						
						else {
							table.changeSelection(schValue, 1, false, false);
						}
					}
				}
			}
		});
		
		dataI.getRank(client.getInfo());
	}
		
	public void setData(Data data) {
		ArrayList<String> name;
		ArrayList<String> point;
		ArrayList<String> rank;
		
		name = data.getName();
		point = data.getPoint();
		rank = data.getRank();
		
		for(int i=0;i<name.size();++i) {
				Vector<String> userRow = new Vector<String>();
				userRow.addElement(rank.get(i));
				userRow.addElement(name.get(i));
				userRow.addElement(point.get(i));
				model.addRow(userRow);
		}
		table.setModel(model);
	}
}

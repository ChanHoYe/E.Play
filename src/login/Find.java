package login;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import client.Client;
import client.DTO;
import client.Interface;
import client.LoginInterface;
import server.Data;

public class Find extends JFrame {
	private volatile boolean Status = true;
	private Data data;
	
	public Find(Client client) {
		LoginInterface loginI = new Interface();
		
		setTitle("ID/PW 찾기");
		setSize(360, 460);
		setResizable(false);
		setLocationRelativeTo(null);
		
		ImageIcon icon = new ImageIcon("img/find_back.png");
		Image originImg = icon.getImage();
		Image changedImg = originImg.getScaledInstance(360, 460, Image.SCALE_SMOOTH);
		ImageIcon Icon = new ImageIcon(changedImg);
		
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(Icon.getImage(), 0, 0, null);
				setOpaque(false);// 배경 띄워주기
				super.paintComponent(g);
			}
		};
		
		JSplitPane splitPane = new JSplitPane() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(Icon.getImage(), 0, 0, null);
				setOpaque(false);// 배경 띄워주기
				super.paintComponent(g);
			}
		}; 
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOpaque(false);
		add(splitPane, BorderLayout.CENTER);
		//add(panel);
		
		JPanel idPanel = new JPanel();
		idPanel.setLayout(null);
		idPanel.setOpaque(false);
		splitPane.setLeftComponent(idPanel);

		JLabel idName_Lb = new JLabel(new ImageIcon("img/log/name.png"));
		idName_Lb.setBounds(70, 45, 56, 34);
		idPanel.add(idName_Lb);

		JLabel idAge_Lb = new JLabel(new ImageIcon("img/log/age.png"));
		idAge_Lb.setBounds(70, 95, 60, 33);
		idPanel.add(idAge_Lb);

		JTextField idName = new JTextField();
		idName.setBounds(140, 50, 116, 21);
		idPanel.add(idName);
		idName.setColumns(10);

		JTextField idAge = new JTextField();
		idAge.setBounds(140, 100, 116, 21);
		idPanel.add(idAge);
		idAge.setColumns(10);

		JButton btnIdFind = new JButton(new ImageIcon("img/log/find_id.png"));
		btnIdFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnIdFind){
					if(idName.getText().length()==0 || idAge.getText().length()==0)
						JOptionPane.showMessageDialog(null, "정보를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else {
						DTO dto = new DTO();
						dto.setName(idName.getText());
						dto.setAge(Integer.parseInt(idAge.getText()));
						loginI.findID(client.getInfo(), dto);
						idName.setText("");
						idAge.setText("");
						while(Status) {
							System.out.println(1);
						}
						Status = true;
						if(data.getSegment().equals("NOID"))
							JOptionPane.showMessageDialog(null, "ID가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, data.getPacket(), "알림", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		btnIdFind.setBounds(120, 160, 106, 37);
		btnIdFind.setOpaque(false);
		btnIdFind.setBorderPainted(false);
		btnIdFind.setContentAreaFilled(false);
		btnIdFind.setFocusPainted(false);
		idPanel.add(btnIdFind);

		JPanel pwPanel = new JPanel();
		pwPanel.setLayout(null);
		pwPanel.setOpaque(false);
		splitPane.setRightComponent(pwPanel);

		JLabel pwID_Lb = new JLabel(new ImageIcon("img/log/id.png"));
		pwID_Lb.setBounds(70, 30, 44, 39);
		pwPanel.add(pwID_Lb);

		JLabel pwName_Lb = new JLabel(new ImageIcon("img/log/name.png"));
		pwName_Lb.setBounds(70, 85, 56, 34);
		pwPanel.add(pwName_Lb);

		JLabel pwAge_Lb = new JLabel(new ImageIcon("img/log/age.png"));
		pwAge_Lb.setBounds(70, 140, 60, 33);
		pwPanel.add(pwAge_Lb);

		JTextField pwID = new JTextField();
		pwID.setBounds(140, 35, 116, 21);
		pwPanel.add(pwID);
		pwID.setColumns(10);

		JTextField pwName = new JTextField();
		pwName.setBounds(140, 90, 116, 21);
		pwPanel.add(pwName);
		pwName.setColumns(10);

		JTextField pwAge = new JTextField();
		pwAge.setBounds(140, 145, 116, 21);
		pwPanel.add(pwAge);
		pwAge.setColumns(10);

		JButton btnPwFind = new JButton(new ImageIcon("img/log/find_pw.png"));
		btnPwFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnPwFind){
					if(pwID.getText().length()==0 || pwName.getText().length()==0 || pwAge.getText().length()==0)
						JOptionPane.showMessageDialog(null, "정보를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else{
						DTO dto = new DTO();
						dto.setId(pwID.getText());
						dto.setName(pwName.getText());
						dto.setAge(Integer.parseInt(pwAge.getText()));
						loginI.findPW(client.getInfo(), dto);
						pwID.setText("");
						pwName.setText("");
						pwAge.setText("");
						
						while(Status) {}
						
						if(data.getSegment().equals("NOPASS"))
							JOptionPane.showMessageDialog(null, "존재하지 않는 정보입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, data.getPacket(), "알림", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		btnPwFind.setBounds(120, 180, 118, 37);
		btnPwFind.setOpaque(false);
		btnPwFind.setBorderPainted(false);
		btnPwFind.setContentAreaFilled(false);
		btnPwFind.setFocusPainted(false);
		pwPanel.add(btnPwFind);
		splitPane.setDividerLocation(200);
		setVisible(true);
	}
	
	public void setData(Data data) {
		this.data = data;
		this.Status = false;
	}
}

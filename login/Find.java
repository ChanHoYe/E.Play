package login;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import client.Client;
import client.DTO;
import server.Data;

public class Find extends JFrame {
	DTO dto = new DTO();
	public Find(String str) {
		super(str);
		setSize(360, 460);
		setResizable(false);
		setLocation(800, 350);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(null);	
		splitPane.setLeftComponent(panel);

		JLabel lblNewLabel = new JLabel("Name",JLabel.CENTER);
		lblNewLabel.setBounds(70, 50, 57, 15);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Age",JLabel.CENTER);
		lblNewLabel_1.setBounds(70, 100, 57, 15);
		panel.add(lblNewLabel_1);

		JTextField idName = new JTextField();
		idName.setBounds(140, 50, 116, 21);
		panel.add(idName);
		idName.setColumns(10);

		JTextField idAge = new JTextField();
		idAge.setBounds(140, 100, 116, 21);
		panel.add(idAge);
		idAge.setColumns(10);

		JButton btnIdFind = new JButton("ID 찾기");
		btnIdFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object obj = arg0.getSource();
				String str;
				if((JButton)obj == btnIdFind){
					if(idName.getText().length()==0 || idAge.getText().length()==0)
						JOptionPane.showMessageDialog(null, "정보를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else{
						try {
							dto.setName(idName.getText());
							dto.setAge(Integer.parseInt(idAge.getText()));
							idName.setText("");
							idAge.setText("");
							Data data = Client.findID(dto);
							if(data.getSegment().equals("NOID"))
								JOptionPane.showMessageDialog(null, "ID가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							else
								JOptionPane.showMessageDialog(null, data.getData(), "알림", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		btnIdFind.setBounds(125, 165, 97, 23);
		panel.add(btnIdFind);

		JPanel pwPanel = new JPanel();
		pwPanel.setLayout(null);	
		splitPane.setRightComponent(pwPanel);

		JLabel pwID_Lb = new JLabel("ID",JLabel.CENTER);
		pwID_Lb.setBounds(70, 35, 57, 15);
		pwPanel.add(pwID_Lb);

		JLabel pwName_Lb = new JLabel("Name",JLabel.CENTER);
		pwName_Lb.setBounds(70, 90, 57, 15);
		pwPanel.add(pwName_Lb);

		JLabel pwAge_Lb = new JLabel("Age",JLabel.CENTER);
		pwAge_Lb.setBounds(70, 145, 57, 15);
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

		JButton btnPwFind = new JButton("PW 찾기");
		btnPwFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				String str;
				if((JButton)obj == btnPwFind){
					if(pwID.getText().length()==0 || pwName.getText().length()==0 || pwAge.getText().length()==0)
						JOptionPane.showMessageDialog(null, "정보를 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					else{
						try {
							dto.setId(pwID.getText());
							dto.setName(pwName.getText());
							dto.setAge(Integer.parseInt(pwAge.getText()));
							pwID.setText("");
							pwName.setText("");
							pwAge.setText("");
							Data data = Client.findPW(dto);
							if(data.getSegment().equals("NOPASS"))
								JOptionPane.showMessageDialog(null, "존재하지 않는 정보입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							else
								JOptionPane.showMessageDialog(null, data.getData(), "알림", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException | ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		btnPwFind.setBounds(125, 190, 97, 23);
		pwPanel.add(btnPwFind);
		splitPane.setDividerLocation(200);
		setVisible(true);
	}
}

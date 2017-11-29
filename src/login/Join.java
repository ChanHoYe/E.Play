package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import client.DTO;
import server.Data;

public class Join extends JFrame {
	int check=0;
	DTO dto = new DTO();
	
	public Join() {
		setTitle("ȸ������");
		setSize(360, 320);
		setResizable(false);
		setLocation(800, 350);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		setVisible(true);
		panel.setLayout(null);
		
		JLabel nameLabel = new JLabel(new ImageIcon("img/log/name.png"),JLabel.CENTER);
		nameLabel.setBounds(26, 45, 100, 40);
		panel.add(nameLabel);
		
		JLabel ageLabel = new JLabel(new ImageIcon("img/log/age.png"),JLabel.CENTER);
		ageLabel.setBounds(26, 96, 100, 40);
		panel.add(ageLabel);
		
		JLabel idLabel = new JLabel(new ImageIcon("img/log/s_id.png"),JLabel.CENTER);
		idLabel.setBounds(26, 148, 100, 40);
		panel.add(idLabel);
		
		JLabel pwLabel = new JLabel(new ImageIcon("img/log/s_pw.png"),JLabel.CENTER);
		pwLabel.setBounds(26, 199, 100, 40);
		panel.add(pwLabel);
		
		JTextField nameText = new JTextField();
		nameText.setBounds(121, 48, 116, 21);
		panel.add(nameText);
		nameText.setColumns(10);
		
		JTextField ageText = new JTextField();
		ageText.setBounds(121, 99, 116, 21);
		panel.add(ageText);
		ageText.setColumns(10);
		
		JTextField idText = new JTextField();
		idText.setBounds(121, 151, 116, 21);
		panel.add(idText);
		idText.setColumns(10);
		
		JPasswordField passField = new JPasswordField();
		passField.setBounds(121, 202, 116, 21);
		panel.add(passField);
		
		JButton btnJoin = new JButton(new ImageIcon("img/log/join.png"));
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str;
				Object obj = e.getSource();
				
				if((JButton)obj == btnJoin) {
					if(check==0)
						JOptionPane.showMessageDialog(null, "�ߺ�Ȯ���� ���ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					else if(check==1) {
						try {
							dto.setName(nameText.getText());
							dto.setAge(Integer.parseInt(ageText.getText()));
							dto.setId(idText.getText());
							dto.setPassword(passField.getText());
							if (dto.getName().length()==0||dto.getAge()==0||dto.getId().length()==0||dto.getPassword().length()==0)
								JOptionPane.showMessageDialog(null, "������ �Է��ϼ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
							else {
								Data data = Client.insertInfo(dto);
								if(data.getSegment().equals("COMPLETE")){
									JOptionPane.showMessageDialog(null, "���Կ� �����߽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
									dispose();
								}
							}
						} catch (IOException | ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
					else
						JOptionPane.showMessageDialog(null, "ID�� �̹������մϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnJoin.setBounds(121, 249, 120, 40);
		btnJoin.setBorderPainted(false);
		btnJoin.setContentAreaFilled(false);
		btnJoin.setFocusPainted(false);
		panel.add(btnJoin);
		
		JButton btnCheck = new JButton(new ImageIcon("img/log/check.png"));
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				if((JButton)obj == btnCheck){
					dto.setId(idText.getText());
					if(dto.getId().length()<5)
						JOptionPane.showMessageDialog(null, "ID�� 5�� �̻��Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					else {
						try {
							Data data = Client.repuID(dto);
							
							if(data.getSegment().equals("EXIST")) {
								JOptionPane.showMessageDialog(null, "ID�� �̹������մϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
								check=2;
							}
							
							else {
								JOptionPane.showMessageDialog(null, "��밡���� ID�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
								check=1;
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnCheck.setBounds(242, 144, 90, 30);
		btnCheck.setBorderPainted(false);
		btnCheck.setContentAreaFilled(false);
		btnCheck.setFocusPainted(false);
		panel.add(btnCheck);
	}
}
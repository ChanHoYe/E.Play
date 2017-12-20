package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import client.DTO;

public class DataThread extends Thread {
	private static final String JDBC_Driver = "com.mysql.jdbc.Driver"; // JDBC 드라이버
	private static final String jdbcUrl = "jdbc:mysql://localhost:3306/eplay?useUnicode=true&characterEncoding=utf8"; // DB 주소
	private static final String userId = "root"; // 사용자계정
	private static final String userPass = "12345"; // 사용자 패스워드

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	private static final Hashtable<ObjectOutputStream, DTO> userList = new Hashtable<ObjectOutputStream, DTO>();
	
	public DataThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Class.forName(JDBC_Driver);	// 드라이버 로딩: DriverManager에 등록
			conn = DriverManager.getConnection(jdbcUrl, userId, userPass); // Connection 객체를 얻어냄
			stmt = conn.createStatement(); // Statement 객체를 얻어냄
		} catch (ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		try {
			while (true) {
				Data data = (Data)in.readObject();
				String type = data.getType();
				String seg = data.getSegment();
				
				if(type.equals("DATA")) {
					if (seg.equals("LOGIN") || seg.equals("JOIN") || seg.equals("CHECK") || seg.equals("FINDID") || seg.equals("FINDPW") || seg.equals("RANK") || seg.equals("WORD")) {
						Data tmp = getData(seg, data);
						out.writeObject(tmp);
						out.flush();
					} else if (seg.equals("AVATAR") || seg.equals("UPDATE") || seg.equals("ENTER")) {
						setData(seg, data);
					} else if (seg.equals("OUT")) {
						setData(seg,data);
						sleepThread();
						break;
					}
				} else if(type.equals("CHAT")) {
					chat(seg, data);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				socket.close();
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void sleepThread() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Data getData(String segment, Data data) {
		Data writeData = null;
		try {
			DTO tmp = data.getDTO();
			if (segment.equals("LOGIN")) {
				rs = stmt.executeQuery("select * from guest where id ='" + tmp.getId() + "' ");
				if (rs.next()) {
					String pass = rs.getString("password");
					if (pass.equals(tmp.getPassword())) {
						boolean valid = true;
						synchronized (userList) {
							for (DTO dto : userList.values()) {
								if (dto.getId().equals(rs.getString("id"))) {
									valid = false;
									writeData = new Data("LOGIN", "DUPLOGIN");
									break;
								}
							}
						}
						if(valid) {
							tmp.setName(rs.getString("name"));
							tmp.setAge(rs.getInt("age"));
							tmp.setAvatar(rs.getInt("avatar"));
							tmp.setPoint(rs.getInt("point"));
							writeData = new Data("LOGIN", "WELCOME", tmp);
						}
					} else {
						writeData = new Data("LOGIN", "NOPASS");
					}
				} else {
					writeData = new Data("LOGIN", "NOID");
				}
			} else if (segment.equals("JOIN")) {
				stmt.executeUpdate("insert into guest values('" + tmp.getName() + "','" + tmp.getAge() + "','" + tmp.getId() + "','" + tmp.getPassword() + "',0,0)");
				writeData = new Data("JOIN", "COMPLETE");
			} else if (segment.equals("CHECK")) {
				rs = stmt.executeQuery("select * from guest where id ='" + tmp.getId() + "' ");
				if (!rs.next())
					writeData = new Data("JOIN", "EMPTY");
				else
					writeData = new Data("JOIN", "EXIST");
			} else if (segment.equals("FINDID")) {
				rs = stmt.executeQuery(
						"select * from guest where name ='" + tmp.getName() + "'and age ='" + tmp.getAge() + "' ");
				if (rs.next()) {
					String id = rs.getString("ID");
					writeData = new Data("FIND", "ID", id);
				} else {
					writeData = new Data("FIND", "NOID");
				}
			} else if (segment.equals("FINDPW")) {
				rs = stmt.executeQuery("select * from guest where name ='" + tmp.getName() + "'and age ='"
						+ tmp.getAge() + "'and id ='" + tmp.getId() + "' ");
				if (rs.next()) {
					String pass = rs.getString("password");
					writeData = new Data("FIND", "PASS", pass);
				} else {
					writeData = new Data("FIND", "NOPASS");
				}
			} else if (segment.equals("RANK")) {
				ArrayList<String> name = new ArrayList<String>();
				ArrayList<String> point = new ArrayList<String>();
				ArrayList<String> rank = new ArrayList<String>();
				
				rs = stmt.executeQuery(
						"select name, point, (@currank := if(@last > point, @curRank := @curRank + 1, @curRank)) as rank, (@last := point) from guest g, (select @curRank := 1, @last := 0) r order by point desc");
				while (rs.next()) {
					name.add(rs.getString("name"));
					point.add(rs.getString("point"));
					rank.add(rs.getString("rank"));
				}
				
				writeData = new Data("RANK", name, point, rank);
			} else if(segment.equals("WORD")) {
				ArrayList<String> word = new ArrayList<String>();
				ArrayList<String> mean = new ArrayList<String>();
				
				rs = stmt.executeQuery("select * from enkodict");
				while (rs.next()) {
					word.add(rs.getString("word"));
					mean.add(rs.getString("mean"));
				}
				
				writeData = new Data("WORD", word, mean);
			}
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}

		return writeData;
	}

	private void setData(String segment, Data data) {
		try {
			if (segment.equals("AVATAR")) {
				DTO tmp = data.getDTO();
				stmt.executeUpdate("update guest set avatar='" + tmp.getAvatar() + "' where id='" + tmp.getId()
						+ "' and password='" + tmp.getPassword() + "'");
			} else if (segment.equals("UPDATE")) {
				int locationValue = data.getLocationValue();
				int statusValue = data.getStatusValue();
				synchronized (userList) {
					userList.get(out).setLocationValue(locationValue);
					userList.get(out).setStatusValue(statusValue);
				}
			} else if(segment.equals("ENTER")) {
				DTO tmp = data.getDTO();
				synchronized(userList) {
					userList.put(out, tmp);
				}
			} else if (segment.equals("OUT")) {
				DTO tmp = data.getDTO();
				synchronized (userList) {
					for (DTO dto : userList.values()) {
						if (dto.getId().equals(tmp.getId())) {
							userList.remove(out);
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}

	private void chat(String segment, Data data) {
		Data writeData;

		try {
			if (segment.equals("MESSAGE")) {
				String message = data.getMessage();
				String name = data.getDTO().getName();
				if (message.startsWith("/w ") || message.startsWith("/W ")) {
					message = message.substring(3);
					String dest = message.substring(0, message.indexOf(" ")).trim();
					message = message.substring(dest.length() + 1);

					writeData = new Data("CHAT", "WHISPHER", name, message);
					synchronized (userList) {
						for (ObjectOutputStream stream : userList.keySet()) {
							if (userList.get(stream).getName().equals(dest)) {
								stream.writeObject(writeData);
								stream.flush();
							}
						}
					}
				}

				else {
					DTO dto = data.getDTO();
					writeData = new Data("CHAT", "MESSAGE", name, message);
					synchronized (userList) {
						for (ObjectOutputStream stream : userList.keySet()) {
							if (dto.getLocationValue() == userList.get(stream).getLocationValue()) {
								writeData = new Data("CHAT", "MESSAGE", name, message);
								stream.writeObject(writeData);
								stream.flush();
							}
						}
					}
				}
			} else if(segment.equals("ENTER")) {
				DTO dto = data.getDTO();
				synchronized (userList) {
					writeData = new Data("CHAT", "ENTER", dto.getName());
					for (ObjectOutputStream stream : userList.keySet()) {
						if (userList.get(stream).getLocationValue() == dto.getLocationValue()) {
							stream.writeObject(writeData);
							stream.flush();
						}
					}
				}
			} else if (segment.equals("EXIT")) {
				DTO dto = data.getDTO();
				synchronized (userList) {
					writeData = new Data("CHAT", "EXIT", dto.getName());
					for (ObjectOutputStream stream : userList.keySet()) {
						if (stream != out)
							if (userList.get(stream).getLocationValue() == dto.getLocationValue()) {
								stream.writeObject(writeData);
								stream.flush();
							}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

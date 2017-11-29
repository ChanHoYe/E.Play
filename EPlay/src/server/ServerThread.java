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
import java.util.HashSet;

import game.Room;
import game.RoomManager;

public class ServerThread implements Runnable {
	Socket socket = null;
	//private BufferedReader inFromClient = null;
	//private DataOutputStream outToClient = null;
	ObjectOutputStream outToClient = null;
	ObjectInputStream inFromClient = null;
	
	private static final String JDBC_Driver = "com.mysql.jdbc.Driver";	//JDBC 드라이버
	private static final String jdbcUrl = "jdbc:mysql://localhost:3306/eplay?useUnicode=true&characterEncoding=utf8";	//사용하는 데이터베이스명을 포함한 URL
	private static final String userId = "root";	//사용자계정
	private static final String userPass = "12345";		//사용자 패스워드 
	static HashSet<String> currentID = new HashSet<String>();
	static RoomManager roomManager = new RoomManager();
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	public ServerThread(Socket socket) {
		this.socket = socket;
		try {
			//inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			//outToClient = new DataOutputStream(socket.getOutputStream());
			inFromClient = new ObjectInputStream(socket.getInputStream());
			outToClient = new ObjectOutputStream(socket.getOutputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			Class.forName(JDBC_Driver);//드라이버 로딩: DriverManager에 등록
			conn = DriverManager.getConnection(jdbcUrl, userId, userPass);	//Connection 객체를 얻어냄
			stmt = conn.createStatement();	//Statement 객체를 얻어냄
		} catch(ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		try {
			while(true) {
				Data data = null;
				try {
					data = (Data)inFromClient.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				String seg = data.getSegment();				
				if(seg.equals("LOGIN") || seg.equals("JOIN") || seg.equals("CHECK") || seg.equals("FIND")) {
					Data tmp = getAct(seg, data.getData());
					outToClient.writeObject(tmp);
					outToClient.flush();
				}
				else if(seg.equals("AVATAR")) {
					setAct(seg, data.getData());
				}
				else if(seg.equals("RANK")) {
					Data tmp = getRank(seg);
					outToClient.writeObject(tmp);
					outToClient.flush();
				}
				else if(seg.equals("OUT")) {
					Data tmp = getAct(seg, data.getData());
					outToClient.writeObject(tmp);
					outToClient.flush();
					break;
				}
				else if(seg.equals("WORDBOOK")) {
					WordData tmp = getWord(seg);
					outToClient.writeObject(tmp);
					outToClient.flush();
				}
				else if(seg.equals("ENTERROOM") || seg.equals("EXITROOM")) {
					setRoom(seg, data.getData(), data.getRoom());
				}
				else if(seg.equals("CREATEROOM") || seg.equals("LIST")) {
					RoomData tmp = getRoom(seg, data.getData());
					outToClient.writeObject(tmp);
					outToClient.flush();
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {if(inFromClient != null) inFromClient.close();} catch (IOException e) {}
			try {if(outToClient != null) outToClient.close();} catch (IOException e) {}			
			try {socket.close();} catch (IOException e) {}
			sleepThread();
		}
	}
	
	private void sleepThread() {
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Data getAct(String segment, String packet) {
		Data data = null;
		String[] tok = packet.split(" ");
		
		try {
			if(segment.equals("LOGIN")) {
				rs = stmt.executeQuery("select * from guest where id ='" + tok[0] + "' ");
				if(rs.next()) {
					String pass = rs.getString("password");
					if(pass.equals(tok[1])) {
						if(currentID.contains(rs.getString("id")))
							data = new Data("DUPLOGIN");
						else {
							currentID.add(rs.getString("id"));
							String tmp = rs.getString("name") + " " + rs.getString("age") + " " + rs.getString("avatar");
							data = new Data("WELCOME", tmp);
						}
					}
					else {
						data = new Data("NOPASS");
					}
				}
				else {
					data = new Data("NOID");
				}
			}
			else if(segment.equals("JOIN")) {
				int tmp = Integer.parseInt(tok[1]);
				stmt.executeUpdate("insert into guest values('" + tok[0] + "','" + tmp + "','" + tok[2] + "','" + tok[3] + "',0,0)");
				data = new Data("COMPLETE");
			}
			else if(segment.equals("CHECK")) {
				rs = stmt.executeQuery("select * from guest where id ='" + tok[0] + "' ");
				if(!rs.next())
					data = new Data("EMPTY");
				else
					data = new Data("EXIST");
			}
			else if(segment.equals("FIND")) {
				if(tok.length<3) {
					rs = stmt.executeQuery("select * from guest where name ='" + tok[0] + "'and age ='" + Integer.parseInt(tok[1]) + "' ");
					if(rs.next()) {
						String id = rs.getString("ID");
						data = new Data("ID", id);
					}
					else {
						data = new Data("NOID");
					}
				}
				
				else {
					rs = stmt.executeQuery("select * from guest where name ='" + tok[0] + "'and age ='" + Integer.parseInt(tok[1]) + "'and id ='" + tok[2] + "' ");
					if(rs.next()) {
						String pass = rs.getString("password");
						data = new Data("PASS", pass);
					}
					else {
						data = new Data("NOPASS");
					}
				}
			}
			else if(segment.equals("OUT")) {
				if(currentID.contains(tok[0])) {
					currentID.remove(tok[0]);
					data = new Data("FINISH");
				}
				else {
					data = new Data("WHAT");
				}
			}
		} catch(SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
		
		return data;
	}
	
	public void setAct(String segment, String packet) {
		String[] tok = packet.split(" ");
		
		try {
			if(segment.equals("AVATAR")) {
				int tmp = Integer.parseInt(tok[2]);
				stmt.executeUpdate("update guest set avatar='" + tmp + "' where id='" + tok[0] + "' and password='" + tok[1] + "'");
			}			
		} catch(SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}
	
	public Data getRank(String segment) {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> point = new ArrayList<String>();
		ArrayList<String> rank = new ArrayList<String>();
		
		try {
			if(segment.equals("RANK")) {
				rs = stmt.executeQuery("select name, point, (@currank := if(@last > point, @curRank := @curRank + 1, @curRank)) as rank, (@last := point) from guest g, (select @curRank := 1, @last := 0) r order by point desc");
				while(rs.next()){
					name.add(rs.getString("name"));
					point.add(rs.getString("point"));
					rank.add(rs.getString("rank"));
				}
			}
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		Data data = new Data(name, point, rank);
		
		return data;
	}
	
	public WordData getWord(String segment) {
		ArrayList<String> word = new ArrayList<String>();
		ArrayList<String> mean = new ArrayList<String>();
		
		try {
			if(segment.equals("WORDBOOK")) {
				rs = stmt.executeQuery("select * from enkodict");
				while(rs.next()){
					word.add(rs.getString("word"));
					mean.add(rs.getString("mean"));
				}
			}
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		WordData data = new WordData(word, mean);
		
		return data;
	}
	
	public void setRoom(String segment, String packet, Room room) {
		String tok[] = packet.split(" ");
		
		if(segment.equals("ENTERROOM")) {
			room.EnterRoom(tok[0], tok[1]);
		}
		else if(segment.equals("EXITROOM")) {
			room.ExitRoom(tok[0]);
		}
	}
	
	public RoomData getRoom(String segment, String packet) {
		String tok[] = packet.split(" ");
		RoomData data = null;
		
		if(segment.equals("CREATEROOM")) {
			Room room = roomManager.createRoom(tok[0], tok[1], tok[2]);
			data = new RoomData(room);
		}
		
		return data;
	}
}

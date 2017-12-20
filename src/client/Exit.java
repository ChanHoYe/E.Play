package client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

public class Exit implements WindowListener {
	private String seg;
	private Client client;
	
	public Exit(String seg, Client client) {
		this.client = client;
		this.seg = seg;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "Log Out", JOptionPane.YES_NO_OPTION);
		if (exit == 0) {
			if (seg.equals("LOGIN")) {
				client.termLogin();
			} else if (seg.equals("WAIT")) {
				client.termWait(false);
			} else if (seg.equals("MAIN")) {
				client.termMain();
			} else if (seg.equals("ROOM")) {
				client.termRoomUI();
			} else if (seg.equals("WORD")) {
				client.termWordbook();
			} else if (seg.equals("AVATAR")) {
				client.termAvatar();
			}
			client.terminate(seg);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}

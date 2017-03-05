package onlineGame.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProc implements Runnable {

	Socket s;
	BufferedReader in;
	PrintWriter out;
	String name = null;

	public ClientProc(Socket s) throws Exception {
		this.s = s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
	}

	public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (name == null) {
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();
					// only execute Login
					if ("login".startsWith(cmd)) {
						name = st.nextToken();
						UmiServer.loginuser(name);
					}
				} else {
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();
					if ("STAT".equalsIgnoreCase(cmd)) {
						UmiServer.statInfo(out);
					} else if ("UP".equalsIgnoreCase(cmd)) {
						UmiServer.up(name);
					} else if ("DOWN".equalsIgnoreCase(cmd)) {
						UmiServer.down(name);
					} else if ("LEFT".equalsIgnoreCase(cmd)) {
						UmiServer.left(name);
					} else if ("RIGHT".equalsIgnoreCase(cmd)) {
						UmiServer.right(name);
					} else if ("LOGOUT".equalsIgnoreCase(cmd)) {
						UmiServer.logoutUser(name);
						break;
					}
					UmiServer.deleteConnection(s);
					s.close();
				}
			}

		} catch (Exception e) {
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

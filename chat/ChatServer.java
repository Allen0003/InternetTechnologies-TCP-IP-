package chat;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class ChatServer {

	static final int DEFAULT_PORT = 6000;
	static ServerSocket serverSocket;
	static Vector connections;

	public static void sendAll(String s) {
		if (connections != null) {
			for (Enumeration e = connections.elements(); e.hasMoreElements();) {
				try {
					PrintWriter pw = new PrintWriter(((Socket) e.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				} catch (Exception e1) {

				}
			}
		}
		System.out.println(s);
	}

	public static void addConnection(Socket s) {
		if (connections == null) {
			connections = new Vector();
		}
		connections.addElement(s);
	}

	public static void deleteCOnnection(Socket s) {
		if (connections != null) {
			connections.removeElement(s);
		}
	}

	public static void main(String[] arg) {
		int port = DEFAULT_PORT;
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		while (true) {
			try {
				Socket cs = serverSocket.accept();
				addConnection(cs);
				Thread ct = new Thread(new ClientProc(cs));
				ct.start();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

}

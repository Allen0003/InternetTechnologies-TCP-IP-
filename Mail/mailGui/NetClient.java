package mail.mailGui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetClient {

	Socket s;
	BufferedReader from_server;
	PrintWriter to_server;
	String encode = "JIS";

	NetClient(String enc) {
		encode = enc;
		if (MailManager.debug) {
			System.out.println("NetClient.encode:" + encode);
		}
	}

	NetClient() {
		this("JIS");
	}

	public void connect(String server, int port) {
		try {
			s = new Socket(server, port);
			from_server = new BufferedReader(new InputStreamReader(s.getInputStream(), encode));
			to_server = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), encode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// read one line only
	public String receive() {
		String res = null;
		try {
			res = from_server.readLine();
			if (MailManager.debug) {
				System.out.println("RECV>" + res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// send one line only
	public void send(String msg) {
		to_server.println(msg);
		to_server.flush();
		if (MailManager.debug) {
			System.out.println("SEND>" + msg);
		}
	}
}

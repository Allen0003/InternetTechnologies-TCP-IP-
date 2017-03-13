package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProc implements Runnable {

	Socket s;
	BufferedReader in;
	PrintWriter out;
	String name = null;
	ChatServer server = null;

	public ClientProc(Socket s) throws Exception {
		this.s = s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
	}

	@Override
	public void run() {

		try {
			while (name == null) {
				out.print("name: ");
				out.flush();
				name = in.readLine();
			}
			String line = in.readLine();
			while (!"quit".equals(line)) {

			}
		} catch (Exception e) {

		}
	}

}

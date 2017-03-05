package onlineGame.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//client AI code 
public class Robot {

	String userName = "AI";
	String host = "**";

	int sleeptime = 50;
	int timeTolive = 50;

	Socket server;
	int port = 10000;
	BufferedReader in;
	PrintWriter out;

	public Robot() {

		login("localhost", "qoo");

		try {
			for (; timeTolive > 0; --timeTolive) {
				System.out.println(" time = " + timeTolive);
				for (int i = 0; i < 10; i++) {
					Thread.sleep(sleeptime * 100);
					out.println("left");
					out.flush();
				}

				for (int i = 0; i < 10; i++) {
					Thread.sleep(sleeptime * 100);
					out.println("right");
					out.flush();
				}
				out.println("up");
				out.flush();
			}
			out.println("logout");
			out.flush();
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void login(String host, String name) {
		try {
			this.userName = name;
			server = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));

			out = new PrintWriter(server.getOutputStream());
			out.println("login" + name);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}

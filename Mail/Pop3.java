package mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//get mail from SMTP server
public class Pop3 {

	final int POP_PORT = 110;
	BufferedReader pop_in = null;
	PrintWriter pop_out = null;
	Socket pop = null;

	public void transaction() throws Exception {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		boolean cont = true;
		while (cont) {
			System.out.println("Command:L)ist R)etrieve D)elete Q)uit");
			buf = lineread.readLine();
			// QUIT
			if (buf.equalsIgnoreCase("Q")) {
				cont = false;
			}
			// LIST
			else if (buf.equalsIgnoreCase("L")) {
				getLines("LIST");
			}
			// RETR
			else if (buf.equalsIgnoreCase("R")) {
				System.out.println("Number?: ");
				buf = lineread.readLine();
				getLines("RETR" + buf);
			}
			// DELE
			else if (buf.equalsIgnoreCase("D")) {
				System.out.println("Number?: ");
				buf = lineread.readLine();
				getLines("DELE" + buf);
			}
		}
	}

	public void getLines(String command) throws Exception {
		boolean cont = true;
		String buf = null;
		pop_out.println(command + "\r\n");
		pop_out.flush();
		String res = pop_in.readLine();
		System.out.println(res);
		if (!("+OK").equals(res.substring(0, 3))) {
			pop.close();
			throw new RuntimeException(res);
		}
		while (cont) {
			buf = pop_in.readLine();
			System.out.println(buf);
			if (".".equals(buf)) {
				cont = false;
			}
		}
	}

	public void getSingleLine(String command) throws Exception {
		pop_out.println(command + "\r\n");
		pop_out.flush();
		System.out.println(command);
		String res = pop_in.readLine();
		System.out.println(res);
		if (!("+OK").equals(res.substring(0, 3))) {
			pop.close();
			throw new RuntimeException(res);
		}
	}

	public void authorization() throws Exception {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		boolean cont = true;
		String pop_server = null;
		String username = null;
		String password = null;

		while (cont) {
			System.out.println(" POP server address");
			pop_server = lineread.readLine();
			System.out.println(" POP server address: " + pop_server);
			System.out.println(" Y/N ");
			buf = lineread.readLine();
			if ("y".equals(buf)) {
				cont = false;
			}
		}
		pop = new Socket(pop_server, POP_PORT);
		pop_in = new BufferedReader(new InputStreamReader(pop.getInputStream()));
		pop_out = new PrintWriter(pop.getOutputStream());
		String res = pop_in.readLine();
		System.out.println(res);
		if (!("+OK").equals(res.substring(0, 3))) {
			pop.close();
			throw new RuntimeException(res);
		}

		cont = true;
		while (cont) {
			System.out.println(" Uesr name: ");
			username = lineread.readLine();
			System.out.println(" Password: ");
			password = lineread.readLine();
			System.out.println(" Uesr name: " + username);
			System.out.println(" Uesr name: " + password);
			System.out.println(" Y/N ");
			buf = lineread.readLine();
			if ("y".equals(buf)) {
				cont = false;
			}
		}
		getSingleLine("USER" + username);
		getSingleLine("PASS" + password);
	}

	public void update() throws Exception {
		getSingleLine("QUIT");
		pop.close();
	}

	public void mainproc(String[] args) throws Exception {
		if (args.length == 0) {
			authorization();
			transaction();
			update();
		} else {
			System.out.println(" usage: java Pop");
		}
	}

	public static void main(String[] args) {
		Pop3 p = new Pop3();
		try {
			p.mainproc(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

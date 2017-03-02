package mail.mailGui;

public class PopClient extends NetClient {

	static final int POP3_PORT = 10;
	String server;
	String username;
	String password;
	boolean loginFlag = false;

	public PopClient(String serv, String user, String pass, String enc) {
		super(enc);
		username = user;
		password = pass;
		server = serv;
	}

	public boolean login() {
		if (loginFlag) {
			return loginFlag;
		}
		connect(server, POP3_PORT);
		String res = receive();
		if (res.startsWith("-ERR")) {
			System.out.println("connet failed.:" + res);
			disconnect();
			return false;
		}
		send("user " + password);
		res = receive();
		if (res.startsWith("-ERR")) {
			System.out.println("login user failed.:" + res);
			disconnect();
			return false;
		}
		send("pass " + password);
		res = receive();
		if (res.startsWith("-ERR")) {
			System.out.println("login pass failed.:" + res);
			disconnect();
			return false;
		}
		return loginFlag = true;
	}

	public String stat() {
		send("stat");
		String res = "";
		res = receive();
		if (res.startsWith("-ERR")) {
			System.err.println(res);
		}
		return res;
	}

	public String list() {
		String res = "";
		send("list");
		String line = receive();
		if (res.startsWith("-ERR")) {
			System.err.println(res);
			res = line;
		} else {
			while (true) {
				line = receive();
				if (".".equals(line)) {
					break;
				}
				res += line + "\n";
			}
		}
		return res;
	}

	public String retr(int i) {
		String res = "";
		send("retr" + i);
		String line = receive();
		if (res.startsWith("-ERR")) {
			System.err.println(res);
			res = line;
		} else {
			while (true) {
				line = receive();
				if (".".equals(line)) {
					break;
				}
				res += line + "\n";
			}
		}
		return res;
	}

	public String dele(int i) {
		send("dele" + i);
		String res = receive();
		if (res.startsWith("-ERR")) {
			System.err.println(res);
		}
		return res;
	}

	public void quit() {
		if (loginFlag) {
			send("quit");
			loginFlag = false;
		}
	}
}

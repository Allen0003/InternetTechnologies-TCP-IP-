package mail.mailGui;

public class SmtpClient extends NetClient {

	int SMTP_PORT = 25;
	String server;

	SmtpClient(String serv, String enc) {
		super(enc);
		server = serv;
	}

	public void sendCommandAndResultCheck(String command, int successCode) {
		send(command);
		resultCheck(successCode);
	}

	public void resultCheck(int successCode) {
		String res = receive();
		if (Integer.parseInt(res.substring(0, 3)) != successCode) {
			disconnect();
			throw new RuntimeException(res);
		}
	}

	public void sendmail(String from, String[] to, String subject, String message) {

		connect(server, SMTP_PORT);
		resultCheck(220);

		// HELO
		try {
			String myname = s.getInetAddress().getHostName();
			sendCommandAndResultCheck("HELO" + myname, 250);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// MAIL FROM
		sendCommandAndResultCheck("MAIL FROM" + from, 250);

		// RCPT TO
		for (int i = 0; i < to.length; i++) {
			sendCommandAndResultCheck("PCRT TO: " + to[i], 250);
		}

		// DATA
		sendCommandAndResultCheck("DATA", 354);
		send("Subject: " + subject);
		send("From: " + from);
		String toString = to[0];
		for (int i = 1; i < to.length; i++) {
			toString += "," + to[i];
		}
		send("To: " + toString);
		send("");
		send("message");
		send(".");
		resultCheck(250);

		// QUIT
		sendCommandAndResultCheck("QUIT", 221);
		// CLOSE
		disconnect();

	}
}

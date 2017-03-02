package mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Receiver {

	final int SMTP_PORT = 25;

	public void sendResult(Socket smtp, BufferedReader smtp_in, PrintWriter smtp_out, String command) throws Exception {
		smtp_out.print(command + "\r\n");
		smtp_out.flush();
		System.out.println("send>" + command);
	}

	public String[] getCommand(Socket smtp, BufferedReader smtp_in, PrintWriter smtp_out) throws Exception {
		String res = smtp_in.readLine();
		System.out.println("recv>");
		StringTokenizer st = new StringTokenizer(res);
		String[] results = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			results[i] = st.nextToken();
		}
		return results;
	}

	public void procCommand(Socket smtp, BufferedReader smtp_in, PrintWriter smtp_out, String command,
			String acceptableCommand, String result) throws Exception {
		if (acceptableCommand.equalsIgnoreCase(command)) {
			sendResult(smtp, smtp_in, smtp_out, result);
		} else {
			sendResult(smtp, smtp_in, smtp_out, "421 service not available");
			smtp.close();
		}
	}

	public void receive(Socket smtp) throws Exception {
		String[] commands = null;
		BufferedReader smtp_in = new BufferedReader(new InputStreamReader(smtp.getInputStream()));
		PrintWriter smtp_out = new PrintWriter(smtp.getOutputStream());
		String myname = InetAddress.getLocalHost().getHostName();
		sendResult(smtp, smtp_in, smtp_out, "220" + myname + "SMTP");

		// HELO
		commands = getCommand(smtp, smtp_in, smtp_out);
		procCommand(smtp, smtp_in, smtp_out, commands[0], "HELO",
				"250" + "Hello" + commands[1] + ",pleased to meet you");

		// MAIL FROM
		commands = getCommand(smtp, smtp_in, smtp_out);
		procCommand(smtp, smtp_in, smtp_out, commands[0], "MAIL",
				"250" + commands[1].substring(commands[1].indexOf(":") + 1) + "...Send ok");

		// RCPT TO
		commands = getCommand(smtp, smtp_in, smtp_out);
		procCommand(smtp, smtp_in, smtp_out, commands[0], "RCPT", "250 Recipient ok");

		// DATA
		commands = getCommand(smtp, smtp_in, smtp_out);
		procCommand(smtp, smtp_in, smtp_out, commands[0], "DATA", "354 Enter mail, end with\".\" on a line by itself");

		String res = smtp_in.readLine();
		while (!(res.equals("."))) {
			System.out.println(res);
			res = smtp_in.readLine();
		}

		sendResult(smtp, smtp_in, smtp_out, "250 Message accepted for delivery");
		commands = getCommand(smtp, smtp_in, smtp_out);
		procCommand(smtp, smtp_in, smtp_out, commands[0], "QUIT", "221" + myname + " closing connection");

		smtp.close();
	}

	@SuppressWarnings("resource")
	public void mainproc() throws Exception {
		Socket sock;
		ServerSocket serversock = new ServerSocket(SMTP_PORT, 1);
		while (true) {
			sock = serversock.accept();
			receive(sock);
		}
	}

	public static void main(String[] args) {
		Receiver r = new Receiver();
		try {
			r.mainproc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

public class MailSMTP {
	// send mail by SMTP

	final int SMTP_PORT = 25;
	String smtp_server = "";
	String my_email_addr = "";

	public void sendCommandAndResultCheck(Socket smtp, BufferedReader smtp_in, PrintWriter smtp_out, String command,
			int success_code) throws Exception {

		smtp_out.print(command + "\r\n");
		smtp_out.flush();
		System.out.println(" sned>" + command);
		resultCheck(smtp, smtp_in, smtp_out, success_code);
	}

	public void resultCheck(Socket smtp, BufferedReader smtp_in, PrintWriter smtp_out, int success_code)
			throws Exception {

		String res = smtp_in.readLine();
		System.out.println("recv>" + res);
		if (Integer.parseInt(res.substring(0, 3)) != success_code) {
			smtp.close();
			throw new RuntimeException(res);
		}
	}

	// send
	public void send(String subject, String[] to, String[] msgs) throws Exception {

		Socket smtp = new Socket(smtp_server, SMTP_PORT);
		BufferedReader smtp_in = new BufferedReader(new InputStreamReader(smtp.getInputStream()));

		PrintWriter smtp_out = new PrintWriter(smtp.getOutputStream());

		resultCheck(smtp, smtp_in, smtp_out, 220);

		// helo

		String myname = InetAddress.getLocalHost().getHostName();
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out, "HELO " + myname, 250);

		// maol from
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out, "MAIL FROM:" + my_email_addr, 250);

		// PCRT TO

		for (int i = 0; i < to.length; i++) {
			sendCommandAndResultCheck(smtp, smtp_in, smtp_out, "RCPT TO:" + to[i], 250);
		}

		// DATA
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out, "DATA", 354);

		// Subject
		smtp_out.print("Subject:" + subject + "\r\n");
		System.out.println("send>" + "Subject:" + subject);
		smtp_out.print("\r\n");

		// content in here
		for (int i = 0; i < msgs.length - 1; i++) {
			smtp_out.print(msgs[i] + "\r\n");
			System.out.println("send>" + msgs[i]);
		}

		sendCommandAndResultCheck(smtp, smtp_in, smtp_out, "\r\n", 250);
		// QUIT
		smtp.close();
	}

	public void setAddress() throws Exception {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		boolean cont = true;
		while (cont) {
			System.out.println(" target email address");
			smtp_server = lineread.readLine();
			System.out.println(" my local email");
			my_email_addr = lineread.readLine();
			System.out.println(" target email address: " + smtp_server);
			System.out.println(" my local email:" + my_email_addr);
			System.out.println(" Y/N ");
			buf = lineread.readLine();
			if ("y".equals(buf.toLowerCase())) {
				cont = false;
			}
		}
	}

	public String[] setMsgs() throws Exception {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		boolean cont = true;
		Vector<String> msgs_list = new Vector<String>();
		String[] msgs = null;
		System.out.println("please type content");
		System.out.println(" type '.' to end ");

		while (cont) {
			buf = lineread.readLine();
			msgs_list.addElement(buf);
			if (".".equals(buf)) {
				cont = false;
			}
			msgs = new String[msgs_list.size()];
			msgs_list.copyInto(msgs);
		}
		return msgs;
	}

	public void mainproc(String[] args) throws Exception {
		String usage = "java MAil[-s subject] to-addr...";
		String subject = "";
		Vector<String> to_list = new Vector<String>();

		for (int i = 0; i < args.length; i++) {
			if ("-s".equals(args[i])) {
				i++;
				subject = args[i];
			} else {
				to_list.addElement(args[i]);
			}
		}

		if (to_list.size() > 0) {
			String[] to = new String[to_list.size()];
			to_list.copyInto(to);
			setAddress();
			String[] msgs = setMsgs();
			send(subject, to, msgs);
		} else {
			System.out.println(" usage: " + usage);
		}
	}

	public static void main(String[] args) {
		MailSMTP m = new MailSMTP();
		try {
			m.mainproc(new String[] { "hello", "apss1943@gmail.com" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package mail.mailGui;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class MailManager {

	static boolean debug = true;
	Properties property;
	String home;
	String cf;
	File mailDir;
	String fsep;

	String username;
	String password;
	String fromString;

	String popServ;
	String smtpServ;

	boolean keepmail = false;
	String codeNet;
	String codeFile;

	int lastMail_NO = 0;

	// constructor
	public MailManager() {
		Properties prop = System.getProperties();
		home = prop.getProperty("user.home");
		fsep = prop.getProperty("file.separator");
		cf = home + fsep + "jet.properties";

		// property = new Properties();
		// try {
		// property.load(new FileInputStream(cf));
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.exit(1);
		// }

		username = "test";
		password = "test";
		popServ = "popServ";
		smtpServ = "smtpServ";
		fromString = "fromString";

		if (fromString == null) {
			fromString = username + "@" + popServ;
		}
		String dummy = "fromString";
		keepmail = dummy == null ? false : "off".equalsIgnoreCase("dummy") ? false : true;

		codeNet = "codeNet";
		codeFile = "codeFile";

		String propMailDir = property.getProperty("maildir");
		mailDir = new File(propMailDir);

		if (mailDir.exists()) {
			if (!mailDir.isDirectory()) {
				System.err.println("not a directory:" + mailDir.toString());
				System.exit(1);
			}
		} else {
			System.err.println("not found mail directory:" + mailDir.toString());
			System.err.println("add create it");
			mailDir.mkdir();
		}

		String[] files = mailDir.list();
		for (int i = 0; i < files.length; i++) {
			try {
				int num = Integer.parseInt(files[i]);
				if (lastMail_NO < num) {
					lastMail_NO = num;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	// show mail list
	public String[] list() {

		Vector v = new Vector();
		for (int i = 1; i <= lastMail_NO; i++) {
			try {
				if (debug) {
					System.out.println("MM.list: scan file" + i);
				}
				Mail mail = new Mail(1);
				String space10 = "          ";
				String from = mail.from + space10 + space10;
				from = from.substring(0, 20);
				String subject = mail.subject + space10 + space10 + space10 + space10;
				subject = subject.substring(0, 40);

				String num = i + space10;
				v.addElement(num.subSequence(0, 8) + "[" + from + "]" + subject);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String[] res = new String[v.size()];
		v.copyInto(res);
		return res;
	}

	public String readMail(int i) {
		try {
			Mail mail = new Mail(1);
			return mail.message;
		} catch (Exception e) {
			return null;
		}
	}

	public void sendmail(String to, String subject, String message) {
		SmtpClient smtp = new SmtpClient(smtpServ, codeNet);
		String[] dummy = new String[1];
		dummy[0] = to;
		smtp.sendmail(fromString, dummy, subject, message);
	}

	public int getmail() {
		int n = 0;
		PopClient pop = new PopClient(popServ, username, password, codeNet);

		pop.login();
		String res = pop.stat();
		StringTokenizer st = new StringTokenizer(res);
		if ("+OK".equals(st.nextToken())) {
			n = Integer.parseInt(st.nextToken());
			for (int i = 1; i <= n; i++) {
				try {
					new Mail(pop.retr(i), lastMail_NO, mailDir);
					if (!keepmail) {
						pop.dele(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		pop.quit();
		return n;
	}

}

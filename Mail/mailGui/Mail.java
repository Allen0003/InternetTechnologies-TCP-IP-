package mail.mailGui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Mail extends MailManager {

	String message;
	String subject;
	String to;
	String from;
	String replyto;
	File file;

	public Mail(String msg, int lastMail_NO, File mailDir) throws Exception {
		message = msg;
		lastMail_NO++;
		file = new File(mailDir.getPath() + fsep + lastMail_NO);
		PrintWriter mail = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), codeFile));
		mail.print(msg);
		mail.flush();
		mail.close();
		parse();
	}

	public Mail(int i) throws Exception {

		file = new File(mailDir.getPath() + fsep + lastMail_NO);
		@SuppressWarnings("resource")
		InputStreamReader mailIn = new InputStreamReader(new FileInputStream(file), codeFile);
		char[] cbuf = new char[1024];
		StringBuffer sb = new StringBuffer();
		while (true) {
			int n = mailIn.read(cbuf, 0, 1024);
			if (n == -1) {
				break;
			}
			sb.append(cbuf, 0, n);
		}
		message = sb.toString();
	}

	// using parseHeader to analyze head information
	public void parse() {
		from = parseHeader("From:");
		to = parseHeader("To:");
		subject = parseHeader("Subject:");
		replyto = parseHeader("Reply-To:");
	}

	public String parseHeader(String header) {
		int begin = 0;
		int end = 0;
		for (end = message.indexOf("\n", begin); end >= 0
				&& begin >= 0; begin = end + 1, end = message.indexOf("\n", begin)) {
			String line = message.substring(begin, end);
			if ("".equals(line)) {
				break;
			}
			if (line.startsWith(header)) {
				return line.substring(header.length());
			}
		}
		return null;
	}

}

package ftp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * no file relative thing, only handle connection
 * 
 */
public class Ftp1 {

	public static void main(String[] arg) throws Exception {
		Ftp1 f = null;
		f = new Ftp1();
		f.openConnection("localhost");
		f.getMsgs();
		f.main_proc();
		f.closeConnection();
	}

	Socket ctrlSocket;
	public PrintWriter ctrlOutput;
	public BufferedReader ctrlInput;
	final int CTRLPORT = 21;

	public void openConnection(String host) throws Exception {
		ctrlSocket = new Socket(host, CTRLPORT);
		ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
		ctrlInput = new BufferedReader(new InputStreamReader(ctrlSocket.getInputStream()));
	}

	public void closeConnection() throws Exception {
		ctrlSocket.close();
	}

	public void showMenu() {
		System.out.println(">Command?");
		System.out.println("1 login");
		System.out.println("9 quit");
	}

	public String getCommand() throws Exception {
		String buf = "";
		BufferedReader linered = new BufferedReader(new InputStreamReader(System.in));

		while (buf.length() != 1) {
			buf = linered.readLine();
		}
		return buf;
	}

	public void doLogin() throws Exception {
		String loginName = "";
		String password = "";

		BufferedReader linered = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter user name: ");
		loginName = linered.readLine();

		ctrlOutput.println("USER " + loginName);
		ctrlOutput.flush();

		System.out.println("Enter password: ");
		password = linered.readLine();
		ctrlOutput.println("PASS " + password);
		ctrlOutput.flush();

	}

	public void doQuit() {
		ctrlOutput.println("QUIT");
		ctrlOutput.flush();
	}

	public boolean execCommand(String command) throws Exception {
		boolean cont = true;

		switch (Integer.parseInt(command)) {
		case 1:
			doLogin();
			break;
		case 9:
			doQuit();
			cont = false;
			break;
		default:
			System.out.println("please enter your choice");

		}
		return (cont);
	}

	public void main_proc() throws Exception {
		boolean cont = true;
		while (cont) {
			showMenu();
			cont = execCommand(getCommand());
		}
	}

	public void getMsgs() {
		CtrlListen listener = new CtrlListen(ctrlInput);
		Thread listenerthread = new Thread(listener);
		listenerthread.start();
	}

}

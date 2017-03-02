package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * add 'list' function
 * 
 */
public class Ftp2 {

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
		System.out.println(" 2 ls");
		System.out.println(" 3 cd");
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

	public void doCd() throws Exception {
		String dirName = "";
		BufferedReader linered = new BufferedReader(new InputStreamReader(System.in));
		dirName = linered.readLine();
		ctrlOutput.println("CWD" + dirName);
		ctrlOutput.flush();
	}

	public void doLs() throws Exception {

		int n;
		byte[] buff = new byte[1024];
		Socket dataSocket = dataConnection("LIST");
		BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
		while ((n = dataInput.read(buff)) > 0) {
			System.out.write(buff, 0, n);
		}

	}

	public Socket dataConnection(String ctrlcmd) throws Exception {

		String cmd = "PORT";
		int i;
		Socket dataSocket = null;
		byte[] address = InetAddress.getLocalHost().getAddress();
		// create server socket!
		ServerSocket serverDataSocket = new ServerSocket(0, 1);
		for (i = 0; i < 4; i++) {
			cmd = cmd + (address[i] & 0xff) + ",";
		}

		cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff) + "," + (serverDataSocket.getLocalPort() & 0xff);

		ctrlOutput.println(cmd);
		ctrlOutput.flush();

		ctrlOutput.println(cmd);
		ctrlOutput.flush();

		dataSocket = serverDataSocket.accept();
		serverDataSocket.close();

		return dataSocket;

	}

	public boolean execCommand(String command) throws Exception {
		boolean cont = true;

		switch (Integer.parseInt(command)) {
		case 1:
			doLogin();
			break;

		case 2:
			doLs();
			break;
		case 3:
			doCd();
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

	public static void main(String[] arg) throws Exception {
		Ftp2 f = null;
		f = new Ftp2();
		f.openConnection("localhost");
		f.getMsgs();
		f.main_proc();
		f.closeConnection();
	}

}

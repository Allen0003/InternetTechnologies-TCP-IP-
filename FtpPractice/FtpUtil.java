package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpUtil {

	Socket ctrlSocket;
	public PrintWriter ctrlOutput;
	public BufferedReader ctrlInput;

	final int CTRLPORT = 21;

	public FtpUtil(Socket ctrlSocket, PrintWriter ctrlOutput, BufferedReader ctrlInput) {
		this.ctrlSocket = ctrlSocket;
		this.ctrlOutput = ctrlOutput;
		this.ctrlInput = ctrlInput;
	}

	public FtpUtil() throws Exception {
		this.openConnection("localhost");
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

	public void doAscii() throws Exception {
		ctrlOutput.println("TYPE A");
		ctrlOutput.flush();
	}

	public void doBinary() throws Exception {
		ctrlOutput.println("TYPE 1");
		ctrlOutput.flush();
	}

	public void doGet() throws Exception {
		String fileName = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		int n;
		byte[] buff = new byte[1024];
		System.out.println("please enter file name");
		fileName = lineread.readLine();
		FileOutputStream outfile = new FileOutputStream(fileName);
		Socket dataSocket = dataConnection("RETR" + fileName);

		// get data flow from server and write down to file
		BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());

		while ((n = dataInput.read(buff)) > 0) {
			outfile.write(buff, 0, n);
		}
		dataSocket.close();
		outfile.close();
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

	public void doPut() throws Exception {
		String fileName = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		int n;
		byte[] buff = new byte[1024];
		FileInputStream sendFile = null;
		System.out.println("please enter file name");
		fileName = lineread.readLine();
		try {
			sendFile = new FileInputStream(fileName);
		} catch (Exception e) {
			System.out.println("file doesn't exist");
		}
		Socket dataSocket = dataConnection("STOR" + fileName);
		OutputStream outstr = dataSocket.getOutputStream();
		while ((n = sendFile.read(buff)) > 0) {
			outstr.write(buff, 0, n);
		}
		dataSocket.close();
		sendFile.close();
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
		case 4:
			doGet();
			break;
		case 5:
			doPut();
			break;
		case 6:
			doAscii();
			break;
		case 7:
			doBinary();
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

	public void openConnection(String host) throws Exception {
		this.ctrlSocket = new Socket(host, CTRLPORT);
		this.ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
		this.ctrlInput = new BufferedReader(new InputStreamReader(ctrlSocket.getInputStream()));
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

	public void showMenu() {
		System.out.println(">Command?");
		System.out.println("1 login");
		System.out.println(" 2 ls");
		System.out.println(" 3 cd");
		System.out.println(" 4 get");
		System.out.println(" 5 put");
		System.out.println(" 6 ascii");
		System.out.println(" 7 binary");
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

	public void closeConnection(Socket ctrlSocket) throws Exception {
		ctrlSocket.close();
	}

	public void closeConnection() throws Exception {
		this.ctrlSocket.close();
	}

	public void getMsgs() {
		CtrlListen listener = new CtrlListen(ctrlInput);
		Thread listenerthread = new Thread(listener);
		listenerthread.start();
	}

	public void main_proc() throws Exception {
		boolean cont = true;
		while (cont) {
			showMenu();
			cont = execCommand(getCommand());
		}
	}

	@SuppressWarnings("resource")
	// The container will inject the resource referred to by @Resource into the
	// component either at runtime or when the component is initialized,
	// depending on whether field/method injection or class injection is used.
	public void openConnection(Socket ctrlSocket, PrintWriter ctrlOutput, BufferedReader ctrlInput, String host)
			throws Exception {
		ctrlSocket = new Socket(host, CTRLPORT);
		ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
		ctrlInput = new BufferedReader(new InputStreamReader(ctrlSocket.getInputStream()));
	}

}

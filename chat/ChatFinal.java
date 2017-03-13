package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 
 * Chatting with multicast
 * 
 * client and server in the same time
 * 
 * 
 */
public class ChatFinal {
	final byte TTL = 1;
	final String MULTICASTADDRESS = "224.0.0.1";
	int port = 6000;
	byte[] buff = new byte[1024];
	String myname = "";
	int nameLength = 0;
	MulticastSocket soc = null;
	InetAddress chatgroup = null;

	public ChatFinal(int port) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		this.port = port;
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("name :");

		try {
			myname = lineread.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(" welcome " + myname + "!");
		myname = myname + ">";
		nameLength = (myname.getBytes()).length;
		for (int i = 0; i < nameLength; i++) {
			buff[i] = (myname.getBytes())[i];
		}
	}

	public void makeMulticastSocket() {
		try {
			chatgroup = InetAddress.getByName(MULTICASTADDRESS);
			soc = new MulticastSocket(port);
			soc.joinGroup(chatgroup);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startListener() {
		try {
			ListenPacket lisner = new ListenPacket(soc);
			Thread lisner_thread = new Thread(lisner);
			lisner_thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMsgs() {
		try {
			while (true) {
				int n = System.in.read(buff, nameLength, 1024 - nameLength);
				if (n > 0) {
					DatagramPacket dp = new DatagramPacket(buff, n + nameLength, chatgroup, port);
					soc.send(dp);
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void quitGroup() {
		try {
			System.out.println(" quit connection ");
			soc.leaveGroup(chatgroup);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatFinal c = null;
		int portno = 6000;

		c = new ChatFinal(portno);
		c.makeMulticastSocket();
		c.startListener();
		c.sendMsgs();
		c.quitGroup();
	}

}

package chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 
 * chatting room client
 * 
 */

public class Msend {

	public static void main(String[] args) {

		String multicastAddress = "224.0.0.1";
		int port = 6000;
		byte[] buff = new byte[1024];

		System.out.println("start client! ");
		
		try {
			System.setProperty("java.net.preferIPv4Stack", "true");
			InetAddress chatgroup = InetAddress.getByName(multicastAddress);
			@SuppressWarnings("resource")
			MulticastSocket soc = new MulticastSocket(port);
			soc.joinGroup(chatgroup);
			while (true) {
				int n = System.in.read(buff, 0, buff.length);
				if (n > 0) {
					DatagramPacket dp = new DatagramPacket(buff, n, chatgroup, port);
					soc.send(dp);
				} else {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

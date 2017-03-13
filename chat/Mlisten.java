package chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 
 * chatting room server
 * 
 */

public class Mlisten {

	public static void main(String[] arg) {
		String multicastAddress = "224.0.0.1";
		int port = 6000;
		byte[] buff = new byte[1024];
		try {
			System.setProperty("java.net.preferIPv4Stack", "true");
			InetAddress chatgroup = InetAddress.getByName(multicastAddress);
			@SuppressWarnings("resource")
			MulticastSocket soc = new MulticastSocket(port);
			soc.joinGroup(chatgroup);
			while (true) {
				DatagramPacket recv = new DatagramPacket(buff, buff.length);
				soc.receive(recv);
				if (recv.getLength() > 0) {
					System.out.write(buff, 0, recv.getLength());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

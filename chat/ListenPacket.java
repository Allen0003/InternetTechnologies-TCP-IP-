package chat;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ListenPacket implements Runnable {

	MulticastSocket s = null;

	public ListenPacket(MulticastSocket soc) {
		s = soc;
	}

	@Override
	public void run() {
		byte[] buff = new byte[1024];
		try {
			while (true) {
				DatagramPacket recv = new DatagramPacket(buff, buff.length);
				s.receive(recv);
				if (recv.getLength() > 0) {
					System.out.write(buff, 0, recv.getLength());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

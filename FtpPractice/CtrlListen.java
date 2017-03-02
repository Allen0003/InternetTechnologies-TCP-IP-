package ftp;

import java.io.BufferedReader;

public class CtrlListen implements Runnable {
	BufferedReader ctrlInput = null;

	public CtrlListen(BufferedReader in) {
		ctrlInput = in;
	}

	@Override
	public void run() {
		try {
			System.out.println(ctrlInput.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
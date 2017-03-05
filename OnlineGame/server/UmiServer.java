package onlineGame.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

public class UmiServer {

	static final int DEFAILT_PORT = 10000;

	static ServerSocket serverSocket;
	static Vector<Socket> connections;
	static Vector<int[]> energy_v;
	static Hashtable<String, Ship> userTable;
	static Random random = null;

	public static void addConnection(Socket s) {
		if (connections == null) {
			connections = new Vector<Socket>();
		}
		connections.addElement(s);
	}

	public static void deleteConnection(Socket s) {
		if (connections != null) {
			connections.removeElement(s);
		}
	}

	public static void loginuser(String name) {
		if (userTable == null) {
			userTable = new Hashtable<String, Ship>();
		}
		if (random == null) {
			random = new Random();
		}

		int ix = Math.abs(random.nextInt()) % 256;
		int iy = Math.abs(random.nextInt()) % 256;

		userTable.put(name, new Ship(ix, iy));
		System.out.println("login: " + name);
		System.out.flush();
	}

	public static void logoutUser(String name) {
		System.out.println("logout: " + name);
		System.out.flush();
		userTable.remove(name);
	}

	public static void left(String name) {
		Ship ship = (Ship) userTable.get(name);
		ship.left();
		calculation();
	}

	public static void right(String name) {
		Ship ship = (Ship) userTable.get(name);
		ship.right();
		calculation();
	}

	public static void up(String name) {
		Ship ship = (Ship) userTable.get(name);
		ship.up();
		calculation();
	}

	public static void down(String name) {
		Ship ship = (Ship) userTable.get(name);
		ship.down();
		calculation();
	}

	public static void calculation() {
		if (userTable != null && energy_v != null) {
			// check all the users
			for (Enumeration<String> users = userTable.keys(); users.hasMoreElements();) {
				String user = users.nextElement().toString();
				Ship ship = (Ship) userTable.get(user);
				for (Enumeration<int[]> energys = energy_v.elements(); energys.hasMoreElements();) {
					int[] e = (int[]) energys.nextElement();
					int x = e[0] - ship.x;
					int y = e[1] - ship.y;
					double r = Math.sqrt(x * x + y * y);
					if (r < 10) {
						energy_v.removeElement(e);
						ship.point++;
					}
				}
			}
		}
	}

	public static void statInfo(PrintWriter pw) {
		pw.println("ship_info");
		if (userTable != null) {
			for (Enumeration<String> users = userTable.keys(); users.hasMoreElements();) {
				String user = users.nextElement().toLowerCase();
				Ship ship = (Ship) userTable.get(user);
				pw.println(user + "" + ship.x + " " + ship.y + " " + ship.point);
			}
		}
		pw.println(".");
		pw.println("energy_info");
		if (energy_v != null) {
			for (Enumeration<int[]> energys = energy_v.elements(); energys.hasMoreElements();) {
				int[] e = (int[]) energys.nextElement();
				pw.println(e[0] + " " + e[1]);
			}
		}
		pw.print(".");
		pw.flush();
	}

	public static void putEnergy() {
		if (energy_v == null) {
			energy_v = new Vector<int[]>();
		}

		if (random == null) {
			random = new Random();
		}

		int[] e = new int[2];
		e[0] = Math.abs(random.nextInt()) % 256;
		e[1] = Math.abs(random.nextInt()) % 256;

		energy_v.addElement(e);
	}

	public static void main(String arg[]) {
		try {
			serverSocket = new ServerSocket(DEFAILT_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread et = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(10 * 1000);
					} catch (Exception e) {
						break;
					}
					UmiServer.putEnergy();
				}
			}
		};

		et.start();
		while (true) {
			try {
				Socket cs = serverSocket.accept();
				addConnection(cs);
				// one connection one client
				Thread ct = new Thread(new ClientProc(cs));
				ct.start();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}

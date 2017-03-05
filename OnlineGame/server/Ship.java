package onlineGame.server;

public class Ship {

	int x;
	int y;

	int point = 0;

	public Ship(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void left() {
		x -= 10;
		if (x < 0) {
			x += 256;
		}
	}

	public void right() {
		x += 10;
		x %= 256;
	}

	public void up() {
		y += 10;
		y %= 256;
	}

	public void down() {
		y -= 10;
		if (y < 0) {
			y += 256;
		}
	}
}

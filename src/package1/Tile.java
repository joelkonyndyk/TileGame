package package1;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile extends GameObject implements EntityA {

	// private double x;
	// private double y;

	private int tileVal = 0;

	private double speed = 1;

	private double velX = 0;
	private double velY = 0;

	private boolean breakT = false;

	private BufferedImage tile;

	private Textures tex;
	private Game game;
	private Controller c;

	private Animation anim;
	private Animation animBreak;

	private int animSpeed = 5;

	public Tile(double x, double y, int row, int col, Textures tex, Game game,
			Controller c) {

		super(x, y);

		// velX = speed;
		// velY = speed;

		// this.x = x;
		// this.y = y;
		this.tex = tex;
		this.game = game;
		this.c = c;

		/**
		 * The below code is working towards adding animations when the tiles
		 * break
		 **/
		// if (row == 1) {
		// // anim = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		// animBreak = new Animation(animSpeed, tex.blueTile[0],
		// tex.blueTile[1], tex.blueTile[2], tex.blueTile[3]);
		//
		// } else if (row == 2) {
		// // anim = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		// animBreak = new Animation(animSpeed, tex.redTile[0],
		// tex.redTile[1], tex.redTile[2], tex.redTile[3]);
		//
		// } else if (row == 3) {
		// // anim = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		// animBreak = new Animation(animSpeed, tex.goldTile[0],
		// tex.goldTile[1], tex.goldTile[2], tex.goldTile[3]);
		//
		// } else if (row == 4) {
		// // anim = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		// animBreak = new Animation(animSpeed, tex.silverTile[0],
		// tex.silverTile[1], tex.silverTile[2], tex.silverTile[3]);
		//
		// } else if (row == 5) {
		// // anim = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		// // animBreak = new Animation(5, tex.player[0], tex.player[1],
		// // tex.player[2]);
		//
		// }

		// anim = new Animation(5, tex.player[0], tex.player[1], tex.player[2]);
		// animBreak = new Animation(5, tex.player[0], tex.player[1],
		// tex.player[2]);

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());

		tile = ss.grabImage(row, col, 32, 32);
	}

	public void tick() {
		x += velX;
		y += velY;

		if (x <= 0) {
			x = 0;
		}
		if (x >= game.getHeight() - 32) {
			x = game.getHeight() - 32;
		}
		if (y <= 0) {
			y = 0;
		}
		if (y >= game.getWidth() - 32) {
			y = game.getWidth() - 32;
		}

		for (int i = 0; i < game.ea.size(); i++) {
			EntityA tempEnt = game.ea.get(i);

			if (Physics.Collision(this, tempEnt)) {

				// velX = 0;
				// velY = 0;

				// c.removeEntity(tempEnt);
				// Game.HEALTH -= 10;

				// game.setEnemy_killed(game.getEnemy_killed() + 1);
			}
		}

		// if (breakT) {
		// animBreak.runAnimation();
		// }

	}

	// public void breakTile() {
	// animBreak.runAnimation();
	// }

	public void render(Graphics g) {

		// if (breakT) {
		// animBreak.drawAnimation(g, x, y, 0);
		// }
		g.drawImage(tile, (int) y, (int) x, null);

		// anim.drawAnimation(g, x, y, 0);
		// g.drawImage(tex.player[0], (int) x, (int) y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) y, (int) x, 32, 32);
	}

	public BufferedImage getImage() {
		return tile;
	}

	public void setImage(BufferedImage b) {
		tile = b;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public boolean getBreakT() {
		return breakT;
	}

	public void setBreakT(boolean b) {
		this.breakT = b;
	}

	public void setTileVal(int i) {
		tileVal = i;
	}

	public int getTileVal() {
		return tileVal;
	}

}

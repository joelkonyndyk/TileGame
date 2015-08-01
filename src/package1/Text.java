package package1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Text implements EntityB {

	private Game game;
	private String str;
	private Controller cont;

	private double speed = 4;

	public double x;
	public double y;

	private Font fnt0 = new Font("arial", Font.BOLD, 20);
	// private int fntSize = 20;

	private Composite c;
	private float transparency = 1.0f;

	public Text(String str, Game game, Controller cont, double x, double y) {

		this.game = game;
		this.str = str;
		this.cont = cont;

		this.x = x;
		this.y = y;

		// fnt0 = new Font("arial", Font.BOLD, fntSize);

		c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);

	}

	public void tick() {

		y -= speed;
		// fntSize++;

		// transparency -= .1f;

		if (transparency > 0.1f) {
			c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					transparency -= .035f);
		} else {
			cont.removeEntity(this);
		}

		// if (fntSize > 30) {
		// cont.removeEntity(this);
		// }
		// fnt0.deriveFont(fntSize);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(c);
		g2d.setFont(fnt0);
		DrawOutline(str, (int) x, (int) y, g2d);
	}

	public void DrawOutline(String str, int x, int y, Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawString(str, x - 1, y);
		g2d.drawString(str, x + 1, y);
		g2d.drawString(str, x, y - 1);
		g2d.drawString(str, x, y + 1);

		g2d.setColor(Color.WHITE);
		g2d.drawString(str, x, y);
	}

	public void setFont(Font f) {
		fnt0 = f;
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

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	// public int getTextSize() {
	// return fntSize;
	// }

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}

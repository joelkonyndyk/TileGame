package package1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class LoadScreen extends JPanel {

	private Game game;

	private BufferedImage logo = null;
	private BufferedImage backgroundimg = null;

	private boolean gameLoading = true;

	private int loadCount = 0;

	private int loadPercent = 0;

	private final int waitCount = 200;

	public LoadScreen(Game game) {
		this.game = game;

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/BOStudios.png");
			backgroundimg = loader.loadImage("/background.png");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void init() {

	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		Font fnt0 = new Font("arial", Font.BOLD, 50);
		Font fnt1 = new Font("arial", Font.BOLD, 30);

		g.setColor(Color.WHITE);

		// g.setFont(fnt0);
		g.setFont(fnt1);

		// draw background image
		g2d.drawImage(backgroundimg, 0, 0, backgroundimg.getWidth(),
				backgroundimg.getHeight(), this);

		// draw the logo
		g2d.drawImage(logo, (game.getWidth() / 2) - (logo.getWidth() / 2), 10,
				logo.getWidth(), logo.getHeight(), this);

		DrawOutline("Loading...", (game.getWidth() / 2) - 60, 500, g2d);

		g.setColor(Color.BLACK);

		g2d.drawRect(249, 549, 302, 32);
		g2d.drawRect(250, 550, 300, 30);

		g.setColor(Color.BLUE);

		g2d.fillRect(251, 551, (loadPercent * 3) / 2, 29);
	}

	public void tick() {
		loadCount++;
		loadPercent++;
		if (loadCount > waitCount) {
			gameLoading = false;
		}
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

	public boolean isGameLoading() {
		return gameLoading;
	}

	public void setGameLoading(boolean gameLoading) {
		this.gameLoading = gameLoading;
	}

}

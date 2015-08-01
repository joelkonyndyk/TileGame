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

public class OptionsMenu extends JPanel {

	private Game game;

	private BufferedImage logo = null;
	private BufferedImage backimg = null;
	private BufferedImage backgroundimg = null;

	private Rectangle backButton;

	public OptionsMenu(Game game) {
		this.game = game;

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/logoA.png");
			backimg = loader.loadImage("/exitbutton.png");
			backgroundimg = loader.loadImage("/screenShot.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		backButton = new Rectangle((game.getWidth() / 2)
				- (backimg.getWidth() / 2), 500, backimg.getWidth(),
				backimg.getHeight());
	}

	public void init() {

	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		// Font fnt0 = new Font("arial", Font.BOLD, 50);
		// Font fnt1 = new Font("arial", Font.BOLD, 30);

		// g.setFont(fnt0);
		// g.setFont(fnt1);
		//
		// g.setColor(Color.WHITE);

		// draw background image
		g2d.drawImage(backgroundimg, 0, 0, backgroundimg.getWidth(),
				backgroundimg.getHeight(), this);

		g2d.setColor(Color.BLACK);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f));
		g2d.fillRect(0, 0, game.getWidth(), game.getHeight());

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		// draw the logo
		g2d.drawImage(logo, (game.getWidth() / 2) - (logo.getWidth() / 2), 10,
				logo.getWidth(), logo.getHeight(), this);

		// draw the buttons
		g2d.drawImage(backimg,
				(game.getWidth() / 2) - (backimg.getWidth() / 2), 500,
				backimg.getWidth(), backimg.getHeight(), this);

	}

	public void tick() {

	}

	public Rectangle getBackButton() {
		return backButton;
	}

}

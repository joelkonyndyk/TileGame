package package1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class NewGame extends JPanel {

	// add buttons for each of the game modes
	// possibly add an image next to each of the selections depicting the game
	// mode
	// add a cancel or back button

	private BufferedImage logo = null;

	private BufferedImage classicimg = null;
	private BufferedImage gravityimg = null;
	private BufferedImage criticalmassimg = null;
	private BufferedImage infinityimg = null;
	private BufferedImage backimg = null;

	private BufferedImage backgroundimg = null;

	private int locationStart = 175;
	private int location = locationStart;
	private int spacing = 75;

	private Rectangle classicButton;
	private Rectangle gravityButton;
	private Rectangle criticalmassButton;
	private Rectangle infinityButton;
	private Rectangle backButton;

	private Game game;

	public NewGame(Game game) {
		this.game = game;

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/logoA.png");
			classicimg = loader.loadImage("/classicbutton.png");
			gravityimg = loader.loadImage("/gravitybutton.png");
			criticalmassimg = loader.loadImage("/criticalmassbutton.png");
			infinityimg = loader.loadImage("/infinitybutton.png");
			backimg = loader.loadImage("/backbutton.png");

			backgroundimg = loader.loadImage("/screenShot.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// backButton = new Rectangle((game.getWidth() / 2)
		// - (backimg.getWidth() / 2), location += spacing,
		// backimg.getWidth(), backimg.getHeight());

		backButton = new Rectangle(10, 10, backimg.getWidth(),
				backimg.getHeight());

		classicButton = new Rectangle((game.getWidth() / 2)
				- (classicimg.getWidth() / 2), location, classicimg.getWidth(),
				classicimg.getHeight());
		gravityButton = new Rectangle((game.getWidth() / 2)
				- (gravityimg.getWidth() / 2), location += spacing,
				gravityimg.getWidth(), gravityimg.getHeight());
		criticalmassButton = new Rectangle((game.getWidth() / 2)
				- (criticalmassimg.getWidth() / 2), location += spacing,
				criticalmassimg.getWidth(), criticalmassimg.getHeight());
		infinityButton = new Rectangle((game.getWidth() / 2)
				- (infinityimg.getWidth() / 2), location += spacing,
				infinityimg.getWidth(), infinityimg.getHeight());

	}

	public void init() {

	}

	public void render(Graphics g) {

		int location = locationStart;

		Graphics2D g2d = (Graphics2D) g;

		updateBackground();

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
		// g2d.drawImage(logo, (game.getWidth() / 2) - (logo.getWidth() / 2),
		// 10,
		// logo.getWidth(), logo.getHeight(), this);

		// draw the back button
		g2d.drawImage(backimg, 10, 10, backimg.getWidth(), backimg.getHeight(),
				this);

		// draw the buttons
		g2d.drawImage(classicimg,
				(game.getWidth() / 2) - (classicimg.getWidth() / 2), location,
				classicimg.getWidth(), classicimg.getHeight(), this);
		g2d.drawImage(gravityimg,
				(game.getWidth() / 2) - (gravityimg.getWidth() / 2),
				location += spacing, gravityimg.getWidth(),
				gravityimg.getHeight(), this);
		g2d.drawImage(criticalmassimg,
				(game.getWidth() / 2) - (criticalmassimg.getWidth() / 2),
				location += spacing, criticalmassimg.getWidth(),
				criticalmassimg.getHeight(), this);
		g2d.drawImage(infinityimg,
				(game.getWidth() / 2) - (infinityimg.getWidth() / 2),
				location += spacing, infinityimg.getWidth(),
				infinityimg.getHeight(), this);

	}

	public void updateBackground() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			backgroundimg = loader.loadImage("/screenShot.png");
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public Rectangle getClassicButton() {
		return classicButton;
	}

	public Rectangle getGravityButton() {
		return gravityButton;
	}

	public Rectangle getCriticalMassButton() {
		return criticalmassButton;
	}

	public Rectangle getInfinityButton() {
		return infinityButton;
	}

	public Rectangle getBackButton() {
		return backButton;
	}

}

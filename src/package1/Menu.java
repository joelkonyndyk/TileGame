package package1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.swing.JPanel;

public class Menu extends JPanel {

	private BufferedImage logo = null;
	private BufferedImage continueimg = null;
	private BufferedImage newgameimg = null;
	private BufferedImage optionsimg = null;
	private BufferedImage scoresimg = null;
	private BufferedImage exitimg = null;

	private BufferedImage backgroundimg = null;

	private Game game;

	private int locationStart = 175;
	private int location = locationStart;
	private int spacing = 75;

	private Rectangle continueButton;
	private Rectangle newgameButton;
	private Rectangle optionsButton;
	private Rectangle scoresButton;
	private Rectangle quitButton;

	public Menu(Game game) {
		this.game = game;

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/logoA.png");
			continueimg = loader.loadImage("/continuebutton.png");
			newgameimg = loader.loadImage("/newgamebutton.png");
			optionsimg = loader.loadImage("/optionsbutton.png");
			scoresimg = loader.loadImage("/scoresbutton.png");
			exitimg = loader.loadImage("/exitbutton.png");

			backgroundimg = loader.loadImage("/screenShot.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		continueButton = new Rectangle((game.getWidth() / 2)
				- (continueimg.getWidth() / 2), location,
				continueimg.getWidth(), continueimg.getHeight());
		newgameButton = new Rectangle((game.getWidth() / 2)
				- (newgameimg.getWidth() / 2), location += spacing,
				newgameimg.getWidth(), newgameimg.getHeight());
		optionsButton = new Rectangle((game.getWidth() / 2)
				- (optionsimg.getWidth() / 2), location += spacing,
				optionsimg.getWidth(), optionsimg.getHeight());
		scoresButton = new Rectangle((game.getWidth() / 2)
				- (scoresimg.getWidth() / 2), location += spacing,
				scoresimg.getWidth(), scoresimg.getHeight());
		quitButton = new Rectangle((game.getWidth() / 2)
				- (exitimg.getWidth() / 2), location += spacing,
				exitimg.getWidth(), exitimg.getHeight());
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
		g2d.drawImage(logo, (game.getWidth() / 2) - (logo.getWidth() / 2), 10,
				logo.getWidth(), logo.getHeight(), this);

		// draw the buttons
		g2d.drawImage(continueimg,
				(game.getWidth() / 2) - (continueimg.getWidth() / 2), location,
				continueimg.getWidth(), continueimg.getHeight(), this);
		g2d.drawImage(newgameimg,
				(game.getWidth() / 2) - (newgameimg.getWidth() / 2),
				location += spacing, newgameimg.getWidth(),
				newgameimg.getHeight(), this);
		g2d.drawImage(optionsimg,
				(game.getWidth() / 2) - (optionsimg.getWidth() / 2),
				location += spacing, optionsimg.getWidth(),
				optionsimg.getHeight(), this);
		g2d.drawImage(scoresimg, (game.getWidth() / 2)
				- (scoresimg.getWidth() / 2), location += spacing,
				scoresimg.getWidth(), scoresimg.getHeight(), this);
		g2d.drawImage(exitimg,
				(game.getWidth() / 2) - (exitimg.getWidth() / 2),
				location += spacing, exitimg.getWidth(), exitimg.getHeight(),
				this);

		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		// g.drawString("TILE GAME", Game.WIDTH / 2, 100);

		Font fnt1 = new Font("arial", Font.BOLD, 30);
		g.setFont(fnt1);
		// g.drawString("Continue", continueButton.x + 19, continueButton.y +
		// 30);
		// g2d.draw(continueButton);
		// g.drawString("New Game", newgameButton.x + 19, newgameButton.y + 30);
		// g2d.draw(newgameButton);
		// g.drawString("Options", optionsButton.x + 19, optionsButton.y + 30);
		// g2d.draw(optionsButton);
		// g.drawString("Scores", scoresButton.x + 19, scoresButton.y + 30);
		// g2d.draw(scoresButton);
		// g.drawString("Exit", quitButton.x + 19, quitButton.y + 30);
		// g2d.draw(quitButton);
	}

	public void tick() {
		updateBackground();
	}

	public void updateBackground() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			backgroundimg = loader.loadImage("/screenShot.png");
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public Rectangle getContinueButton() {
		return continueButton;
	}

	public Rectangle getNewgameButton() {
		return newgameButton;
	}

	public Rectangle getOptionsButton() {
		return optionsButton;
	}

	public Rectangle getScoresButton() {
		return scoresButton;
	}

	public Rectangle getQuitButton() {
		return quitButton;
	}

}

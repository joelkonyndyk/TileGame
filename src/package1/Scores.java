package package1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JPanel;

public class Scores extends JPanel {

	private BufferedImage logo = null;

	private Game game;
	private GamePanel gamePanel;

	private BufferedImage backimg = null;
	private BufferedImage resetimg = null;

	private Rectangle backButton;
	private Rectangle resetButton;

	// private Rectangle backgroundRect;

	private int classicHighScore;
	private int gravityHighScore;
	private int critMassHighScore;
	private int infinityHighScore;

	public Scores(Game game, GamePanel gamePanel) {
		this.game = game;
		this.gamePanel = gamePanel;

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/logoA.png");
			resetimg = loader.loadImage("/resetbutton.png");
			backimg = loader.loadImage("/backbutton.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		backButton = new Rectangle(10, 10, backimg.getWidth(),
				backimg.getHeight());

		resetButton = new Rectangle((game.getWidth() / 2)
				- (resetimg.getWidth() / 2), 500, resetimg.getWidth(),
				resetimg.getHeight());
	}

	public void init() {

	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		Font fnt1 = new Font("arial", Font.BOLD, 40);
		Font fnt2 = new Font("arial", Font.PLAIN, 24);

		// draw the logo
		g2d.drawImage(logo, (game.getWidth() / 2) - (logo.getWidth() / 2), 10,
				logo.getWidth(), logo.getHeight(), this);

		// Color and Draw Background Rectangle
		g2d.setColor(new Color(33, 51, 15));
		g2d.fillRect(5, logo.getMinY() + logo.getHeight() + 20,
				game.getWidth() - 10, 510);

		// draw the buttons
		g2d.drawImage(resetimg, (game.getWidth() / 2)
				- (resetimg.getWidth() / 2), 500, resetimg.getWidth(),
				resetimg.getHeight(), this);

		// draw the back button
		g2d.drawImage(backimg, 10, 10, backimg.getWidth(), backimg.getHeight(),
				this);

		g2d.setColor(Color.WHITE);

		g2d.setFont(fnt1);
		DrawOutline("High Scores!", 290, 225, g2d);	

		g2d.setFont(fnt2);

		loadGame();

		// Score Labels
		DrawOutline("Classic: ", 150, 300, g2d);
		DrawOutline("Gravity: ", 150, 350, g2d);
		DrawOutline("Critical Mass: ", 150, 400, g2d);
		DrawOutline("Infinity: ", 150, 450, g2d);

		// Score Numbers
		DrawOutline(
				NumberFormat.getNumberInstance(Locale.US).format(
						classicHighScore), 325, 300, g2d);
		DrawOutline(
				NumberFormat.getNumberInstance(Locale.US).format(
						gravityHighScore), 325, 350, g2d);
		DrawOutline(
				NumberFormat.getNumberInstance(Locale.US).format(
						critMassHighScore), 325, 400, g2d);
		DrawOutline(
				NumberFormat.getNumberInstance(Locale.US).format(
						infinityHighScore), 325, 450, g2d);		

	}

	public void resetGameScores() {

		BufferedWriter writer = null;

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File scores = new File(classLoader.getResource("scores.txt").getFile());

		try {
			writer = new BufferedWriter(new FileWriter(scores));

			writer.write("BonusPoints:" + gamePanel.getBonusPoints());
			writer.newLine();
			writer.write("Classic:" + 0);
			writer.newLine();
			writer.write("Gravity:" + 0);
			writer.newLine();
			writer.write("CriticalMass:" + 0);
			writer.newLine();
			writer.write("Infinity:" + 0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
		gamePanel.resetScores();
	}

	public void loadGame() {

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File scores = new File(classLoader.getResource("scores.txt").getFile());

		try (BufferedReader br = new BufferedReader(new FileReader(scores))) {
			// StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {

				// System.out.println(line.substring(0, 11));
				// System.out.println(line);

				if (line.substring(0, 7).equals("Classic")) {
					classicHighScore = Integer.parseInt(line.substring(8));
				} else if (line.substring(0, 7).equals("Gravity")) {
					gravityHighScore = Integer.parseInt(line.substring(8));
				} else if (line.substring(0, 8).equals("Infinity")) {
					infinityHighScore = Integer.parseInt(line.substring(9));
				}
				// else if (line.substring(0, 11).equals("BonusPoints")) {
				// bonusPoints = Integer.parseInt(line.substring(12));
				// // bonusPtStr = Integer.toString(bonusPoints);
				// }
				else if (line.substring(0, 12).equals("CriticalMass")) {
					critMassHighScore = Integer.parseInt(line.substring(13));
				}

				// sb.append(line);
				// sb.append(System.lineSeparator());
				line = br.readLine();
			}
			// String everything = sb.toString();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public Rectangle getResetButton() {
		return resetButton;
	}

	public Rectangle getBackButton() {
		return backButton;
	}

}

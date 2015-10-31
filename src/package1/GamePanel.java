package package1;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	/** This represents the universal version identifier of ConnectFourPanel */
	private static final long serialVersionUID = 12345678910L;

	/** This represents a grid of JLabels over the board */
	private JLabel[][] board;

	private Tile[][] tiles;
	private Tile[] tileRow;

	private int[][] prevBoard;
	private int[][] tempBoard;

	private Color[] colors = new Color[] { Color.WHITE, Color.YELLOW,
			Color.ORANGE, Color.RED };
	private Color tempColor;
	private int colorIndex = 0;

	public LinkedList<EntityB> eb;

	private int BDSIZE;

	private int BDWIDTH;
	private int BDHEIGHT;

	private Point pointClicked;
	private boolean mouseClicked = false;

	private Point clickPoint;

	private Point actualClickPoint;

	private int screenX = 0;
	private int screenY = 0;

	private long timer;
	private final int delay = 250;

	private final int tilesize = 32;

	private final int classic = 1;
	private final int gravity = 2;
	private final int criticalMass = 3;
	private final int infinity = 4;

	private int gameMode = 0;

	private String gmStr = "";

	private final int small = 1;
	private final int normal = 2;
	private final int large = 3;

	private int boardSize = 1;

	private boolean droptile;

	private boolean showScoreStr;

	private boolean displayGame = false;
	private boolean Menu = true;

	// private boolean displayClearBoard = true;

	private boolean useBonusPoints = false;
	private int bonusPointsUsed;
	private int bpUsedDisplayCount = 0;

	private Rectangle gameRect;

	private Rectangle NGButton;

	private int spacing = 3;

	private int scoredisplaycount = 0;

	/** This represents the menu item quit */
	private JMenuItem quitItem;

	/** This represents the menu item new game */
	private JMenuItem newGameItem;

	private GameState state;

	private TileGame tileGame;

	private JFrame menuFrame;

	private JPanel menuPanel1;
	private JButton b1;

	private JPanel menuPanel;
	private JPanel scorePanel;
	private JPanel gameGrid;

	private JLabel gameModeLabel;
	private JLabel scoreLabel;
	private JLabel bonusPtLabel;
	private JLabel directionLabel;

	private JLabel menuLabel;
	private JLabel gmLabel;
	private JLabel bdSizeLabel;

	private JButton undoButton;

	private JButton directionUP;
	private JButton directionDOWN;
	private JButton directionLEFT;
	private JButton directionRIGHT;

	// private JButton menuButton;

	private JButton continueButton;
	private JButton newGameButton;
	private JButton optionsButton;
	private JButton quitButton;

	private JButton smallButton;
	private JButton normalButton;
	private JButton largeButton;

	private JButton classicButton;
	private JButton gravityButton;
	private JButton criticalMassButton;
	private JButton infinityButton;

	private Textures tex;
	private Game game;
	private Controller c;

	public LinkedList<EntityA> ea;

	private BufferedImage logo = null;
	private BufferedImage menuButton = null;
	private BufferedImage undoButtonImg = null;
	private BufferedImage nextGameButton = null;
	private BufferedImage background = null;
	private BufferedImage classicBackground = null;

	private BufferedImage swapButton = null;
	private BufferedImage dropLayerButton = null;
	private BufferedImage fillBoardButton = null;
	private BufferedImage breakCritMassButton = null;

	private BufferedImage screenShot = null;

	private Robot robot;

	private int moveCount = 0;

	private Rectangle swapRectangle;
	private Rectangle dropRectangle;
	private Rectangle fillboardRectangle;
	private Rectangle breakCritMassRectangle;

	private Rectangle upRectangle;
	private Rectangle downRectangle;
	private Rectangle leftRectangle;
	private Rectangle rightRectangle;

	private Rectangle undoRectangle;

	private boolean displayUndoButton = false;

	private boolean showSwapDrop = false;
	private boolean showFillBoard = false;
	private boolean showBreakCM = false;

	private boolean gameIsOver = false;

	private boolean showMovingText = false;

	private boolean undoPts = false;

	private boolean showSwapInfo = false;
	private boolean showDropRowInfo = false;
	private boolean showFillBoardInfo = false;
	private boolean showBreakCMInfo = false;

	private boolean upInfo = false;
	private boolean downInfo = false;
	private boolean leftInfo = false;
	private boolean rightInfo = false;

	// private int timeLeft;

	private int movingText = 0;

	private int clickPt = 0;

	private Timer gameOverTimer;

	private float tempTime = 1.0f;

	Animation anim;

	private Point mouseLocation;

	private StopWatch time;
	private StopWatch undoTime;
	private StopWatch mouseTimer;
	private StopWatch directionTimer;

	// Edit Game Variables
	private boolean editGame = false;
	private Tile[] editRow;
	private Tile currTile;
	private int editTileVal = 0;

	public GamePanel(Textures tex, Game game, Controller c) {

		this.tex = tex;
		this.game = game;
		this.c = c;

		// ea = c.getEntityA();
		eb = c.getEntityB();

		tileGame = new TileGame(boardSize, gameMode);
		// BDSIZE = tileGame.getBDSIZE();
		BDWIDTH = tileGame.getBDWIDTH();
		BDHEIGHT = tileGame.getBDHEIGHT();

		// board = new JLabel[BDSIZE][BDSIZE];
		prevBoard = new int[BDHEIGHT][BDWIDTH];
		tiles = new Tile[BDHEIGHT][BDWIDTH];
		tileRow = new Tile[BDWIDTH];
		editRow = new Tile[5];

		tempBoard = new int[BDHEIGHT][BDWIDTH];

		gameRect = new Rectangle(2, 182, 805, 485);

		time = new StopWatch();
		undoTime = new StopWatch();
		mouseTimer = new StopWatch();
		directionTimer = new StopWatch();
		time.start();
		undoTime.start();
		mouseTimer.start();
		directionTimer.start();

	}

	public void init() {

		// displayClearBoard = true;
		tileGame.setDisplayClearBoard(true);

		// timeLeft = tileGame.getTimeLeft();

		Runnable takeSecond = new Runnable() {
			public void run() {
				if (tileGame.GameOver()) {
					tileGame.setTimeLeft(tileGame.getTimeLeft() - 1);
				} else {
					tileGame.setTimeLeft(5);
				}
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(takeSecond, 0, 1, TimeUnit.SECONDS);

		try {
			robot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			logo = loader.loadImage("/logoA.png");
			menuButton = loader.loadImage("/menubutton.png");
			undoButtonImg = loader.loadImage("/undobutton.png");
			nextGameButton = loader.loadImage("/newgamebutton.png");
			background = loader.loadImage("/gameBackground.png");
			classicBackground = loader.loadImage("/gameBackgroundClassic.png");

			swapButton = loader.loadImage("/swapbutton.png");
			dropLayerButton = loader.loadImage("/droprowbutton.png");
			fillBoardButton = loader.loadImage("/fillboardbutton.png");
			breakCritMassButton = loader.loadImage("/breakCritMass.png");

		} catch (IOException e) {
			e.printStackTrace();
		}

		NGButton = new Rectangle((game.getWidth() / 2)
				- (nextGameButton.getWidth() / 2), 500,
				nextGameButton.getWidth(), nextGameButton.getHeight());

		undoRectangle = new Rectangle(menuButton.getWidth() + 20, 10,
				undoButtonImg.getWidth(), undoButtonImg.getHeight());

		swapRectangle = new Rectangle(25, 90, swapButton.getWidth(),
				swapButton.getHeight());
		dropRectangle = new Rectangle(100, 90, dropLayerButton.getWidth(),
				dropLayerButton.getHeight());
		fillboardRectangle = new Rectangle(175, 90, fillBoardButton.getWidth(),
				fillBoardButton.getHeight());

		breakCritMassRectangle = new Rectangle(25, 90,
				breakCritMassButton.getWidth(), breakCritMassButton.getHeight());

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {

				int temp = tileGame.getBoardVal(x, y);

				if (temp == 1) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							1, 1, tex, game, c);

				} else if (temp == 2) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							2, 1, tex, game, c);

				} else if (temp == 3) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							3, 1, tex, game, c);

				} else if (temp == 4) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							4, 1, tex, game, c);

				} else if (temp == 5) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							5, 1, tex, game, c);

				}
			}
		}
		updateTileRow();

		// initializes the edit row of tiles
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				editRow[i] = new Tile(85, (i * tilesize) + 300, 7, 1, tex,
						game, c);
				currTile = new Tile(10, 575, 7, 1, tex, game, c);
			} else {
				editRow[i] = new Tile(85, (i * tilesize) + 300, i, 1, tex,
						game, c);
			}
		}

		upRectangle = new Rectangle(game.getWidth() - 120, 65 - 32, 32, 32);
		downRectangle = new Rectangle(game.getWidth() - 120, 65 + 32, 32, 32);
		leftRectangle = new Rectangle(game.getWidth() - 120 - 32, 65, 32, 32);
		rightRectangle = new Rectangle(game.getWidth() - 120 + 32, 65, 32, 32);
	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		if (tileGame.getGameMode() != classic) {
			g2d.drawImage(background, 0, 0, background.getWidth(),
					background.getHeight(), this);
		} else {
			g2d.drawImage(classicBackground, 0, 0,
					classicBackground.getWidth(),
					classicBackground.getHeight(), this);

			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.4f));

			if (showSwapDrop) {

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f));

				g2d.drawImage(swapButton, 25, 90, swapButton.getWidth(),
						swapButton.getHeight(), this);
				g2d.drawImage(dropLayerButton, 100, 90,
						dropLayerButton.getWidth(),
						dropLayerButton.getHeight(), this);
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.4f));
			} else {
				g2d.drawImage(swapButton, 25, 90, swapButton.getWidth(),
						swapButton.getHeight(), this);
				g2d.drawImage(dropLayerButton, 100, 90,
						dropLayerButton.getWidth(),
						dropLayerButton.getHeight(), this);
			}
			if (showFillBoard) {
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f));
				g2d.drawImage(fillBoardButton, 175, 90,
						fillBoardButton.getWidth(),
						fillBoardButton.getHeight(), this);

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.4f));
			} else {
				g2d.drawImage(fillBoardButton, 175, 90,
						fillBoardButton.getWidth(),
						fillBoardButton.getHeight(), this);
			}

		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.8f));

		// draws the top row that is about to drop
		if (tileGame.getGameMode() == classic && tileGame.getDisplayRow()) {
			updateTileRow();
			for (int i = 0; i < BDWIDTH; i++) {
				if (tileGame.getBoardRowVal(i) != 0) {
					tileRow[i].render(g2d);
				}
			}
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		Font fnt0 = new Font("arial", Font.BOLD, 16);
		g2d.setFont(fnt0);
		g2d.setColor(Color.WHITE);

		if (tileGame.getGameMode() == 1) {
			gmStr = "Game Mode: Classic";

		} else if (tileGame.getGameMode() == 2) {
			gmStr = "Game Mode: Gravity";

		} else if (tileGame.getGameMode() == 3) {
			gmStr = "Game Mode: Critical Mass";

		} else if (tileGame.getGameMode() == 4) {
			gmStr = "Game Mode: Infinity";

		} else {
			gmStr = "Game Mode: Classic";

		}

		// Draws the game mode string
		DrawOutline(gmStr, 10, 65, g2d);

		// draws shaded rectangles behind score
		g2d.setColor(Color.BLACK);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));

		g2d.fillRect(225, 10, 325, 32);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		DrawOutline(
				"Score: "
						+ NumberFormat.getNumberInstance(Locale.US).format(
								tileGame.getScore()), 235, 32, g2d);
		DrawOutline(
				"Bonus Points: "
						+ NumberFormat.getNumberInstance(Locale.US).format(
								tileGame.getBonusPoints()), 370, 32, g2d);

		Font fnt1 = new Font("arial", Font.BOLD, 20);
		g2d.setFont(fnt1);

		DrawOutline("Direction", game.getWidth() - 145, 25, g2d);

		if (tileGame.getGameMode() == classic) {
			g2d.drawImage(tex.centerTile[0], game.getWidth() - 120, 65, 32, 32,
					this);

		} else {
			g2d.drawImage(tex.centerTile[1], game.getWidth() - 120, 65, 32, 32,
					this);
		}

		// down
		if (tileGame.getDirection() == 1) {
			g2d.drawImage(tex.downTile[1], game.getWidth() - 120, 65 + 32, 32,
					32, this);
		} else {
			g2d.drawImage(tex.downTile[0], game.getWidth() - 120, 65 + 32, 32,
					32, this);
		}
		// up
		if (tileGame.getDirection() == 2) {
			g2d.drawImage(tex.upTile[1], game.getWidth() - 120, 65 - 32, 32,
					32, this);
		} else {
			g2d.drawImage(tex.upTile[0], game.getWidth() - 120, 65 - 32, 32,
					32, this);
		}
		// left
		if (tileGame.getDirection() == 3) {
			g2d.drawImage(tex.leftTile[1], game.getWidth() - 120 - 32, 65, 32,
					32, this);
		} else {
			g2d.drawImage(tex.leftTile[0], game.getWidth() - 120 - 32, 65, 32,
					32, this);
		}
		// right
		if (tileGame.getDirection() == 4) {
			g2d.drawImage(tex.rightTile[1], game.getWidth() - 120 + 32, 65, 32,
					32, this);
		} else {
			g2d.drawImage(tex.rightTile[0], game.getWidth() - 120 + 32, 65, 32,
					32, this);
		}

		g2d.setColor(Color.DARK_GRAY);

		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(3));

		g2d.draw(gameRect);

		g2d.setStroke(oldStroke);

		// draws in the tiles on the game board
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (tileGame.getBoardVal(x, y) != 0) {
					tiles[x][y].render(g2d);
				}
			}
		}

		g2d.setFont(fnt0);

		// Delayed button information

		if (!upInfo && !downInfo && !leftInfo && !rightInfo) {
			directionTimer.reset();
		}
		if (upInfo) {
			if (directionTimer.getElapsedTime() > 1000) {
				DrawOutline("Up", mouseLocation.x, mouseLocation.y + 32, g2d);
			}
		}
		if (downInfo) {
			if (directionTimer.getElapsedTime() > 1000) {
				DrawOutline("Down", mouseLocation.x, mouseLocation.y + 32, g2d);
			}
		}
		if (leftInfo) {
			if (directionTimer.getElapsedTime() > 1000) {
				DrawOutline("Left", mouseLocation.x, mouseLocation.y + 32, g2d);
			}
		}
		if (rightInfo) {
			if (directionTimer.getElapsedTime() > 1000) {
				DrawOutline("Right", mouseLocation.x, mouseLocation.y + 32, g2d);
			}
		}

		if (tileGame.gameMode == classic) {

			if (!showSwapInfo && !showDropRowInfo && !showFillBoardInfo) {
				mouseTimer.reset();
			}
			if (showSwapInfo) {
				if (mouseTimer.getElapsedTime() > 1000) {
					DrawOutline("Swap Row", mouseLocation.x,
							mouseLocation.y + 32, g2d);
				}
			}
			if (showDropRowInfo) {
				if (mouseTimer.getElapsedTime() > 1000) {
					DrawOutline("Drop Row", mouseLocation.x,
							mouseLocation.y + 32, g2d);
				}
			}
			if (showFillBoardInfo) {
				if (mouseTimer.getElapsedTime() > 1000) {
					DrawOutline("Fill Board", mouseLocation.x,
							mouseLocation.y + 32, g2d);
				}
			}
		}

		if (tileGame.gameMode == criticalMass) {

			if (!showBreakCMInfo) {
				mouseTimer.reset();
			}
			if (showBreakCMInfo) {
				if (mouseTimer.getElapsedTime() > 1000) {
					DrawOutline("Smash Mass", mouseLocation.x,
							mouseLocation.y + 32, g2d);
				}
			}
		}

		// Displays prompt when undo is pressed
		if (!undoPts) {
			undoTime.reset();
			tempTime = 1.0f;
		} else {
			if (undoTime.getElapsedTime() > 500) {
				if (tempTime > .1f) {
					tempTime -= .1f;
					g2d.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, tempTime));
					DrawOutline("Undo: -25pts", 235, 60, g2d);
				} else {
					undoPts = false;
				}
			} else {
				DrawOutline("Undo: -25pts", 235, 60, g2d);
			}
		}

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		g2d.setFont(fnt1);

		if (tileGame.GameOver()) {

			Font fnt2 = new Font("arial", Font.BOLD, 75);
			Font fnt3 = new Font("arial", Font.BOLD, 28);
			Font fnt4 = new Font("arial", Font.BOLD, 60);

			g2d.setColor(Color.BLACK);

			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f));

			if (tileGame.getTimeLeft() > 0) {

				g2d.fillRect((game.getWidth() / 2) - 225,
						(game.getHeight() / 2) - 60, 450, 125);

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f));

				g2d.setFont(fnt4);
				DrawOutline("Game Over In:", 200, game.getHeight() / 2, g2d);

				g2d.setFont(fnt3);

				DrawOutline(tileGame.getTimeLeft() + " Sec!", 350,
						(game.getHeight() / 2) + 50, g2d);

			} else {

				// gameIsOver = true;
				setGameIsOver(true);

				g2d.fillRect(0, 0, game.getWidth(), game.getHeight());

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f));

				g2d.drawImage(nextGameButton, (game.getWidth() / 2)
						- (nextGameButton.getWidth() / 2), 500,
						nextGameButton.getWidth(), nextGameButton.getHeight(),
						this);

				g2d.setFont(fnt2);

				DrawOutline("Game Over!", 200, game.getHeight() / 2, g2d);

				g2d.setFont(fnt3);

				if (tileGame.getHighScore()) {

					tempColor = colors[colorIndex % 4];

					DrawOutline("New High Score!", 300,
							(game.getHeight() / 2) + 50, tempColor, g2d);
					DrawOutline(NumberFormat.getNumberInstance(Locale.US)
							.format(tileGame.getScore()), 350,
							(game.getHeight() / 2) + 90, tempColor, g2d);
				} else {
					DrawOutline(
							"Score: "
									+ NumberFormat.getNumberInstance(Locale.US)
											.format(tileGame.getScore())
									+ "pts", 300, (game.getHeight() / 2) + 75,
							g2d);

					// DrawOutline(NumberFormat.getNumberInstance(Locale.US)
					// .format(tileGame.getScore()), 350,
					// (game.getHeight() / 2) + 90, tempColor, g2d);
				}
			}
		}

		g2d.drawImage(menuButton, 10, 10, menuButton.getWidth(),
				menuButton.getHeight(), this);

		if (!displayUndoButton || tileGame.getTimeLeft() <= 0) {
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.4f));
			g2d.drawImage(undoButtonImg, menuButton.getWidth() + 20, 10,
					undoButtonImg.getWidth(), undoButtonImg.getHeight(), this);
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1f));
		} else {
			g2d.drawImage(undoButtonImg, menuButton.getWidth() + 20, 10,
					undoButtonImg.getWidth(), undoButtonImg.getHeight(), this);
		}

		if (tileGame.getGameMode() == criticalMass) {

			if (showBreakCM) {
				g2d.drawImage(breakCritMassButton, 25, 90,
						breakCritMassButton.getWidth(),
						breakCritMassButton.getHeight(), this);
			} else {
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.4f));

				g2d.drawImage(breakCritMassButton, 25, 90,
						breakCritMassButton.getWidth(),
						breakCritMassButton.getHeight(), this);

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f));
			}

			displayCritMass(g2d);
			checkCriticalMass(g2d);
		}

		if (editGame) {

			DrawOutline("EDIT GAME", 325, 70, g2d);

			for (int i = 0; i < 5; i++) {
				editRow[i].render(g2d);
			}
			currTile.render(g2d);

		}

		c.render(g);
		// c.render(g2d);

	}

	public void tick() {

		if (mouseClicked) {

			if (editGame) {

				for (int i = 0; i < 5; i++) {
					if (editRow[i].getBounds().contains(pointClicked)) {
						currTile.setImage(editRow[i].getImage());
						editTileVal = i;
					}
				}
			}

			mouseClicked = false;
		}

		if (!getUndoList().isEmpty()) {
			setDisplayUndoButton(true);
		} else {
			setDisplayUndoButton(false);
		}

		if (tileGame.getGameMode() == classic) {
			tileGame.checkMoveCount();

			if (tileGame.getDropRow()) {
				tileGame.mergeRow();
			}
			if (tileGame.getDisplayRow() && !tileGame.getDropRow()) {
				showSwapDrop = true;
			} else {
				showSwapDrop = false;
			}
			if (tileGame.tilesRemaining() < 0.3) {
				showFillBoard = true;
			} else {
				showFillBoard = false;
			}
		}

		if (tileGame.getGameMode() == classic && tileGame.getDisplayRow()) {
			for (int i = 0; i < BDWIDTH; i++) {
				tileRow[i].tick();
			}
		}

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				tiles[x][y].tick();
			}
		}

		UpdateBoard();

		if (time.getElapsedTime() > 75) {
			colorIndex++;
			time.reset();
		}

		boardCleared();
		if (!isBoardCleared()) {
			// displayClearBoard = true;
			tileGame.setDisplayClearBoard(true);
		}

		c.tick();
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

	public void DrawOutline(String str, int x, int y, Color clr, Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawString(str, x - 1, y);
		g2d.drawString(str, x + 1, y);
		g2d.drawString(str, x, y - 1);
		g2d.drawString(str, x, y + 1);

		g2d.setColor(clr);
		g2d.drawString(str, x, y);
	}

	public void setDirection(int i) {
		tileGame.setDirection(i);
	}

	public void pointClicked(Point p) {
		clickPoint = p;
		DropTiles();
	}

	public void undo() {
		tileGame.Undo();
	}

	public void UpdateBoard() {

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {

				int temp = tileGame.getBoardVal(x, y);

				if (temp == 0) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							7, 1, tex, game, c);

				} else if (temp == 1) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							1, 1, tex, game, c);

				} else if (temp == 2) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							2, 1, tex, game, c);

				} else if (temp == 3) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							3, 1, tex, game, c);

				} else if (temp == 4) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							4, 1, tex, game, c);

				} else if (temp == 5) {

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							5, 1, tex, game, c);

				}
			}
		}
	}

	public void copyBoard() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				prevBoard[x][y] = tileGame.getBoardVal(x, y);
			}
		}
	}

	public void copyTempBoard() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				tempBoard[x][y] = tileGame.getBoardVal(x, y);
			}
		}
	}

	public void DropTGTiles() {
		tileGame.DropTiles();
	}

	public void DropTiles() {

		// int clickPt = 0;

		if (clickPoint != null && !tileGame.CheckSingle(clickPoint)) {
			int count;

			if (actualClickPoint.x > game.getWidth() - 175) {
				clickPt = game.getWidth() - 175;
			} else {
				clickPt = actualClickPoint.x;
			}

			if (tileGame.getBoardVal(clickPoint.x, clickPoint.y) != 5) {
				tileGame.addMoveCount();
				copyBoard();
				tileGame.addUndo();
			}
			tileGame.checkShapes(clickPoint);

			if (tileGame.getBoardVal(clickPoint.x, clickPoint.y) != 5) {
				count = tileGame.countTiles(clickPoint);
				if (tileGame.isSquare()) {
					c.addEntity(new Text("Square!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("Square!");
					tileGame.CalculateScore(count, "square");
					tileGame.setSquare(false);
				} else if (tileGame.isLine()) {
					c.addEntity(new Text("Line!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("Line!");
					tileGame.CalculateScore(count, "line");
					tileGame.setLine(false);
				} else if (tileGame.isCorner()) {
					c.addEntity(new Text("Corner!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("Corner!");
					tileGame.CalculateScore(count, "corner");
					tileGame.setCorner(false);
				} else if (tileGame.isStairs()) {
					c.addEntity(new Text("Stairs!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("Stairs!");
					tileGame.CalculateScore(count, "stairs");
					tileGame.setStairs(false);
				} else if (tileGame.isCross()) {
					c.addEntity(new Text("Cross!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("Cross!");
					tileGame.CalculateScore(count, "cross");
					tileGame.setCross(false);
				} else if (tileGame.isL()) {
					c.addEntity(new Text("Awesome L!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("L!");
					tileGame.CalculateScore(count, "l");
					tileGame.setL(false);
				} else if (tileGame.isT()) {
					c.addEntity(new Text("Sweet T!", game, c, clickPt,
							actualClickPoint.y - 50));
					// System.out.println("T!");
					tileGame.CalculateScore(count, "t");
					tileGame.setT(false);
				} else {
					tileGame.CalculateScore(count, null);
				}
			}
			count = tileGame.CheckBoard(clickPoint);
			copyTempBoard();
			breakTiles();
			tileGame.DropTiles();
			// tileGame.boardCleared();

			if (tileGame.getBoardVal(clickPoint.x, clickPoint.y) != 5) {

				if (tileGame.getComboStr() != "") {
					c.addEntity(new Text(tileGame.getComboStr(), game, c,
							clickPt, actualClickPoint.y + 25));

				}
				if (tileGame.getBonusPtStr() != "") {
					c.addEntity(new Text(tileGame.getBonusPtStr(), game, c,
							clickPt, actualClickPoint.y - 25));
					c.addEntity(new Text(tileGame.getScoreStr(), game, c,
							clickPt, actualClickPoint.y));

				} else {
					c.addEntity(new Text(tileGame.getScoreStr(), game, c,
							actualClickPoint.x, actualClickPoint.y));
				}
			}
		}

		// System.out.println(tileGame.getBoardCleared());

		if (tileGame.GameOver()) {
			if (tileGame.isDisplayClearBoard()) {
				if (tileGame.getBoardCleared()) {
					// displayClearBoard = false;
					Text t = new Text("Board Cleared! +1000pts!", game, c,
							(game.getWidth() / 2) - 200,
							(game.getHeight() / 2) - 100);
					t.setFont(new Font("arial", Font.BOLD, 36));
					t.setSpeed(2);

					Text t1 = new Text("Bonus Points! +100!", game, c,
							(game.getWidth() / 2) - 175,
							(game.getHeight() / 2) - 50);
					t1.setFont(new Font("arial", Font.BOLD, 36));
					t1.setSpeed(2);

					c.addEntity(t);
					c.addEntity(t1);

					tileGame.setBoardCleared(false);
					// displayClearBoard = false;
					tileGame.setDisplayClearBoard(false);
				}
			}
		}

		if (tileGame.getGameMode() == criticalMass) {

		}

	}

	public void breakTiles() {

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {

				if (prevBoard[x][y] != 0 && tileGame.getBoardVal(x, y) == 0) {
					tiles[x][y].setBreakT(true);
				}

			}
		}

	}

	public void displayBPointUse(int num) {
		bonusPointsUsed = num;
		tileGame.SubtractBonusPoints(num);
		String str = "-" + num + " Bonus Points";
		// shows bonus point update on screen
		c.addEntity(new Text(str, game, c, (game.getWidth() / 2) - 50, game
				.getHeight() / 2));

	}

	public void checkCriticalMass(Graphics2D g2d) {
		tileGame.CriticalMass();
		Stack<String> CMPercentages = tileGame.getCritMassPercentages();
		String temp = "";

		for (int i = 0; i < CMPercentages.size(); i++) {
			temp = CMPercentages.get(i);
			if (tileGame.checkCriticalMass(temp)) {
				String regx = "[]";
				char[] ca = regx.toCharArray();
				for (char c : ca) {
					temp = temp.replace("" + c, ",");
				}
				String[] str = temp.split(",");

				int count = ((Integer.parseInt(str[3]) * tileGame
						.getCritMassSize()) / 100);
				int tempScore = count * 25;

				c.addEntity(new Text("CRITICAL MASS!", game, c, Integer
						.parseInt(str[2]) - 25, Integer.parseInt(str[1])));
				c.addEntity(new Text(tempScore + " Points!", game, c, Integer
						.parseInt(str[2]), Integer.parseInt(str[1]) + 30));
				// use below to display info in center of screen
				// c.addEntity(new Text("CRITICAL MASS!", game, c, (game
				// .getWidth() / 2) - 25, game.getHeight() / 2));
				// c.addEntity(new Text(tempScore + " Points!", game, c, (game
				// .getWidth() / 2), (game.getHeight() / 2) + 30));
			}

		}

	}

	public void displayCritMass(Graphics2D g2d) {

		Font fnt1 = new Font("arial", Font.BOLD, 20);
		g2d.setFont(fnt1);

		tileGame.CriticalMass();

		Stack<String> CMPercentages = tileGame.getCritMassPercentages();
		String temp = "";

		for (int i = 0; i < CMPercentages.size(); i++) {
			temp = CMPercentages.get(i);
			String regx = "[]";
			char[] ca = regx.toCharArray();
			for (char c : ca) {
				temp = temp.replace("" + c, ",");
			}
			String[] str = temp.split(",");

			int x = Integer.parseInt(str[2]);
			int y = Integer.parseInt(str[1]);

			DrawOutline(str[3] + "%", x, y + 16, g2d);

			if (Integer.parseInt(str[3]) > 50) {
				showBreakCM = true;
			}
		}

	}

	public void removeLargestCritMass() {

		int num = 0;
		int tempX = 0;
		int tempY = 0;

		tileGame.CriticalMass();
		Stack<String> CMPercentages = tileGame.getCritMassPercentages();
		String temp = "";

		for (int i = 0; i < CMPercentages.size(); i++) {
			temp = CMPercentages.get(i);

			String regx = "[]";
			char[] ca = regx.toCharArray();
			for (char c : ca) {
				temp = temp.replace("" + c, ",");
			}
			String[] str = temp.split(",");

			if (Integer.parseInt(str[3]) > num) {
				num = Integer.parseInt(str[3]);
				tempX = Integer.parseInt(str[4]);
				tempY = Integer.parseInt(str[5]);
			}
		}

		tileGame.removeJoiningTiles(new Point(tempX, tempY), 5);
	}

	public void mergeRow() {
		tileGame.mergeRow();
	}

	public void startTimer() {
		timer = System.currentTimeMillis();
	}

	public double elapsedTime() {
		long now = System.currentTimeMillis();
		return (now - timer);
	}

	public void resetScores() {
		tileGame.resetGameScores();
	}

	public void captureScreen(int screenX, int screenY) {

		Rectangle screenRectangle = new Rectangle(screenX, screenY,
				game.getWidth(), game.getHeight());
		try {
			BufferedImage image = robot.createScreenCapture(screenRectangle);
			ImageIO.write(image, "png", new File("res/screenShot.png"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTileRow() {

		for (int i = 0; i < BDWIDTH; i++) {
			int temp = tileGame.getBoardRowVal(i);

			int tempX = (gameRect.y + spacing - tilesize - 10);

			if (temp == 1) {

				tileRow[i] = new Tile(tempX, (i * tilesize) + gameRect.x
						+ spacing, 1, 1, tex, game, c);

			} else if (temp == 2) {

				tileRow[i] = new Tile(tempX, (i * tilesize) + gameRect.x
						+ spacing, 2, 1, tex, game, c);

			} else if (temp == 3) {

				tileRow[i] = new Tile(tempX, (i * tilesize) + gameRect.x
						+ spacing, 3, 1, tex, game, c);

			} else if (temp == 4) {

				tileRow[i] = new Tile(tempX, (i * tilesize) + gameRect.x
						+ spacing, 4, 1, tex, game, c);

			} else if (temp == 5) {

				tileRow[i] = new Tile(tempX, (i * tilesize) + gameRect.x
						+ spacing, 5, 1, tex, game, c);

			}
		}
	}

	public void setBoardVal(int x, int y) {
		tileGame.setBoardVal(x, y, editTileVal);
	}

	public boolean EditingBoard() {
		return editGame;
	}

	public void mouseClicked(Point p) {
		mouseClicked = true;
		pointClicked = p;
	}

	public boolean isDisplayClearBoard() {
		return tileGame.isDisplayClearBoard();
	}

	public void setDisplayClearBoard(boolean displayClearBoard) {
		tileGame.setDisplayClearBoard(displayClearBoard);
	}

	public int getTimeLeft() {
		return tileGame.getTimeLeft();
	}

	public Rectangle getUndoRectangle() {
		return undoRectangle;
	}

	public void setUndoRectangle(Rectangle undoRectangle) {
		this.undoRectangle = undoRectangle;
	}

	public boolean isDisplayUndoButton() {
		return displayUndoButton;
	}

	public void setDisplayUndoButton(boolean displayUndoButton) {
		this.displayUndoButton = displayUndoButton;
	}

	public Stack<String> getUndoList() {
		return tileGame.getUndoList();
	}

	public Rectangle getUpRectangle() {
		return upRectangle;
	}

	public void setUpRectangle(Rectangle upRectangle) {
		this.upRectangle = upRectangle;
	}

	public Rectangle getDownRectangle() {
		return downRectangle;
	}

	public void setDownRectangle(Rectangle downRectangle) {
		this.downRectangle = downRectangle;
	}

	public Rectangle getLeftRectangle() {
		return leftRectangle;
	}

	public void setLeftRectangle(Rectangle leftRectangle) {
		this.leftRectangle = leftRectangle;
	}

	public Rectangle getRightRectangle() {
		return rightRectangle;
	}

	public void setRightRectangle(Rectangle rightRectangle) {
		this.rightRectangle = rightRectangle;
	}

	public boolean isUpInfo() {
		return upInfo;
	}

	public void setUpInfo(boolean upInfo) {
		this.upInfo = upInfo;
	}

	public boolean isDownInfo() {
		return downInfo;
	}

	public void setDownInfo(boolean downInfo) {
		this.downInfo = downInfo;
	}

	public boolean isLeftInfo() {
		return leftInfo;
	}

	public void setLeftInfo(boolean leftInfo) {
		this.leftInfo = leftInfo;
	}

	public boolean isRightInfo() {
		return rightInfo;
	}

	public void setRightInfo(boolean rightInfo) {
		this.rightInfo = rightInfo;
	}

	public boolean isShowBreakCMInfo() {
		return showBreakCMInfo;
	}

	public void setShowBreakCMInfo(boolean showBreakCMInfo) {
		this.showBreakCMInfo = showBreakCMInfo;
	}

	public Point getMouseLocation() {
		return mouseLocation;
	}

	public void setMouseLocation(Point mouseLocation) {
		this.mouseLocation = mouseLocation;
	}

	public boolean isShowSwapInfo() {
		return showSwapInfo;
	}

	public void setShowSwapInfo(boolean showSwapInfo) {
		this.showSwapInfo = showSwapInfo;
	}

	public boolean isShowDropRowInfo() {
		return showDropRowInfo;
	}

	public void setShowDropRowInfo(boolean showDropRowInfo) {
		this.showDropRowInfo = showDropRowInfo;
	}

	public boolean isShowFillBoardInfo() {
		return showFillBoardInfo;
	}

	public void setShowFillBoardInfo(boolean showFillBoardInfo) {
		this.showFillBoardInfo = showFillBoardInfo;
	}

	public boolean isUndoPts() {
		return undoPts;
	}

	public void setUndoPts(boolean undoPts) {
		this.undoPts = undoPts;
	}

	public void checkCritMass() {
		tileGame.CriticalMass();
	}

	public void fillBoard() {
		tileGame.fillBoard();
	}

	public boolean isBoardCleared() {
		return tileGame.getBoardCleared();
	}

	public void boardCleared() {
		tileGame.boardCleared();
	}

	public boolean getDropRow() {
		return tileGame.getDropRow();
	}

	public void setDropRow(boolean b) {
		tileGame.setDropRow(b);
	}

	public boolean getDisplayRow() {
		return tileGame.getDisplayRow();
	}

	public boolean gameIsOver() {
		return tileGame.isGameOver();
	}

	public void setGameIsOver(boolean gameIsOver) {
		tileGame.setGameOver(gameIsOver);
	}

	public Rectangle getSwapRectangle() {
		return swapRectangle;
	}

	public void setSwapRectangle(Rectangle swapRectangle) {
		this.swapRectangle = swapRectangle;
	}

	public Rectangle getBreakCritMassRectangle() {
		return breakCritMassRectangle;
	}

	public Rectangle getDropRectangle() {
		return dropRectangle;
	}

	public void setDropRectangle(Rectangle dropRectangle) {
		this.dropRectangle = dropRectangle;
	}

	public Rectangle getFillboardRectangle() {
		return fillboardRectangle;
	}

	public void setFillboardRectangle(Rectangle fillboardRectangle) {
		this.fillboardRectangle = fillboardRectangle;
	}

	public void setClickPoint(Point p) {
		clickPoint = p;
	}

	public int getTileSize() {
		return tilesize;
	}

	public void saveCurrentGame() {
		tileGame.saveCurrentGame();
	}

	public void loadCurrentGame() {
		tileGame.loadCurrentGame();
		moveCount = tileGame.getMoveCount();
		updateTileRow();
	}

	public void subtractMoveCount() {
		tileGame.subtractMoveCount();
	}

	public void checkMoveCount() {
		tileGame.checkMoveCount();
	}

	public void setHighScore(boolean b) {
		tileGame.setHighScore(b);
	}

	public boolean isShowSwapDrop() {
		return showSwapDrop;
	}

	public void setShowSwapDrop(boolean showSwapDrop) {
		this.showSwapDrop = showSwapDrop;
	}

	public boolean getshowBreakCM() {
		return showBreakCM;
	}

	public void setShowBreakCM(boolean showBreakCM) {
		this.showBreakCM = showBreakCM;
	}

	public boolean isShowFillBoard() {
		return showFillBoard;
	}

	public void setShowFillBoard(boolean showFillBoard) {
		this.showFillBoard = showFillBoard;
	}

	public boolean isGameOver() {
		if (tileGame.GameOver()) {
			return true;
		} else
			return false;
	}

	public int getBDWIDTH() {
		return BDWIDTH;
	}

	public int getBDHEIGHT() {
		return BDHEIGHT;
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	public Rectangle getGameRect() {
		return gameRect;
	}

	public void setEditGame(boolean b) {
		editGame = b;
	}

	public int getGameMode() {
		return tileGame.getGameMode();
	}

	public void setGameMode(int gm) {

		tileGame = new TileGame(boardSize, gm);
		if (gm == 1) {
			tileGame.createRow();
		}
		// tileGame.setGameMode(gm);
	}

	public void setActualClickPoint(Point p) {
		actualClickPoint = p;
	}

	public int getBonusPoints() {
		return tileGame.getBonusPoints();
	}

	public Rectangle getNewGameButton() {
		return NGButton;
	}

	public void setMoveCount(int mc) {
		tileGame.setMoveCount(mc);
	}

	public void createNewRow() {
		tileGame.createRow();
		updateTileRow();
	}
}

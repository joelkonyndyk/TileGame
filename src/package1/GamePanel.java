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

	/* Animation Variables */
	private Sprite[][] spriteTiles;
	private Sprite[] spriteTileRow;
	private SpriteSheet ss;
	private AnimationController anim;
	private boolean isAnimating = false;
	private final int animSpeed = 2;

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

	private final int delaySml = 10;
	private final int delayLrg = 25;

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

	private boolean displayStats = false;

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

	// Animation anim;

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

		/* Animation Variables */
		spriteTiles = new Sprite[BDHEIGHT][BDWIDTH];
		spriteTileRow = new Sprite[BDWIDTH];
		anim = new AnimationController();

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

		ss = new SpriteSheet(game.getSpriteSheet());

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

					spriteTiles[x][y] = new Sprite(ss.grabImage(1, 1, 32, 32));
					spriteTiles[x][y].setPosition((y * tilesize) + gameRect.x
							+ spacing, (x * tilesize) + gameRect.y + spacing);
					spriteTiles[x][y].setTileVal(1);

					// spriteTiles[x][y] = new Sprite(ss.grabImage(1, 1, 32,
					// 32));
					// spriteTiles[x][y].setPosition((x * tilesize) + gameRect.y
					// + spacing, (y * tilesize) + gameRect.x + spacing);

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							1, 1, tex, game, c);

				} else if (temp == 2) {

					spriteTiles[x][y] = new Sprite(ss.grabImage(2, 1, 32, 32));
					spriteTiles[x][y].setPosition((y * tilesize) + gameRect.x
							+ spacing, (x * tilesize) + gameRect.y + spacing);
					spriteTiles[x][y].setTileVal(2);
					// spriteTiles[x][y].setPosition((x * tilesize) + gameRect.y
					// + spacing, (y * tilesize) + gameRect.x + spacing);

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							2, 1, tex, game, c);

				} else if (temp == 3) {

					spriteTiles[x][y] = new Sprite(ss.grabImage(3, 1, 32, 32));
					spriteTiles[x][y].setPosition((y * tilesize) + gameRect.x
							+ spacing, (x * tilesize) + gameRect.y + spacing);
					spriteTiles[x][y].setTileVal(3);
					// spriteTiles[x][y].setPosition((x * tilesize) + gameRect.y
					// + spacing, (y * tilesize) + gameRect.x + spacing);

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							3, 1, tex, game, c);

				} else if (temp == 4) {

					spriteTiles[x][y] = new Sprite(ss.grabImage(4, 1, 32, 32));
					spriteTiles[x][y].setPosition((y * tilesize) + gameRect.x
							+ spacing, (x * tilesize) + gameRect.y + spacing);
					spriteTiles[x][y].setTileVal(4);
					// spriteTiles[x][y].setPosition((x * tilesize) + gameRect.y
					// + spacing, (y * tilesize) + gameRect.x + spacing);

					tiles[x][y] = new Tile((x * tilesize) + gameRect.y
							+ spacing, (y * tilesize) + gameRect.x + spacing,
							4, 1, tex, game, c);

				} else if (temp == 5) {

					spriteTiles[x][y] = new Sprite(ss.grabImage(5, 1, 32, 32));
					spriteTiles[x][y].setPosition((y * tilesize) + gameRect.x
							+ spacing, (x * tilesize) + gameRect.y + spacing);
					spriteTiles[x][y].setTileVal(5);
					// spriteTiles[x][y].setPosition((x * tilesize) + gameRect.y
					// + spacing, (y * tilesize) + gameRect.x + spacing);

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
		int dir = tileGame.getDirection();

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
		// Sets the top row to display faded
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f));

		// draws the top row that is about to drop
		if (tileGame.getGameMode() == classic && tileGame.getDisplayRow()) {
			for (int i = 0; i < BDWIDTH; i++) {
				spriteTileRow[i].paint(g2d);
			}
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		Font fnt0 = new Font("arial", Font.BOLD, 16);
		g2d.setFont(fnt0);
		g2d.setColor(Color.WHITE);

		/**
		 * When displayStats is true display variables in game for debugging
		 * purposes
		 **/
		if (displayStats) {

			DrawOutline("Move Count: " + tileGame.getMoveCount(), 500, 70, g2d);
			DrawOutline("Display Row: " + tileGame.getDisplayRow(), 500, 85,
					g2d);
			DrawOutline("Drop Row: " + tileGame.getDropRow(), 500, 100, g2d);
			DrawOutline("Is Animating: " + isAnimating, 500, 115, g2d);

		}

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
		if (dir == 1) {
			g2d.drawImage(tex.downTile[1], game.getWidth() - 120, 65 + 32, 32,
					32, this);
		} else {
			g2d.drawImage(tex.downTile[0], game.getWidth() - 120, 65 + 32, 32,
					32, this);
		}
		// up
		if (dir == 2) {
			g2d.drawImage(tex.upTile[1], game.getWidth() - 120, 65 - 32, 32,
					32, this);
		} else {
			g2d.drawImage(tex.upTile[0], game.getWidth() - 120, 65 - 32, 32,
					32, this);
		}
		// left
		if (dir == 3) {
			g2d.drawImage(tex.leftTile[1], game.getWidth() - 120 - 32, 65, 32,
					32, this);
		} else {
			g2d.drawImage(tex.leftTile[0], game.getWidth() - 120 - 32, 65, 32,
					32, this);
		}
		// right
		if (dir == 4) {
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

		// /**
		// * Draws animation tiles on the board
		// **/
		// // down
		// if (dir == 1) {
		// for (int y = 0; y < BDWIDTH; y++) {
		// for (int x = BDHEIGHT - 1; x >= 0; x--) {
		// if (spriteTiles[x][y].getTileVal() != 0) {
		// spriteTiles[x][y].paint(g2d);
		// }
		// }
		// }
		// }
		// // up
		// else if (dir == 2) {
		// for (int y = 0; y < BDWIDTH; y++) {
		// for (int x = 0; x < BDHEIGHT; x++) {
		// if (spriteTiles[x][y].getTileVal() != 0) {
		// spriteTiles[x][y].paint(g2d);
		// }
		// }
		// }
		// }
		// // left
		// else if (dir == 3) {
		// for (int x = 0; x < BDHEIGHT; x++) {
		// for (int y = 0; y < BDWIDTH; y++) {
		// if (spriteTiles[x][y].getTileVal() != 0) {
		// spriteTiles[x][y].paint(g2d);
		// }
		// }
		// }
		// }
		// // right
		// else if (dir == 4) {
		// for (int x = 0; x < BDHEIGHT; x++) {
		// for (int y = BDWIDTH - 1; y >= 0; y--) {
		// if (spriteTiles[x][y].getTileVal() != 0) {
		// spriteTiles[x][y].paint(g2d);
		// }
		// }
		// }
		// }

		/* Draws animation tiles on the board */
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {

				if (spriteTiles[x][y].getTileVal() != 0) {
					spriteTiles[x][y].paint(g2d);
				}
			}
		}

		// draws in the tiles on the game board
		// for (int x = 0; x < BDHEIGHT; x++) {
		// for (int y = 0; y < BDWIDTH; y++) {
		// if (tileGame.getBoardVal(x, y) != 0) {
		// tiles[x][y].render(g2d);
		// }
		// }
		// }

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
				// tileGame.mergeRow();
				mergeRow();
				updateTileRow();

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

		// if (tileGame.getGameMode() == classic && tileGame.getDisplayRow()) {
		// for (int i = 0; i < BDWIDTH; i++) {
		// // tileRow[i].tick();
		// }
		// }

		// for (int x = 0; x < BDHEIGHT; x++) {
		// for (int y = 0; y < BDWIDTH; y++) {
		// // tiles[x][y].tick();
		// }
		// }

		// UpdateBoard();

		// UpdateSprites();

		UpdateSpriteVal();

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

		CheckIsAnimating();

		if (!anim.isEmpty()) {
			anim.tick();
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
		// UpdateSprites();

		// tileGame.setDirection(i);
		// tileGame.Update();
		// DropTiles();
		// UpdateBoard();
	}

	public void pointClicked(Point p) {
		clickPoint = p;
		DropTiles();
		UpdateBoard();
		UpdateSprites();
		if (tileGame.getGameMode() == 3 || tileGame.getGameMode() == 4) {
			FillSpaces();
		}
	}

	public void FillSpaces() {

		int temp = 0;
		Point tempPoint;
		int dir = tileGame.getDirection();
		float startX = 0;
		float startY = 0;

		int count = 0;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				temp = tileGame.getBoardVal(x, y);

				if (temp == 0) {

					int rand = tileGame.generateRandom();
					tileGame.setBoardVal(x, y, rand);

					tempPoint = spriteTiles[x][y].getLocation();

					startX = (spriteTiles[x][y].getX());
					startY = (spriteTiles[x][y].getY());

					// DOWN
					if (dir == 1) {

						while (tileGame.inBounds(x + (count + 1), y)
								&& tileGame.getBoardVal(x + (count + 1), y) == 0) {
							count++;
						}
						startY = (spriteTiles[x][y].getY() - ((32 + (x * 32)) + (count * 32)));
						count = 0;
					}

					// UP
					else if (dir == 2) {

						if (count == 0) {
							while (tileGame.inBounds(x + (count + 1), y)
									&& tileGame.getBoardVal(x + (count + 1), y) == 0) {
								count++;
							}
							startY = (spriteTiles[x][y].getY() + (32 + (count * 32)));
						}
						if (tileGame.inBounds(x - 1, y)) {
							startY = (spriteTiles[x - 1][y].getY() + 32);
						}

					}
					// LEFT and RIGHT
					else {
						if (count == 0) {
							while (tileGame.inBounds(x, y + (count + 1))
									&& tileGame.getBoardVal(x, y + (count + 1)) == 0) {
								count++;
							}
						}

						// LEFT
						if (dir == 3) {
							startX = (spriteTiles[x][y].getX() + (32 + (count * 32)));
						}
						// RIGHT
						else if (dir == 4) {
							startX = (spriteTiles[x][y].getX() - (32 + (count * 32)));
						}
					}

					spriteTiles[x][y].setPosition(startX, startY);

					spriteTiles[x][y].setImage(ss.grabImage(rand, 1, 32, 32));

					anim.createAnimation(new Animation(anim, spriteTiles[x][y],
							tempPoint, animSpeed, true));
				}
			}
			if (dir == 3 || dir == 4) {
				count = 0;
			}
		}
	}

	public void undo() {
		tileGame.Undo();

		anim.clearAnimations();
		// UpdateBoard();
		if (tileGame.getGameMode() == classic) {
			updateTileRow();
		}
		UpdateBoard();
	}

	public void addUndo() {
		tileGame.addUndo();
	}

	public void UpdateSprites() {

		int temp;
		int count = 0;
		Point startPoint;
		Point destPoint;
		BufferedImage valImg;
		BufferedImage blankImg;

		BufferedImage tempImg;
		Point tempPoint;

		int dir = tileGame.getDirection();

		/* Down */
		if (dir == 1) {
			for (int y = 0; y < BDWIDTH; y++) {
				for (int x = BDHEIGHT - 1; x >= 0; x--) {
					temp = tileGame.getBoardVal(x, y);

					if (temp != 0) {
						if (tileGame.inBounds(x + 1, y)
								&& tileGame.getBoardVal(x + 1, y) == 0) {
							/*
							 * This while loop determines how many empty tiles
							 * there are below a valid tiles
							 */
							while (tileGame.inBounds(x + (count + 1), y)
									&& tileGame.getBoardVal(x + (count + 1), y) == 0) {
								count++;
							}

							startPoint = getTilePosistion(x, y);
							destPoint = getTilePosistion(x + count, y);
							valImg = spriteTiles[x][y].getImage();
							blankImg = spriteTiles[x + count][y].getImage();

							spriteTiles[x + count][y].setPosition(startPoint);
							spriteTiles[x + count][y].setImage(valImg);
							spriteTiles[x + count][y].setTileVal(temp);

							spriteTiles[x][y].setTileVal(0);
							spriteTiles[x][y].setImage(blankImg);

							anim.createAnimation(new Animation(anim,
									spriteTiles[x + count][y], destPoint,
									animSpeed, true));

							/*
							 * This updates the board value to the correct new
							 * value
							 */
							tileGame.setBoardVal(x, y, 0);
							tileGame.setBoardVal(x + count, y, temp);
							count = 0;
						}
					}
				}
			}
			if (tileGame.getGameMode() == classic) {
				for (int x = BDHEIGHT - 1; x >= 0; x--) {
					for (int y = BDWIDTH - 1; y >= 0; y--) {
						if (y > 0) {
							if (tileGame.colEmpty(y)
									&& tileGame.colEmpty(y - 1) == false) {

								for (int i = BDHEIGHT - 1; i >= 0; i--) {

									temp = tileGame.getBoardVal(i, y - 1);

									startPoint = getTilePosistion(i, y - 1);
									destPoint = getTilePosistion(i, y);
									valImg = spriteTiles[i][y - 1].getImage();
									blankImg = spriteTiles[i][y].getImage();

									spriteTiles[i][y].setPosition(startPoint);
									spriteTiles[i][y].setImage(valImg);
									spriteTiles[i][y].setTileVal(temp);

									spriteTiles[i][y - 1].setTileVal(0);
									spriteTiles[i][y - 1].setImage(blankImg);

									anim.createAnimation(new Animation(anim,
											spriteTiles[i][y], destPoint,
											animSpeed, true));

									/*
									 * This updates the board value to the
									 * correct new value
									 */
									tileGame.setBoardVal(i, y - 1, 0);
									tileGame.setBoardVal(i, y, temp);
								}
							}
						}
					}
				}
			}
		}
		/* Up */
		else if (dir == 2) {
			for (int y = 0; y < BDWIDTH; y++) {
				for (int x = 0; x < BDHEIGHT; x++) {

					temp = tileGame.getBoardVal(x, y);

					if (temp != 0) {
						if (tileGame.inBounds(x - 1, y)
								&& tileGame.getBoardVal(x - 1, y) == 0) {
							/*
							 * This while loop determines how many empty tiles
							 * there are below a valid tiles
							 */
							while (tileGame.inBounds(x - (count + 1), y)
									&& tileGame.getBoardVal(x - (count + 1), y) == 0) {
								count++;
							}

							startPoint = getTilePosistion(x, y);
							destPoint = getTilePosistion(x - count, y);
							valImg = spriteTiles[x][y].getImage();
							blankImg = spriteTiles[x - count][y].getImage();

							spriteTiles[x - count][y].setPosition(startPoint);
							spriteTiles[x - count][y].setImage(valImg);
							spriteTiles[x - count][y].setTileVal(temp);

							spriteTiles[x][y].setTileVal(0);
							spriteTiles[x][y].setImage(blankImg);

							anim.createAnimation(new Animation(anim,
									spriteTiles[x - count][y], destPoint,
									animSpeed, true));

							/*
							 * This updates the board value to the correct new
							 * value
							 */
							tileGame.setBoardVal(x, y, 0);
							tileGame.setBoardVal(x - count, y, temp);
							count = 0;
						}
					}
				}
			}
		}
		/* Left */
		else if (dir == 3) {
			for (int x = 0; x < BDHEIGHT; x++) {
				for (int y = 0; y < BDWIDTH; y++) {

					temp = tileGame.getBoardVal(x, y);

					if (temp != 0) {
						if (tileGame.inBounds(x, y - 1)
								&& tileGame.getBoardVal(x, y - 1) == 0) {
							/*
							 * This while loop determines how many empty tiles
							 * there are below a valid tiles
							 */
							while (tileGame.inBounds(x, y - (count + 1))
									&& tileGame.getBoardVal(x, y - (count + 1)) == 0) {
								count++;
							}

							startPoint = getTilePosistion(x, y);
							destPoint = getTilePosistion(x, y - count);
							valImg = spriteTiles[x][y].getImage();
							blankImg = spriteTiles[x][y - count].getImage();

							spriteTiles[x][y - count].setPosition(startPoint);
							spriteTiles[x][y - count].setImage(valImg);
							spriteTiles[x][y - count].setTileVal(temp);

							spriteTiles[x][y].setTileVal(0);
							spriteTiles[x][y].setImage(blankImg);

							anim.createAnimation(new Animation(anim,
									spriteTiles[x][y - count], destPoint,
									animSpeed, true));

							/*
							 * This updates the board value to the correct new
							 * value
							 */
							tileGame.setBoardVal(x, y, 0);
							tileGame.setBoardVal(x, y - count, temp);
							count = 0;
						}
					}
				}
			}
		}
		/* Right */
		else if (dir == 4) {
			for (int x = 0; x < BDHEIGHT; x++) {
				for (int y = BDWIDTH - 1; y >= 0; y--) {

					temp = tileGame.getBoardVal(x, y);

					if (temp != 0) {
						if (tileGame.inBounds(x, y + 1)
								&& tileGame.getBoardVal(x, y + 1) == 0) {
							/*
							 * This while loop determines how many empty tiles
							 * there are below a valid tiles
							 */
							while (tileGame.inBounds(x, y + (count + 1))
									&& tileGame.getBoardVal(x, y + (count + 1)) == 0) {
								count++;
							}

							startPoint = getTilePosistion(x, y);
							destPoint = getTilePosistion(x, y + count);
							valImg = spriteTiles[x][y].getImage();
							blankImg = spriteTiles[x][y + count].getImage();

							spriteTiles[x][y + count].setPosition(startPoint);
							spriteTiles[x][y + count].setImage(valImg);
							spriteTiles[x][y + count].setTileVal(temp);

							spriteTiles[x][y].setTileVal(0);
							spriteTiles[x][y].setImage(blankImg);

							anim.createAnimation(new Animation(anim,
									spriteTiles[x][y + count], destPoint,
									animSpeed, true));

							/*
							 * This updates the board value to the correct new
							 * value
							 */
							tileGame.setBoardVal(x, y, 0);
							tileGame.setBoardVal(x, y + count, temp);
							count = 0;
						}
					}
				}
			}
		}
	}

	public void CheckIsAnimating() {
		if (anim.isEmpty()) {
			isAnimating = false;
		} else {
			isAnimating = true;
		}
	}

	public boolean IsAnimating() {
		return isAnimating;
	}

	public void UpdateSpriteVal() {
		int tempVal = 0;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				int temp = tileGame.getBoardVal(x, y);

				if (temp == 0) {
					tempVal = 0;
				} else if (temp == 1) {
					tempVal = 1;
				} else if (temp == 2) {
					tempVal = 2;
				} else if (temp == 3) {
					tempVal = 3;
				} else if (temp == 4) {
					tempVal = 4;
				} else if (temp == 5) {
					tempVal = 5;
				}
				spriteTiles[x][y].setTileVal(tempVal);
			}
		}
	}

	public void UpdateBoard() {
		int tempNum;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				tempNum = 7; // default to the 0 value tile
				int temp = tileGame.getBoardVal(x, y);
				Point tempPoint = new Point((y * tilesize) + gameRect.x
						+ spacing, (x * tilesize) + gameRect.y + spacing);

				if (temp != 0) {
					// if tile is not empty the board value is the same as the
					// tile position
					tempNum = temp;
				}

				spriteTiles[x][y].setImage(ss.grabImage(tempNum, 1, 32, 32));
				spriteTiles[x][y].setPosition(tempPoint.x, tempPoint.y);
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

		// tileGame.outputBoard();

		String shapeStr = "";
		String shape = "";
		int count;
		int tileVal;

		if (clickPoint != null && !tileGame.CheckSingle(clickPoint)) {
			tileVal = tileGame.getBoardVal(clickPoint.x, clickPoint.y);

			if (actualClickPoint.x > game.getWidth() - 175) {
				clickPt = game.getWidth() - 175;
			} else {
				clickPt = actualClickPoint.x;
			}

			if (tileVal != 5) {
				tileGame.addMoveCount();
				copyBoard();
				tileGame.addUndo();
			}
			tileGame.checkShapes(clickPoint);
			count = tileGame.CheckBoard(clickPoint);

			if (tileVal != 5) {
				if (tileGame.isSquare()) {
					shapeStr = "Square!";
					shape = "square";
					tileGame.setSquare(false);
				} else if (tileGame.isLine()) {
					shapeStr = "Line!";
					shape = "line";
					tileGame.setLine(false);
				} else if (tileGame.isCorner()) {
					shapeStr = "Corner!";
					shape = "corner";
					tileGame.setCorner(false);
				} else if (tileGame.isStairs()) {
					shapeStr = "Stairs!";
					shape = "stairs";
					tileGame.setStairs(false);
				} else if (tileGame.isCross()) {
					shapeStr = "Cross!";
					shape = "cross";
					tileGame.setCross(false);
				} else if (tileGame.isL()) {
					shapeStr = "Awesome L!";
					shape = "l";
					tileGame.setL(false);
				} else if (tileGame.isT()) {
					shapeStr = "Sweet T!";
					shape = "t";
					tileGame.setT(false);
				}

				c.addEntity(new Text(shapeStr, game, c, clickPt,
						actualClickPoint.y - 50, delaySml));
				tileGame.CalculateScore(count, shape);

				// Draw this text on screen if the board gets cleared
				if (tileGame.tileTypeCount() == 0) {
					Text t = new Text("Board Cleared! +1000pts!", game, c,
							(game.getWidth() / 2) - 200,
							(game.getHeight() / 2) - 100, delayLrg);
					t.setFont(new Font("arial", Font.BOLD, 36));
					t.setSpeed(2);

					Text t1 = new Text("Bonus Points! +100!", game, c,
							(game.getWidth() / 2) - 175,
							(game.getHeight() / 2) - 50, delayLrg);
					t1.setFont(new Font("arial", Font.BOLD, 36));
					t1.setSpeed(2);

					c.addEntity(t);
					c.addEntity(t1);
				}

			}
			copyTempBoard();
			// breakTiles();
			// tileGame.DropTiles();

			if (tileVal != 5) {

				if (tileGame.getComboStr() != "") {
					c.addEntity(new Text(tileGame.getComboStr(), game, c,
							clickPt, actualClickPoint.y + 25, delaySml));

				}
				if (tileGame.getBonusPtStr() != "") {
					c.addEntity(new Text(tileGame.getBonusPtStr(), game, c,
							clickPt, actualClickPoint.y - 25, delaySml));
					c.addEntity(new Text(tileGame.getScoreStr(), game, c,
							clickPt, actualClickPoint.y, delaySml));

				} else {
					c.addEntity(new Text(tileGame.getScoreStr(), game, c,
							actualClickPoint.x, actualClickPoint.y, delaySml));
				}
			}
		}

		if (tileGame.getGameMode() == criticalMass) {

		}

	}

	public void RemoveClicked() {

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
				.getHeight() / 2, delaySml));

	}

	public void checkCreateRow() {
		tileGame.checkCreateRow();
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
						.parseInt(str[2]) - 25, Integer.parseInt(str[1]),
						delaySml));
				c.addEntity(new Text(tempScore + " Points!", game, c, Integer
						.parseInt(str[2]), Integer.parseInt(str[1]) + 30,
						delaySml));
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

		int count = 0;

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
				count++;
				// showBreakCM = true;
			}
		}

		if (count > 0) {
			showBreakCM = true;
		} else {
			showBreakCM = false;
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
		int count = 0;
		Point startPoint;
		Point destPoint;

		for (int i = 0; i < BDWIDTH; i++) {
			// tileGame.addUndo();
			// tileGame.setPrevRowVal(i, tileGame.getBoardRowVal(i));
			if (tileGame.getRowVal(i) != 0 && tileGame.getBoardVal(0, i) == 0) {

				while (tileGame.inBounds(0 + count, i)
						&& tileGame.getBoardVal(0 + count, i) == 0) {
					count++;
				}

				startPoint = getTileRowPosistion(i);
				destPoint = getTilePosistion(0 + count - 1, i);

				// This section changes the values on the tile game side
				tileGame.setBoardVal(0 + count - 1, i,
						tileGame.getBoardRowVal(i));
				tileGame.setBoardRowVal(i, 0);

				// Sets the tile to move's image to the corresponding row tile
				spriteTiles[0 + (count - 1)][i].setImage(spriteTileRow[i]
						.getImage());

				// Sets the board tile's position to the row tile's position
				spriteTiles[0 + (count - 1)][i].setPosition(startPoint);

				// creates an animation to move the board tile back to its
				// original position
				anim.createAnimation(new Animation(anim,
						spriteTiles[0 + (count - 1)][i], destPoint, animSpeed,
						true));

				tileGame.mergeRow();

				count = 0;
			}

		}
	}

	public Point getTilePosistion(int x, int y) {
		Point p = new Point((y * tilesize) + gameRect.x + spacing,
				(x * tilesize) + gameRect.y + spacing);
		return p;
	}

	public Point getTileRowPosistion(int i) {
		Point p = new Point((i * tilesize) + gameRect.x + spacing, gameRect.y
				+ spacing - tilesize - 10);
		return p;
	}

	public Sprite getTileSprite(int x, int y) {
		return spriteTiles[x][y];
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

		int tileNum;

		for (int i = 0; i < BDWIDTH; i++) {
			tileNum = 7; // default to the 0 value tile
			int temp = tileGame.getBoardRowVal(i);
			int tempX = (gameRect.y + spacing - tilesize - 10);

			if (temp != 0) {
				// if tile is not empty the board value is the same as the
				// tile position
				tileNum = temp;
			}

			spriteTileRow[i] = new Sprite(ss.grabImage(tileNum, 1, 32, 32));
			spriteTileRow[i].setPosition((i * tilesize) + gameRect.x + spacing,
					tempX);
		}
	}

	public void setBoardVal(int x, int y) {
		tileGame.setBoardVal(x, y, editTileVal);
	}

	public boolean EditingBoard() {
		return editGame;
	}

	public void OutputBoard() {
		tileGame.outputBoard();
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
		UpdateBoard();
		updateTileRow();
	}

	public void subtractMoveCount() {
		tileGame.subtractMoveCount();
	}

	public void addMoveCount() {
		tileGame.addMoveCount();
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
			// tileGame.createRow();
		}
		gameMode = gm;
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

	public void setDisplayStats(boolean b) {
		displayStats = b;
	}

	public void createNewRow() {
		tileGame.createRow();
		updateTileRow();
	}
}

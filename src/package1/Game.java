package package1;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

/**********************************************************************
 * 
 * DESCRIPTION
 * 
 * @author Joel Konyndyk
 * @version 09/04/14
 * 
 *********************************************************************/
public class Game extends Canvas implements Runnable, WindowListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	// height is 300
	public static final int HEIGHT = WIDTH / 12 * 10;
	public static final int SCALE = 2;

	public final String TITLE = "Tile Game";

	private boolean running = false;
	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage spriteSheetMisc = null;
	private BufferedImage background = null;

	private Textures tex;
	private Controller c;
	private Menu menu;
	private Scores scores;
	private NewGame newGame;
	private GamePanel gamePanel;
	private LoadScreen loadScreen;
	private OptionsMenu options;

	private int screenX = 0;
	private int screenY = 0;

	public LinkedList<EntityA> ea;
	public LinkedList<EntityB> eb;

	// public static GameState State = GameState.MENU;
	public static GameState State = GameState.LOAD;

	private boolean displayStats = false;

	private int updates;
	private int frames;

	/** This represents the main panel in which the game is contained */
	public static GamePanel panel;

	/**
	 * This represents the main frame that holds and displays the game
	 */
	public static JFrame frame;

	// initialize
	public void init() {
		requestFocus();
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/sprite_Sheet.png");
			spriteSheetMisc = loader.loadImage("/misc.png");
			background = loader.loadImage("/background.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		tex = new Textures(this);
		c = new Controller(tex, this);

		gamePanel = new GamePanel(tex, this, c);
		gamePanel.init();

		menu = new Menu(this);
		scores = new Scores(this, gamePanel);
		newGame = new NewGame(this);
		loadScreen = new LoadScreen(this);
		options = new OptionsMenu(this);

		screenX = this.getLocationOnScreen().x;
		screenY = this.getLocationOnScreen().y;

		ea = c.getEntityA();
		// eb = c.getEntityB();

		this.addKeyListener(new KeyInput(this));
		this.addMouseListener(new MouseInput(this, gamePanel));
		this.addMouseMotionListener(new MouseInput(this, gamePanel));

		// c.createEnemy(enemy_count);

	}

	private synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (!running)
			return;

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updatesTemp = 0;
		int framesTemp = 0;
		long timer = System.currentTimeMillis();

		while (running) {
			// this is the game loop
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				updatesTemp++;
				delta--;
			}
			render();
			framesTemp++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println(updates + " Ticks, Fps " + frames);
				updates = updatesTemp;
				frames = framesTemp;
				updatesTemp = 0;
				framesTemp = 0;
			}
		}
		stop();
	}

	private void tick() {
		if (State == GameState.GAME) {
			gamePanel.tick();
		} else if (State == GameState.LOAD) {
			loadScreen.tick();
			if (!loadScreen.isGameLoading()) {
				State = GameState.MENU;
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		// anything under here is what is being drawn
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

		if (State == GameState.GAME) {
			gamePanel.render(g);
		} else if (State == GameState.MENU) {
			menu.render(g);
		} else if (State == GameState.SCORES) {
			scores.render(g);
		} else if (State == GameState.NEWGAME) {
			newGame.render(g);
		} else if (State == GameState.LOAD) {
			loadScreen.render(g);
		} else if (State == GameState.OPTIONS) {
			options.render(g);
		}

		if (displayStats) {
			// move font out of render()
			Font fnt0 = new Font("arial", Font.BOLD, 15);
			g.setFont(fnt0);
			g.setColor(Color.WHITE);
			g.drawString(updates + " Ticks", 15, 15);
			g.drawString("Fps " + frames, 15, 30);

		}

		// ///////////

		g.dispose();
		bs.show();

	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		// displays stats when "P" is pressed
		if (key == KeyEvent.VK_P) {
			if (displayStats) {
				displayStats = false;
			} else {
				displayStats = true;
			}
		}
		// Fires undo function when "U" is pressed
		if (key == KeyEvent.VK_U) {
			if (!gamePanel.getUndoList().isEmpty()) {
				if (!gamePanel.gameIsOver()) {
					if (gamePanel.getGameMode() == 1) {
						gamePanel.subtractMoveCount();
						gamePanel.checkMoveCount();
						gamePanel.setUndoPts(true);
					}
					gamePanel.undo();
				}
			}
		}

		// if (State == GameState.GAME) {
		// }
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		// if (State == GameState.GAME) {
		// }

	}

	public static void main(String[] args) {

		Game game = new Game();

		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.addWindowListener(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public BufferedImage getSpriteSheetMisc() {
		return spriteSheetMisc;
	}

	public Menu getMenu() {
		return menu;
	}

	public Scores getScores() {
		return scores;
	}

	public NewGame getNewGame() {
		return newGame;
	}

	public OptionsMenu getOptionsMenu() {
		return options;
	}

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (State == GameState.GAME) {
			gamePanel.saveCurrentGame();
			screenX = this.getLocationOnScreen().x;
			screenY = this.getLocationOnScreen().y;
			gamePanel.captureScreen(screenX, screenY);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
}

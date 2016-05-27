package package1;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MouseInput implements MouseListener, MouseMotionListener {

	private Game game;
	private GamePanel gamePanel;

	private Menu menu;
	private Scores scores;
	private NewGame newGame;
	private OptionsMenu options;

	private StopWatch mouseTimer;

	private int BDSIZE;

	private int BDWIDTH;
	private int BDHEIGHT;

	private int screenX = 0;
	private int screenY = 0;

	// private boolean

	private Point clickPoint;

	public MouseInput(final Game game, final GamePanel gamePanel) {
		this.game = game;
		this.gamePanel = gamePanel;

		menu = this.game.getMenu();
		scores = this.game.getScores();
		newGame = this.game.getNewGame();
		options = this.game.getOptionsMenu();

		this.BDHEIGHT = gamePanel.getBDHEIGHT();
		this.BDWIDTH = gamePanel.getBDWIDTH();

		mouseTimer = new StopWatch();
		mouseTimer.start();
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// gamePanel.boardCleared();

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {

		screenX = game.getLocationOnScreen().x;
		screenY = game.getLocationOnScreen().y;

		int mx = e.getX();
		int my = e.getY();

		Point p = new Point(mx, my);

		gamePanel.mouseClicked(p);

		if (game.State == GameState.GAME) {

			// gamePanel.boardCleared();

			if (gamePanel.getGameMode() == 2 || gamePanel.getGameMode() == 3
					|| gamePanel.getGameMode() == 4) {

				// Don't allow user to change direction when there are
				// animations occurring
				if (!gamePanel.IsAnimating()) {

					// down & up
					if (mx >= game.getWidth() - 120
							&& mx <= game.getWidth() - 120 + 32) {
						// down
						if (my >= 65 + 32 && my <= 65 + 32 + 32) {
							gamePanel.setDirection(1);
						}
						// up
						else if (my >= 65 - 32 && my <= 65) {
							gamePanel.setDirection(2);
						}
					}
					// left & right
					if (my >= 65 && my <= 65 + 32) {
						// left
						if (mx >= game.getWidth() - 120 - 32
								&& mx <= game.getWidth() - 120) {
							gamePanel.setDirection(3);
						}
						// right
						else if (mx >= game.getWidth() - 120 + 32
								&& mx <= game.getWidth() - 120 + 32 + 32) {
							gamePanel.setDirection(4);
						}
					}
				}
			}

			if (gamePanel.getGameMode() == 1) {

				// Classic mode power ups
				if (!gamePanel.gameIsOver()) {
					if (gamePanel.isShowSwapDrop()) {
						// swap button (25,90)
						if (gamePanel.getSwapRectangle().contains(p)) {
							gamePanel.createNewRow();
							gamePanel.displayBPointUse(5);
						}
						// drop row button (100, 90)
						else if (gamePanel.getDropRectangle().contains(p)) {
							gamePanel.addUndo();
							gamePanel.setDropRow(true);
							gamePanel.addMoveCount();
							gamePanel.displayBPointUse(10);
						}
					}
					if (gamePanel.isShowFillBoard()) {
						// fill board button (175, 90)
						if (gamePanel.getFillboardRectangle().contains(p)) {
							gamePanel.fillBoard();
							gamePanel.displayBPointUse(50);
							gamePanel.setShowFillBoard(false);
						}
					}
				}
				gamePanel.checkCreateRow();
				gamePanel.updateTileRow();
			}

			// Critical Mass Power ups
			if (gamePanel.getGameMode() == 3) {
				if (gamePanel.getshowBreakCM()) {
					if (gamePanel.getBreakCritMassRectangle().contains(p)) {
						gamePanel.removeLargestCritMass();
						gamePanel.displayBPointUse(100);
						gamePanel.setShowBreakCM(false);
						gamePanel.DropTGTiles();
					}
				}
			}

			// menu and undo buttons
			if (my >= 10 && my <= 42) {
				// menu button
				if (mx >= 10 && mx <= 106) {
					gamePanel.saveCurrentGame();
					// takes screenshot
					gamePanel.captureScreen(screenX, screenY);

					Game.State = GameState.MENU;
					gamePanel.setHighScore(false);
				}
				// undo button
				if (!gamePanel.getUndoList().isEmpty()) {
					// gamePanel.setDisplayUndoButton(true);
					// if (!gamePanel.gameIsOver()) {
					if (gamePanel.getTimeLeft() > 0) {
						if (gamePanel.getUndoRectangle().contains(p)) {
							if (gamePanel.getGameMode() == 1) {
								gamePanel.subtractMoveCount();
								gamePanel.checkMoveCount();
								gamePanel.setDropRow(false);
								gamePanel.setUndoPts(true);
								// System.out.println("yep");
							}
							gamePanel.undo();

						}
					} else {

					}
				}
			}

			if (gamePanel.EditingBoard()) {
				for (int x = 0; x < BDHEIGHT; x++) {
					for (int y = 0; y < BDWIDTH; y++) {
						if (gamePanel.getTile(x, y).getBounds()
								.contains(e.getPoint())) {
							gamePanel.setBoardVal(x, y);
						} else {
							gamePanel.pointClicked(null);
						}
					}
				}
			}
			/*
			 * This Section check to see if any of the game tiles were clicked
			 */
			else if (!gamePanel.IsAnimating()) {
				Point tempPoint = null;
				for (int x = 0; x < BDHEIGHT; x++) {
					for (int y = 0; y < BDWIDTH; y++) {
						if (gamePanel.getTileSprite(x, y).getBounds()
								.contains(e.getPoint())) {
							// if (gamePanel.getTile(x, y).getBounds()
							// .contains(e.getPoint())) {
							gamePanel.setActualClickPoint(new Point(mx, my));
							tempPoint = new Point(x, y);
							// gamePanel.pointClicked(new Point(x, y));
							// gamePanel.DropTiles();

							// gamePanel.OutputBoard();

							gamePanel.boardCleared();
							gamePanel.checkMoveCount();
						} else {

							// gamePanel.pointClicked(null);
						}
					}
				}
				gamePanel.pointClicked(tempPoint);
				// gamePanel.OutputBoard();
			}

			// gamePanel.boardCleared();

			if (gamePanel.gameIsOver()) {
				if (gamePanel.getTimeLeft() <= 0) {
					// new game button
					if (gamePanel.getNewGameButton().contains(p)) {
						gamePanel.setGameIsOver(false);
						if (gamePanel.getGameMode() == 1) {
							gamePanel.setMoveCount(0);
						}
						int gm = gamePanel.getGameMode();
						gamePanel.setGameMode(gm);
					}
				}
			}
		} else if (game.State == GameState.NEWGAME) {

			// Back Button
			if (newGame.getBackButton().contains(p)) {
				Game.State = GameState.MENU;
			}

			// Classic Button
			if (newGame.getClassicButton().contains(p)) {
				gamePanel.setGameIsOver(false);
				gamePanel.setGameMode(1);
				gamePanel.setMoveCount(0);
				// gamePanel.updateTileRow();
				Game.State = GameState.GAME;
				gamePanel.UpdateBoard();
			}

			// Gravity Button
			if (newGame.getGravityButton().contains(p)) {
				gamePanel.setGameIsOver(false);
				gamePanel.setGameMode(2);
				Game.State = GameState.GAME;
				gamePanel.UpdateBoard();
			}

			// Critical Mass Button
			if (newGame.getCriticalMassButton().contains(p)) {
				gamePanel.setGameIsOver(false);
				gamePanel.setGameMode(3);
				gamePanel.checkCritMass();
				Game.State = GameState.GAME;
				gamePanel.UpdateBoard();
			}

			// Infinity Button
			if (newGame.getInfinityButton().contains(p)) {
				gamePanel.setGameIsOver(false);
				gamePanel.setGameMode(4);
				Game.State = GameState.GAME;
				gamePanel.UpdateBoard();
			}

		} else if (game.State == GameState.MENU) {

			// Continue Button
			if (menu.getContinueButton().contains(p)) {
				// Pressed Continue Button
				gamePanel.loadCurrentGame();
				if (gamePanel.getGameMode() == 3) {
					gamePanel.checkCritMass();
				}
				Game.State = GameState.GAME;
			}

			// New Game Button
			if (menu.getNewgameButton().contains(p)) {
				// Pressed New Game Button
				Game.State = GameState.NEWGAME;
			}

			// Scores Button
			if (menu.getScoresButton().contains(p)) {
				// Pressed Scores Button
				Game.State = GameState.SCORES;
			}

			// Options Button
			if (menu.getOptionsButton().contains(p)) {
				// Pressed Options Button
				Game.State = GameState.OPTIONS;
			}

			// Quit Button
			if (menu.getQuitButton().contains(p)) {
				// Pressed Quit Button
				System.exit(1);
			}
		} else if (game.State == GameState.SCORES) {

			// Reset Button
			if (scores.getResetButton().contains(p)) {

				JPanel resetPanel = new JPanel();
				resetPanel
						.setLayout(new BoxLayout(resetPanel, BoxLayout.Y_AXIS));
				resetPanel.add(new JLabel(
						"Are you sure you want to reset scores?"));
				int reset = JOptionPane.showConfirmDialog(null, resetPanel,
						"Reset Scores", JOptionPane.YES_NO_OPTION);

				// if yes is pressed reset the game scores
				if (reset == JOptionPane.YES_OPTION) {
					// Resets the high scores
					scores.resetGameScores();
				}

				// if no button pressed exit the program
				if (reset == JOptionPane.NO_OPTION)
					System.exit(1);

				// if close button pressed exit the program
				if (reset == JOptionPane.CLOSED_OPTION)
					System.exit(1);

			}

			// Back Button
			if (scores.getBackButton().contains(p)) {
				Game.State = GameState.MENU;
			}
		} else if (game.State == GameState.OPTIONS) {
			if (options.getBackButton().contains(p)) {
				game.State = GameState.MENU;
			}
		}

		// gamePanel.boardCleared();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = e.getPoint();

		int mx = e.getX();
		int my = e.getY();

		if (p != null) {

			if (game.State == GameState.GAME) {

				// Direction Buttons
				// down button
				if (gamePanel.getDownRectangle().contains(p)) {
					gamePanel.setDownInfo(true);
				} else {
					gamePanel.setDownInfo(false);
				}
				// up button
				if (gamePanel.getUpRectangle().contains(p)) {
					gamePanel.setUpInfo(true);
				} else {
					gamePanel.setUpInfo(false);
				}
				// left button
				if (gamePanel.getLeftRectangle().contains(p)) {
					gamePanel.setLeftInfo(true);
				} else {
					gamePanel.setLeftInfo(false);
				}
				// right button
				if (gamePanel.getRightRectangle().contains(p)) {
					gamePanel.setRightInfo(true);
				} else {
					gamePanel.setRightInfo(false);
				}

				// Classic Game Mode
				if (gamePanel.getGameMode() == 1) {
					if (!gamePanel.gameIsOver()) {
						// swap button
						if (gamePanel.getSwapRectangle().contains(p)) {
							gamePanel.setShowSwapInfo(true);
						} else {
							gamePanel.setShowSwapInfo(false);
						}
						// drop row button
						if (gamePanel.getDropRectangle().contains(p)) {
							gamePanel.setShowDropRowInfo(true);
						} else {
							gamePanel.setShowDropRowInfo(false);
						}
						// fill board button
						if (gamePanel.getFillboardRectangle().contains(p)) {
							gamePanel.setShowFillBoardInfo(true);
						} else {
							gamePanel.setShowFillBoardInfo(false);
						}
					}
				}
			}
			// Critical Mass Power ups
			if (gamePanel.getGameMode() == 3) {
				// Break Critical Mass button
				if (gamePanel.getBreakCritMassRectangle().contains(p)) {
					gamePanel.setShowBreakCMInfo(true);
				} else {
					gamePanel.setShowBreakCMInfo(false);
				}
			}
			gamePanel.setMouseLocation(p);
		}
	}
}

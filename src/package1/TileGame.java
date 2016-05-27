package package1;

import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**********************************************************************
 * 
 * DESCRIPTION
 * 
 * @author Joel Konyndyk
 * @version 9/9/14
 * 
 *********************************************************************/
public class TileGame {

	/** This represents the game board grid */
	private int[][] board;

	private int[] boardRow;
	private int[] prevBoardRow;

	private boolean rowCreated = false;

	/** This represents the board height and length of the current game */
	private int BDSIZE;

	private int BDWIDTH;
	private int BDHEIGHT;

	private final int tileOriginX = 2;
	private final int tileOriginY = 182;

	private final int tilesize = 32;
	private int spacing = 3;

	/** This contains the list of moves made */
	private Stack<String> undoList = new Stack<String>();

	private Stack<String> undoRowList = new Stack<String>();

	/**
	 * This list contains the values of previously checked tiles when checking
	 * shapes
	 */
	private Stack<String> alreadyChecked = new Stack<String>();

	private Stack<Integer> scoreUndoList = new Stack<Integer>();
	private Stack<Integer> bonusPtUndoList = new Stack<Integer>();

	private Stack<String> critMassPercentages = new Stack<String>();
	private ArrayList<String> checkedTiles = new ArrayList<String>();
	private ArrayList<String> CMGroups = new ArrayList<String>();

	private ArrayList<String> tempCritMass = new ArrayList<String>();

	private String gameBoardStr;
	private String undoListStr;

	private String scoreStr;
	private String bonusPtStr;
	private String comboStr;

	private int score;
	private int prevScore;

	private int bonusPoints;
	private int prevBonusPoints;

	private int classicHighScore;
	private int gravityHighScore;
	private int critMassHighScore;
	private int infinityHighScore;

	public static int direction;

	private final int down = 1;
	private final int up = 2;
	private final int left = 3;
	private final int right = 4;

	public static int gameMode;

	private final int classic = 1;
	private final int gravity = 2;
	private final int criticalMass = 3;
	private final int infinity = 4;

	private final int critMassSize = 100;
	private int massSize;
	private final float showCMSize = (float) .05;

	private final int rowDisplayCount = 15;
	private final int rowDropCount = 25;

	private boolean highScore = false;

	private boolean square = false;
	private boolean line = false;
	private boolean stairs = false;
	private boolean corner = false;
	private boolean cross = false;
	private boolean T = false;
	private boolean L = false;

	private boolean inBounds = false;
	private boolean gameOver = false;

	private int boardVal;

	private int randomMin = 1;
	private int randomMax = 4;
	private int randomMaxCM = 5;

	private int combo = 0;

	private int timeLeft = 5;

	private final int small = 15;

	private final int defaultW = 25;
	private final int defaultH = 15;

	private final int normal = 25;
	private final int large = 40;

	private int moveCount = 0;
	private int prevMoveCount;

	private boolean checkedL = false;
	private boolean checkedT = false;

	private boolean displayRow = false;
	private boolean dropRow = false;

	private boolean boardCleared = false;

	private boolean displayClearBoard = true;

	// 0 - 35
	private int[] scoreList = new int[] { 0, 0, 5, 9, 15, 22, 31, 41, 53, 66,
			81, 97, 115, 168, 201, 226, 252, 280, 305, 342, 366, 397, 431, 453,
			481, 522, 553, 597, 631, 681, 741, 809, 885, 922 };

	private double[] comboList = new double[] { 0, 1, 2, 4, 6, 9, 18, 32, 48,
			64, 72, 84 };

	/**********************************************************************
	 * The ConnectFourGame constructor creates a new board with the desired
	 * board size, sets the size of winCount to the number of players, and
	 * resets the board to be ready for a new game.
	 * 
	 *********************************************************************/
	public TileGame() {

		BDWIDTH = defaultW;
		BDHEIGHT = defaultH;

		direction = down;
		gameMode = classic;

		score = 0;
		bonusPoints = 0;

		board = new int[BDHEIGHT][BDWIDTH];
		boardRow = new int[BDWIDTH];
		prevBoardRow = new int[BDWIDTH];
		createBoard();
		createRow();
	}

	public TileGame(int boardSize, int gamemode) {

		score = 0;

		if (boardSize == 1) {

			BDWIDTH = defaultW;
			BDHEIGHT = defaultH;

			board = new int[BDHEIGHT][BDWIDTH];
			boardRow = new int[BDWIDTH];
			prevBoardRow = new int[BDWIDTH];

		} else if (boardSize == 2) {

			BDWIDTH = defaultW;
			BDHEIGHT = defaultH;

			board = new int[BDHEIGHT][BDWIDTH];
			boardRow = new int[BDWIDTH];
			prevBoardRow = new int[BDWIDTH];

		} else if (boardSize == 3) {

			BDWIDTH = defaultW;
			BDHEIGHT = defaultH;

			board = new int[BDHEIGHT][BDWIDTH];
			boardRow = new int[BDWIDTH];
			prevBoardRow = new int[BDWIDTH];

		} else {

			BDWIDTH = defaultW;
			BDHEIGHT = defaultH;

			board = new int[BDHEIGHT][BDWIDTH];
			boardRow = new int[BDWIDTH];
			prevBoardRow = new int[BDWIDTH];
		}

		if (gamemode == 0) {
			this.gameMode = classic;
		} else {
			this.gameMode = gamemode;
		}
		direction = down;

		createBoard();
		createRow();

		if (gameMode == criticalMass) {
			randomMax = randomMaxCM;
		}

		loadGame();
	}

	public void createBoard() {

		if (gameMode == criticalMass)
			randomMax++;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				board[x][y] = generateRandom();
				// board[x][y] = 0;
			}
		}
	}

	public void createRow() {
		for (int i = 0; i < BDWIDTH; i++) {
			boardRow[i] = generateRandom();
			// prevBoardRow[i] = 0;
			prevBoardRow[i] = boardRow[i];
		}

		// outputRow();

		// rowCreated = true;
	}

	public int CheckBoard(Point p) {
		try {
			int x = p.x;
			int y = p.y;
			int count = 1;
			boardVal = board[x][y];

			if (boardVal == 5)
				return 0;
			if (boardVal != 0) {
				setBoardVal(x, y, 0);

				if (x == 0 && y == 0 || x == BDHEIGHT - 1 && y == 0 || x == 0
						&& y == BDWIDTH - 1 || x == BDHEIGHT - 1
						&& y == BDWIDTH - 1) {
					if (x == 0 && y == 0) {
						if (board[x + 1][y] == boardVal) {
							count += CheckBoard(new Point(x + 1, y));
						}
						if (board[x][y + 1] == boardVal) {
							count += CheckBoard(new Point(x, y + 1));
						}
					} else if (x == BDHEIGHT - 1 && y == 0) {
						if (board[x - 1][y] == boardVal) {
							count += CheckBoard(new Point(x - 1, y));
						}
						if (board[x][y + 1] == boardVal) {
							count += CheckBoard(new Point(x, y + 1));
						}
					} else if (x == 0 && y == BDWIDTH - 1) {
						if (board[x + 1][y] == boardVal) {
							count += CheckBoard(new Point(x + 1, y));
						}
						if (board[x][y - 1] == boardVal) {
							count += CheckBoard(new Point(x, y - 1));
						}
					} else if (x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
						if (board[x - 1][y] == boardVal) {
							count += CheckBoard(new Point(x - 1, y));
						}
						if (board[x][y - 1] == boardVal) {
							count += CheckBoard(new Point(x, y - 1));
						}
					}
				} else if (x == 0 || x == BDHEIGHT - 1 || y == 0
						|| y == BDWIDTH - 1) {
					if (x == 0) {
						if (board[x + 1][y] == boardVal) {
							count += CheckBoard(new Point(x + 1, y));
						}
						if (board[x][y + 1] == boardVal) {
							count += CheckBoard(new Point(x, y + 1));
						}
						if (board[x][y - 1] == boardVal) {
							count += CheckBoard(new Point(x, y - 1));
						}
					} else if (x == BDHEIGHT - 1) {
						if (board[x - 1][y] == boardVal) {
							count += CheckBoard(new Point(x - 1, y));
						}
						if (board[x][y + 1] == boardVal) {
							count += CheckBoard(new Point(x, y + 1));
						}
						if (board[x][y - 1] == boardVal) {
							count += CheckBoard(new Point(x, y - 1));
						}
					} else if (y == 0) {
						if (board[x + 1][y] == boardVal) {
							count += CheckBoard(new Point(x + 1, y));
						}
						if (board[x - 1][y] == boardVal) {
							count += CheckBoard(new Point(x - 1, y));
						}
						if (board[x][y + 1] == boardVal) {
							count += CheckBoard(new Point(x, y + 1));
						}
					} else if (y == BDWIDTH - 1) {
						if (board[x + 1][y] == boardVal) {
							count += CheckBoard(new Point(x + 1, y));
						}
						if (board[x - 1][y] == boardVal) {
							count += CheckBoard(new Point(x - 1, y));
						}
						if (board[x][y - 1] == boardVal) {
							count += CheckBoard(new Point(x, y - 1));
						}
					}
				} else {
					if (board[x + 1][y] == boardVal) {
						count += CheckBoard(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += CheckBoard(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += CheckBoard(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += CheckBoard(new Point(x, y - 1));
					}
				}
			}
			return count;
		} catch (Exception e) {
			System.out.println("Clicked to fast again!");
			return 0;
		}

	}

	public int countTiles(Point p) {
		int temp = countTilesRemoved(p);
		returnTiles();
		return temp;
	}

	public void returnTiles() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] == -1) {
					board[x][y] = boardVal;
				}
			}
		}
	}

	public int countTilesRemoved(Point p) {
		int x = p.x;
		int y = p.y;
		int count = 1;
		boardVal = board[x][y];

		if (boardVal != 0) {
			setBoardVal(x, y, -1);

			if (x == 0 && y == 0 || x == BDHEIGHT - 1 && y == 0 || x == 0
					&& y == BDWIDTH - 1 || x == BDHEIGHT - 1
					&& y == BDWIDTH - 1) {
				if (x == 0 && y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y + 1));
					}
				} else if (x == BDHEIGHT - 1 && y == 0) {
					if (board[x - 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y + 1));
					}
				} else if (x == 0 && y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x + 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
					if (board[x - 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y - 1));
					}
				}
			} else if (x == 0 || x == BDHEIGHT - 1 || y == 0
					|| y == BDWIDTH - 1) {
				if (x == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1) {
					if (board[x - 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y - 1));
					}
				} else if (y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y + 1));
					}
				} else if (y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += countTilesRemoved(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countTilesRemoved(new Point(x, y - 1));
					}
				}
			} else {
				if (board[x + 1][y] == boardVal) {
					count += countTilesRemoved(new Point(x + 1, y));
				}
				if (board[x - 1][y] == boardVal) {
					count += countTilesRemoved(new Point(x - 1, y));
				}
				if (board[x][y + 1] == boardVal) {
					count += countTilesRemoved(new Point(x, y + 1));
				}
				if (board[x][y - 1] == boardVal) {
					count += countTilesRemoved(new Point(x, y - 1));
				}
			}
		}
		return count;
	}

	public void checkShapes(Point p) {
		int x = p.x;
		int y = p.y;

		int count = countTiles(p);

		try {
			alreadyChecked.clear();
			checkSquare(x, y, count);
			if (!square) {
				alreadyChecked.clear();
				checkLine(x, y, count);
			}
			if (!line) {
				alreadyChecked.clear();
				checkCorner(x, y, count);
			}
			if (!corner) {
				alreadyChecked.clear();
				checkStairs(x, y, count);
			}
			if (!stairs) {
				alreadyChecked.clear();
				checkCross(x, y, count);
			}
			if (!cross) {
				alreadyChecked.clear();
				checkT(x, y, count);
			}
			if (!T) {
				alreadyChecked.clear();
				checkL(x, y, count);
			}
		} catch (Exception e) {
			System.out.println("Error Checking Shapes!");
		}
	}

	public void checkSquare(int x, int y, int num) {

		int count = 0;
		int nonTemp = 0;
		int tempCount;
		int sides = 0;

		int countX = 0;
		int countY = 0;

		int temp = board[x][y];
		int tempX = x;
		int tempY = y;

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		square = false;

		if (num > 3) {

			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (sides == 2) {
				if (up && right) {
					while (inBounds((x - countX), y)
							&& temp == board[(x - countX)][y]) {
						countX++;
					}
					checkSquare((x - countX) + 1, y, num);

				} else if (up && left) {
					while (inBounds(x, (y - countY))
							&& temp == board[x][(y - countY)]) {
						countY++;
					}
					checkSquare(x, (y - countY) + 1, num);
				} else if (down && right) {
					while (inBounds((x + countX), y)
							&& temp == board[(x + countX)][y]) {
						countX++;
					}
					for (int i = 0; i < countX; i++) {
						for (int j = 0; j < countX; j++) {
							if (inBounds(x + i, y + j)
									&& temp == board[x + i][y + j]) {
								count++;
							} else {
								nonTemp++;
							}
						}
					}
				} else if (down && left) {
					while (inBounds(x, y - countY)
							&& temp == board[x][y - countY]) {
						countY++;
					}
					checkSquare(x, (y - countY) + 1, num);
				}
				if (count == num && nonTemp == 0) {
					square = true;
				}
			} else if (sides == 3) {

				if (left && right && up || left && right && down) {
					while (inBounds(x, (y - countY))
							&& temp == board[x][(y - countY)]) {
						countY++;
					}
					checkSquare(x, (y - countY) + 1, num);
				} else if (left && up && down || up && right && down) {
					while (inBounds((x - countX), y)
							&& temp == board[(x - countX)][y]) {
						countX++;
					}
					checkSquare((x - countX) + 1, y, num);
				}
			} else if (sides == 4) {
				while (inBounds(x, (y - countY))
						&& temp == board[x][(y - countY)]) {
					countY++;
				}
				checkSquare(x, (y - countY) + 1, num);
			}
		}
	}

	public void checkLine(int x, int y, int num) {

		int sides = 0;
		int count = 1;
		int temp = board[x][y];

		int numX = 0;
		int numY = 0;

		line = false;

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		if (num > 3) {
			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (sides == 1) {
				if (up) {
					for (int i = 1; i < num; i++) {
						if (inBounds(x - i, y) && temp == board[x - i][y]) {
							count++;
						} else {
							break;
						}
					}
					if (count == num) {
						line = true;
					}
					up = false;

				} else if (down) {
					for (int i = 1; i < num; i++) {
						if (inBounds(x + i, y) && temp == board[x + i][y]) {
							count++;
						} else {
							break;
						}
					}
					if (count == num) {
						line = true;
					}
					down = false;

				} else if (left) {
					for (int i = 1; i < num; i++) {
						if (inBounds(x, y - i) && temp == board[x][y - i]) {
							count++;
						} else {
							break;
						}
					}
					if (count == num) {
						line = true;
					}
					left = false;

				} else if (right) {
					for (int i = 1; i < num; i++) {
						if (inBounds(x, y + i) && temp == board[x][y + i]) {
							count++;
						} else {
							break;
						}
					}
					if (count == num) {
						line = true;
					}
					right = false;
				}

			} else if (sides == 2) {

				if (up && down) {

					while (inBounds(x - numX, y) && temp == board[x - numX][y]) {
						numX++;
					}

					while (inBounds(x + numY, y) && temp == board[x + numY][y]) {
						numY++;
					}

				} else if (left && right) {

					while (inBounds(x, y - numX) && temp == board[x][y - numX]) {
						numX++;
					}

					while (inBounds(x, y + numY) && temp == board[x][y + numY]) {
						numY++;
					}
				}

				if (num == (numX + numY) - 1) {
					line = true;
				}

			}
		}
	}

	public void checkStairs(int x, int y, int num) {
		int sides = 0;
		int count = 0;
		int countTemp = 0;
		int temp = board[x][y];

		int xNum = 0;
		int yNum = 0;

		stairs = false;

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		if (num > 3) {
			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (sides == 1) {

				if (up) {

					if (inBounds(x - 1, y - 1) && temp == board[x - 1][y - 1]) {
						while (inBounds(x - xNum, y - yNum)
								&& temp == board[x - xNum][y - yNum]) {
							count++;
							if (count % 2 == 0) {
								yNum++;
							} else {
								xNum++;
							}
						}

					} else if (inBounds(x - 1, y + 1)
							&& temp == board[x - 1][y + 1]) {
						while (inBounds(x - xNum, y + yNum)
								&& temp == board[x - xNum][y + yNum]) {
							count++;
							if (count % 2 == 0) {
								yNum++;
							} else {
								xNum++;
							}
						}
					}

				} else if (down) {

					if (inBounds(x + 1, y - 1) && temp == board[x + 1][y - 1]) {
						// count = 0;
						while (inBounds(x + xNum, y - yNum)
								&& temp == board[x + xNum][y - yNum]) {
							count++;
							if (count % 2 == 0) {
								yNum++;
							} else {
								xNum++;
							}
						}
					} else if (inBounds(x + 1, y + 1)
							&& temp == board[x + 1][y + 1]) {
						// count = 0;
						while (inBounds(x + xNum, y + yNum)
								&& temp == board[x + xNum][y + yNum]) {
							count++;
							if (count % 2 == 0) {
								yNum++;
							} else {
								xNum++;
							}
						}
					}

				} else if (left) {

					if (inBounds(x - 1, y - 1) && temp == board[x - 1][y - 1]) {
						// count = 0;
						while (inBounds(x - xNum, y - yNum)
								&& temp == board[x - xNum][y - yNum]) {
							count++;
							if (count % 2 == 0) {
								xNum++;
							} else {
								yNum++;
							}
						}
					} else if (inBounds(x + 1, y - 1)
							&& temp == board[x + 1][y - 1]) {
						// count = 0;
						while (inBounds(x + xNum, y - yNum)
								&& temp == board[x + xNum][y - yNum]) {
							count++;
							if (count % 2 == 0) {
								yNum++;
							} else {
								xNum++;
							}
						}
					}

				} else if (right) {

					if (inBounds(x - 1, y + 1) && temp == board[x - 1][y + 1]) {
						// count = 0;
						while (inBounds(x - xNum, y + yNum)
								&& temp == board[x - xNum][y + yNum]) {
							count++;
							if (count % 2 == 0) {
								xNum++;
							} else {
								yNum++;
							}
						}
					} else if (inBounds(x + 1, y + 1)
							&& temp == board[x + 1][y + 1]) {
						// count = 0;
						while (inBounds(x + xNum, y + yNum)
								&& temp == board[x + xNum][y + yNum]) {
							count++;
							if (count % 2 == 0) {
								xNum++;
							} else {
								yNum++;
							}
						}
					}
				}

				if (count == num) {
					stairs = true;
				}

			} else if (sides == 2) {

				if (up && left) {
					while (inBounds(x - xNum, y + yNum)
							&& temp == board[x - xNum][y + yNum]) {
						count++;
						if (count % 2 == 0) {
							yNum++;
						} else {
							xNum++;
						}
					}
					xNum = 0;
					yNum = 0;
					while (inBounds(x + xNum, y - yNum)
							&& temp == board[x + xNum][y - yNum]) {
						countTemp++;
						if (countTemp % 2 == 0) {
							xNum++;
						} else {
							yNum++;
						}
					}

				} else if (up && right) {

					while (inBounds(x - xNum, y - yNum)
							&& temp == board[x - xNum][y - yNum]) {
						count++;
						if (count % 2 == 0) {
							yNum++;
						} else {
							xNum++;
						}
					}
					xNum = 0;
					yNum = 0;
					while (inBounds(x + xNum, y + yNum)
							&& temp == board[x + xNum][y + yNum]) {
						countTemp++;
						if (countTemp % 2 == 0) {
							xNum++;
						} else {
							yNum++;
						}
					}

				} else if (down && left) {
					while (inBounds(x - xNum, y - yNum)
							&& temp == board[x - xNum][y - yNum]) {
						count++;
						if (count % 2 == 0) {
							xNum++;
						} else {
							yNum++;
						}
					}
					xNum = 0;
					yNum = 0;
					while (inBounds(x + xNum, y + yNum)
							&& temp == board[x + xNum][y + yNum]) {
						countTemp++;
						if (countTemp % 2 == 0) {
							yNum++;
						} else {
							xNum++;
						}
					}

				} else if (down && right) {
					while (inBounds(x - xNum, y + yNum)
							&& temp == board[x - xNum][y + yNum]) {
						count++;
						if (count % 2 == 0) {
							xNum++;
						} else {
							yNum++;
						}
					}
					xNum = 0;
					yNum = 0;
					while (inBounds(x + xNum, y - yNum)
							&& temp == board[x + xNum][y - yNum]) {
						countTemp++;
						if (countTemp % 2 == 0) {
							yNum++;
						} else {
							xNum++;
						}
					}
				}
				// System.out.println(count + countTemp);
				if (num == (count + countTemp) - 1) {
					stairs = true;
				}
			}
		}
	}

	public void checkCorner(int x, int y, int num) {

		int sides = 0;
		int count = 0;
		int temp = board[x][y];

		int xNum = 0;
		int yNum = 0;

		// alreadyChecked.push(x + "," + y);

		corner = false;

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		boolean NE = false;
		boolean NW = false;
		boolean SE = false;
		boolean SW = false;

		if (num == 6 || num == 10 || num == 1) {
			// if (x < BDHEIGHT - 1) {
			// if (board[x + 1][y] == temp) {
			// sides++;
			// down = true;
			// }
			// }
			// if (x > 0) {
			// if (board[x - 1][y] == temp) {
			// sides++;
			// up = true;
			// }
			// }
			// if (y < BDWIDTH - 1) {
			// if (board[x][y + 1] == temp) {
			// sides++;
			// right = true;
			// }
			// }
			// if (y > 0) {
			// if (board[x][y - 1] == temp) {
			// sides++;
			// left = true;
			// }
			// }

			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (inBounds(x - 1, y - 1) && temp == board[x - 1][y - 1]) {
				NW = true;
			}
			if (inBounds(x + 1, y - 1) && temp == board[x + 1][y - 1]) {
				SW = true;
			}
			if (inBounds(x + 1, y + 1) && temp == board[x + 1][y + 1]) {
				SE = true;
			}
			if (inBounds(x - 1, y + 1) && temp == board[x - 1][y + 1]) {
				NE = true;
			}

			if (sides == 1) {
				if (up) {

					while (inBounds(x - xNum, y) && temp == board[x - xNum][y]) {
						xNum++;
					}

					if (xNum < 3) {
						corner = false;
					} else {
						xNum -= 1;
					}

					if (!inBounds(x, y - xNum) && !inBounds(x, y + xNum)) {
						corner = false;
					}

					if (inBounds(x, y - xNum)) {

						while (inBounds(x - xNum, y - yNum)
								&& temp == board[x - xNum][y - yNum]) {
							yNum++;
						}

						if (xNum == yNum - 1) {

							if (!inBounds(x - xNum, y + 1)
									&& !inBounds((x - xNum) - 1, y)) {
								checkCorner(x - xNum, y, num);
							} else if (!inBounds(x - xNum, y + 1)) {
								if (temp != board[(x - xNum) - 1][y]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							} else if (!inBounds((x - xNum) - 1, y)) {
								if (temp != board[x - xNum][y + 1]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x - xNum][y + 1]
										&& temp != board[x - (xNum + 1)][y]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							}
						}
					}
					if (inBounds(x, y + xNum)) {

						yNum = 0;

						while (inBounds(x - xNum, y + yNum)
								&& temp == board[x - xNum][y + yNum]) {
							yNum++;
						}
						if (xNum == yNum - 1) {

							if (!inBounds(x - xNum, y - 1)
									&& !inBounds(x - (xNum + 1), y)) {
								checkCorner(x - xNum, y, num);

							} else if (!inBounds(x - xNum, y - 1)) {
								if (temp != board[x - (xNum + 1)][y]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x - (xNum + 1), y)) {

								if (temp != board[x - xNum][y - 1]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x - xNum][y - 1]
										&& temp != board[x - (xNum + 1)][y]) {
									checkCorner(x - xNum, y, num);
								} else {
									corner = false;
								}
							}
						}
					}
					up = false;

				} else if (down) {

					while (inBounds(x + xNum, y) && temp == board[x + xNum][y]) {
						xNum++;
					}

					if (xNum < 3) {
						corner = false;
					} else {
						xNum -= 1;
					}

					if (!inBounds(x, y - xNum) && !inBounds(x, y + xNum)) {
						corner = false;
					}

					if (inBounds(x, y - xNum)) {

						while (inBounds(x + xNum, y - yNum)
								&& temp == board[x + xNum][y - yNum]) {
							yNum++;
						}

						if (xNum == yNum - 1) {

							if (!inBounds(x + xNum, y + 1)
									&& !inBounds(x + (xNum + 1), y)) {
								checkCorner(x + xNum, y, num);

							} else if (!inBounds(x + xNum, y + 1)) {
								if (temp != board[x + (xNum + 1)][y]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x + (xNum + 1), y)) {

								if (temp != board[x + xNum][y + 1]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x + xNum][y + 1]
										&& temp != board[x + (xNum + 1)][y]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							}
						}

					}
					if (inBounds(x, y + xNum)) {

						yNum = 0;

						while (inBounds(x + xNum, y + yNum)
								&& temp == board[x + xNum][y + yNum]) {
							yNum++;
						}
						if (xNum == yNum - 1) {

							if (!inBounds(x + xNum, y - 1)
									&& !inBounds(x + (xNum + 1), y)) {
								checkCorner(x + xNum, y, num);

							} else if (!inBounds(x + xNum, y - 1)) {
								if (temp != board[x + (xNum + 1)][y]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x + (xNum + 1), y)) {

								if (temp != board[x + xNum][y + 1]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x + xNum][y - 1]
										&& temp != board[x + (xNum + 1)][y]) {
									checkCorner(x + xNum, y, num);
								} else {
									corner = false;
								}
							}
						}
					}
					down = false;

				} else if (left) {

					while (inBounds(x, y - yNum) && temp == board[x][y - yNum]) {
						yNum++;
					}

					if (yNum < 3) {
						corner = false;
					} else {
						yNum -= 1;
					}

					if (!inBounds(x - yNum, y) && !inBounds(x + yNum, y)) {
						corner = false;
					}

					if (inBounds(x - yNum, y)) {

						while (inBounds(x - xNum, y - yNum)
								&& temp == board[x - xNum][y - yNum]) {
							xNum++;
						}

						if (yNum == xNum - 1) {

							if (!inBounds(x + 1, y - yNum)
									&& !inBounds(x, y - (yNum + 1))) {
								checkCorner(x, y - yNum, num);

							} else if (!inBounds(x + 1, y - yNum)) {
								if (temp != board[x][y - (yNum + 1)]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x, y - (yNum + 1))) {

								if (temp != board[x + 1][y - yNum]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x + 1][y - yNum]
										&& temp != board[x][y - (yNum + 1)]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							}
						}

					}
					if (inBounds(x + yNum, y)) {
						xNum = 0;

						while (inBounds(x + xNum, y - yNum)
								&& temp == board[x + xNum][y - yNum]) {
							xNum++;
						}

						if (yNum == xNum - 1) {

							if (!inBounds(x - 1, y - yNum)
									&& !inBounds(x, y - (yNum + 1))) {
								checkCorner(x, y - yNum, num);

							} else if (!inBounds(x - 1, y - yNum)) {
								if (temp != board[x][y - (yNum + 1)]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x, y - (yNum + 1))) {

								if (temp != board[x - 1][y - yNum]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x - 1][y - yNum]
										&& temp != board[x][y - (yNum + 1)]) {
									checkCorner(x, y - yNum, num);
								} else {
									corner = false;
								}
							}
						}
					}

					left = false;

				} else if (right) {

					while (inBounds(x, y + yNum) && temp == board[x][y + yNum]) {
						yNum++;
					}

					if (yNum < 3) {
						corner = false;
					} else {
						yNum -= 1;
					}

					if (!inBounds(x - yNum, y) && !inBounds(x + yNum, y)) {
						corner = false;
					}

					if (inBounds(x - yNum, y)) {

						while (inBounds(x - xNum, y + yNum)
								&& temp == board[x - xNum][y + yNum]) {
							xNum++;
						}

						if (yNum == xNum - 1) {

							if (!inBounds(x + 1, y + yNum)
									&& !inBounds(x, y + (yNum + 1))) {
								checkCorner(x, y + yNum, num);

							} else if (!inBounds(x + 1, y + yNum)) {
								if (temp != board[x][y + (yNum + 1)]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x, y + (yNum + 1))) {

								if (temp != board[x + 1][y + yNum]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x + 1][y + yNum]
										&& temp != board[x][y + (yNum + 1)]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							}
						}

					}
					if (inBounds(x + yNum, y)) {
						xNum = 0;

						while (inBounds(x + xNum, y + yNum)
								&& temp == board[x + xNum][y + yNum]) {
							xNum++;
						}

						if (yNum == xNum - 1) {

							if (!inBounds(x - 1, y + yNum)
									&& !inBounds(x, y + (yNum + 1))) {
								checkCorner(x, y + yNum, num);

							} else if (!inBounds(x - 1, y + yNum)) {
								if (temp != board[x][y + (yNum + 1)]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							} else if (!inBounds(x, y + (yNum + 1))) {

								if (temp != board[x - 1][y + yNum]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							} else {
								if (temp != board[x - 1][y + yNum]
										&& temp != board[x][y + (yNum + 1)]) {
									checkCorner(x, y + yNum, num);
								} else {
									corner = false;
								}
							}
						}
					}

					right = false;
				}

			}

			if (sides == 2) {

				if (up && left) {

					if (NE && NW && !SE && SW) {
						checkCorner(x - 1, y + 1, num);
					}

					// while (inBounds(x - yNum, y - xNum)
					// && temp == board[x + yNum][y + xNum]) {
					// xNum++;
					// }

					while (inBounds(x, y - xNum) && temp == board[x][y - xNum]) {
						xNum++;
					}
					while (inBounds(x - yNum, y) && temp == board[x - yNum][y]) {
						yNum++;
					}

					if (xNum == yNum) {
						if (xNum < 3 || yNum < 3) {
							// corner = false;
						} else {
							count = ((xNum + yNum) - 1);
							if (temp == board[x - 1][y - 1]) {
								count++;
							}
							if (temp == board[x - 2][y - 1]
									&& temp == board[x - 1][y - 2]) {
								count += 2;
							}
							if (count == num) {
								corner = true;
							} else {
								corner = false;
							}
						}

					}
					// else {
					// corner = false;
					// }

				} else if (up && right) {

					if (NE && NW && SE && !SW) {
						checkCorner(x - 1, y - 1, num);
					}

					while (inBounds(x, y + xNum) && temp == board[x][y + xNum]) {
						xNum++;
					}
					while (inBounds(x - yNum, y) && temp == board[x - yNum][y]) {
						yNum++;
					}

					if (xNum == yNum) {
						if (xNum < 3 || yNum < 3) {
							// corner = false;
						} else {
							count = ((xNum + yNum) - 1);
							if (temp == board[x - 1][y + 1]) {
								count++;
							}
							if (temp == board[x - 2][y + 1]
									&& temp == board[x - 1][y + 2]) {
								count += 2;
							}
							if (count == num) {
								corner = true;
							} else {
								corner = false;
							}
						}

					}
					// else {
					// corner = false;
					// }

				} else if (down && left) {

					// if (!NE && !NW && !SE && !SW){
					//
					// }
					if (!NE && NW && SE && SW) {
						checkCorner(x - 1, y - 1, num);
					}

					while (inBounds(x, y - xNum) && temp == board[x][y - xNum]) {
						xNum++;
					}
					while (inBounds(x + yNum, y) && temp == board[x + yNum][y]) {
						yNum++;
					}

					// System.out.println(x);
					// System.out.println(y);
					// System.out.println(BDWIDTH);
					// System.out.println(BDHEIGHT);

					if (xNum == yNum) {
						if (xNum < 3 || yNum < 3) {
							// corner = false;
						} else {
							count = ((xNum + yNum) - 1);
							if (temp == board[x + 1][y - 1]) {
								count++;
							}
							if (temp == board[x + 2][y - 1]
									&& temp == board[x + 1][y - 2]) {
								count += 2;
							}
							if (count == num) {
								corner = true;
							} else {
								corner = false;
							}
						}

					}
					// else {
					// corner = false;
					// }

				} else if (down && right) {

					if (NE && !NW && SE && SW) {
						checkCorner(x + 1, y - 1, num);
					}

					while (inBounds(x, y + xNum) && temp == board[x][y + xNum]) {
						xNum++;
					}
					while (inBounds(x + yNum, y) && temp == board[x + yNum][y]) {
						yNum++;
					}

					if (xNum == yNum) {
						if (xNum < 3 || yNum < 3) {
							// corner = false;
						} else {
							count = ((xNum + yNum) - 1);
							if (temp == board[x + 1][y + 1]) {
								count++;
							}
							if (temp == board[x + 2][y + 1]
									&& temp == board[x + 1][y + 2]) {
								count += 2;
							}
							if (count == num) {
								corner = true;
							} else {
								corner = false;
							}
						}

					}
					// else {
					// corner = false;
					// }

				}

			}
			if (sides == 3) {

				if (!up) {
					if (y - 1 >= 0) {
						checkCorner(x, y - 1, num);
					} else {
						corner = false;
					}
				} else if (!down) {
					if (y - 1 >= 0) {
						checkCorner(x, y - 1, num);
					} else {
						corner = false;
					}
				} else if (!left) {
					if (x - 1 >= 0) {
						checkCorner(x - 1, y, num);
					} else {
						corner = false;
					}
				} else if (!right) {
					if (x - 1 >= 0) {
						checkCorner(x - 1, y, num);
					} else {
						corner = false;
					}
				}

			}

			if (sides == 4) {
				// fix error when possible corner is irregular, Stack Overflow
				if (x - 1 >= 0 && y + 1 < BDWIDTH && y - 1 >= 0
						&& x + 1 < BDHEIGHT) {
					if (temp != board[x - 1][y - 1]) {
						checkCorner(x - 1, y, num);
					} else if (temp != board[x + 1][y + 1]) {
						checkCorner(x + 1, y, num);
					} else if (temp != board[x - 1][y + 1]) {
						checkCorner(x - 1, y, num);
					} else if (temp != board[x + 1][y - 1]) {
						checkCorner(x + 1, y, num);
					}
				} else {
					corner = false;
				}
			}
		}
	}

	public void checkCross(int x, int y, int num) {

		if (num > 4 && (num - 1) % 4 == 0) {
			int tempNum = (num - 1) / 4;

			int sides = 0;
			int count = 0;
			int temp = board[x][y];
			int numX = 0;
			int numY = 0;

			int tempCount = 0;

			corner = false;

			boolean left = false;
			boolean right = false;
			boolean up = false;
			boolean down = false;

			boolean NE = false;
			boolean NW = false;
			boolean SE = false;
			boolean SW = false;

			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (inBounds(x - 1, y - 1) && temp == board[x - 1][y - 1]) {
				NW = true;
			}
			if (inBounds(x + 1, y - 1) && temp == board[x + 1][y - 1]) {
				SW = true;
			}
			if (inBounds(x + 1, y + 1) && temp == board[x + 1][y + 1]) {
				SE = true;
			}
			if (inBounds(x - 1, y + 1) && temp == board[x - 1][y + 1]) {
				NE = true;
			}

			if (num > 4) {

				if (sides == 1) {

					if (up) {
						while (inBounds(x - numX, y)
								&& temp == board[x - numX][y]) {
							if (inBounds(x - (numX + 1), y)
									&& temp == board[x - (numX + 1)][y]
									&& inBounds(x - numX, y + 1)
									&& temp == board[x - numX][y + 1]
									&& inBounds(x - numX, y - 1)
									&& temp == board[x - numX][y - 1]) {

								checkCross(x - numX, y, num);

							}
							numX++;
						}

					} else if (down) {
						while (inBounds(x + numX, y)
								&& temp == board[x + numX][y]) {
							if (inBounds(x + (numX + 1), y)
									&& temp == board[x + (numX + 1)][y]
									&& inBounds(x + numX, y + 1)
									&& temp == board[x + numX][y + 1]
									&& inBounds(x + numX, y - 1)
									&& temp == board[x + numX][y - 1]) {

								checkCross(x + numX, y, num);

							}
							numX++;
						}

					} else if (left) {
						while (inBounds(x, y - numY)
								&& temp == board[x][y - numY]) {
							if (inBounds(x, y - (numY + 1))
									&& temp == board[x][y - (numY + 1)]
									&& inBounds(x + 1, y - numY)
									&& temp == board[x + 1][y - numY]
									&& inBounds(x - 1, y - numY)
									&& temp == board[x - 1][y - numY]) {

								checkCross(x, y - numY, num);

							}
							numY++;
						}

					} else if (right) {
						while (inBounds(x, y + numY)
								&& temp == board[x][y + numY]) {
							if (inBounds(x, y + (numY + 1))
									&& temp == board[x][y + (numY + 1)]
									&& inBounds(x + 1, y + numY)
									&& temp == board[x + 1][y + numY]
									&& inBounds(x - 1, y + numY)
									&& temp == board[x - 1][y + numY]) {

								checkCross(x, y + numY, num);

							}
							numY++;
						}

					}
				} else if (sides == 2) {

					if (up && down) {
						if (NE && NW) {
							if (inBounds(x - 1, y) && temp == board[x - 1][y]) {
								checkCross(x - 1, y, num);
							}

						} else if (SE && SW) {
							if (inBounds(x + 1, y) && temp == board[x + 1][y]) {
								checkCross(x + 1, y, num);
							}
						} else {

							while (inBounds(x - numX, y)
									&& inBounds(x + numY, y)) {

								if (temp == board[x - numX][y]
										|| temp == board[x + numY][y]) {

									if (inBounds(x + (numX + 1), y)
											&& temp == board[x + (numX + 1)][y]
											&& inBounds(x + numX, y + 1)
											&& temp == board[x + numX][y + 1]
											&& inBounds(x + numX, y - 1)
											&& temp == board[x + numX][y - 1]) {

										checkCross(x + numX, y, num);

									} else if (inBounds(x - (numY + 1), y)
											&& temp == board[x - (numY + 1)][y]
											&& inBounds(x - numY, y + 1)
											&& temp == board[x - numY][y + 1]
											&& inBounds(x - numY, y - 1)
											&& temp == board[x - numY][y - 1]) {

										checkCross(x - numY, y, num);

									}

									if (inBounds(x + numY, y)
											&& temp == board[x + numY][y]) {
										numY++;
									}
									if (inBounds(x - numX, y)
											&& temp == board[x - numX][y]) {
										numX++;
									}
								} else {
									break;
								}
							}
						}

					} else if (left && right) {
						if (NE && SE) {
							if (inBounds(x, y + 1) && temp == board[x][y + 1]) {
								checkCross(x, y + 1, num);
							}

						} else if (NW && SW) {
							if (inBounds(x, y - 1) && temp == board[x][y - 1]) {
								checkCross(x, y - 1, num);
							}
						} else {
							while (inBounds(x, y - numX)
									&& inBounds(x, y + numY)) {

								if (temp == board[x][y - numX]
										|| temp == board[x][y + numY]) {

									if (inBounds(x, y + (numX + 1))
											&& temp == board[x][y + (numX + 1)]
											&& inBounds(x + 1, y + numX)
											&& temp == board[x + 1][y + numX]
											&& inBounds(x - 1, y + numX)
											&& temp == board[x - 1][y + numX]) {

										checkCross(x, y + numX, num);

									} else if (inBounds(x, y - (numY + 1))
											&& temp == board[x][y - (numY + 1)]
											&& inBounds(x + 1, y - numY)
											&& temp == board[x + 1][y - numY]
											&& inBounds(x - 1, y - numY)
											&& temp == board[x - 1][y - numY]) {

										checkCross(x, y - numY, num);

									}

									if (temp == board[x][y + numY]) {
										numY++;
									}
									if (temp == board[x][y - numX]) {
										numX++;
									}
								} else {
									break;
								}

							}

						}
					}

				} else if (sides == 4) {
					while (inBounds(x + numX, y) && temp == board[x + numX][y]) {
						tempCount++;
						count++;
						numX++;
					}

					for (int i = 1; i < tempCount; i++) {
						if (inBounds(x - i, y) && temp == board[x - i][y]) {
							count++;
						}
						if (inBounds(x, y - i) && temp == board[x][y - i]) {
							count++;
						}
						if (inBounds(x, y + i) && temp == board[x][y + i]) {
							count++;
						}
					}

					if (count == num) {
						cross = true;
					}
				}
			}
		}
	}

	public void checkL(int x, int y, int num) {

		if (num > 4) {
			int tempNum = (num - 1) / 2;
			int sides = 0;
			int count = 1;
			int temp = board[x][y];

			int numX = 0;
			int numY = 0;

			L = false;

			boolean left = false;
			boolean right = false;
			boolean up = false;
			boolean down = false;

			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (sides == 1) {

				if (up) {
					while (inBounds(x - numX, y) && temp == board[x - numX][y]) {
						numX++;
					}
					numX -= 1;
					if (inBounds(x - numX, y + 1)
							&& temp == board[x - numX][y + 1]
							|| inBounds(x - numX, y - 1)
							&& temp == board[x - numX][y - 1]) {

						checkL(x - numX, y, num);
					}

				} else if (down) {
					while (inBounds(x + numX, y) && temp == board[x + numX][y]) {
						numX++;
					}
					numX -= 1;
					if (inBounds(x + numX, y + 1)
							&& temp == board[x + numX][y + 1]
							|| inBounds(x + numX, y - 1)
							&& temp == board[x + numX][y - 1]) {
						checkL(x + numX, y, num);
					}
				} else if (left) {
					while (inBounds(x, y - numY) && temp == board[x][y - numY]) {
						numY++;
					}
					numY -= 1;
					if (inBounds(x + 1, y - numY)
							&& temp == board[x + 1][y - numY]
							|| inBounds(x - 1, y - numY)
							&& temp == board[x - 1][y - numY]) {
						checkL(x, y - numY, num);
					}
				} else if (right) {
					while (inBounds(x, y + numY) && temp == board[x][y + numY]) {
						numY++;
					}
					numY -= 1;
					if (inBounds(x + 1, y + numY)
							&& temp == board[x + 1][y + numY]
							|| inBounds(x - 1, y + numY)
							&& temp == board[x - 1][y + numY]) {
						checkL(x, y + numY, num);
					}
				}
			} else if (sides == 2) {

				if (up && down) {

					while (inBounds(x - numX, y) && temp == board[x - numX][y]) {
						numX++;
					}

					while (inBounds(x + numY, y) && temp == board[x + numY][y]) {
						numY++;
					}

					numX -= 1;
					numY -= 1;
					checkL(x - numX, y, num);
					checkL(x + numY, y, num);

				} else if (left && right) {

					while (inBounds(x, y - numX) && temp == board[x][y - numX]) {
						numX++;
					}

					while (inBounds(x, y + numY) && temp == board[x][y + numY]) {
						numY++;
					}

					numX -= 1;
					numY -= 1;
					checkL(x, y - numX, num);
					checkL(x, y + numY, num);

				} else if (up && left) {

					while (inBounds(x - numX, y) && inBounds(x, y - numY)) {

						if (temp == board[x - numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y - numY]) {
							numY++;
						} else {
							break;
						}
					}

				} else if (up && right) {

					while (inBounds(x - numX, y) && inBounds(x, y + numY)) {

						if (temp == board[x - numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y + numY]) {
							numY++;
						} else {
							break;
						}
					}

				} else if (down && left) {

					while (inBounds(x + numX, y) && inBounds(x, y - numY)) {

						if (temp == board[x + numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y - numY]) {
							numY++;
						} else {
							break;
						}
					}
				} else if (down && right) {

					while (inBounds(x + numX, y) && inBounds(x, y + numY)) {

						if (temp == board[x + numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y + numY]) {
							numY++;
						} else {
							break;
						}
					}
				}
				if (num == (numX + numY) - 1) {
					L = true;
				}
			}
		}
	}

	public void checkT(int x, int y, int num) {

		if (num > 4) {
			int sides = 0;
			int count = 1;
			int temp = board[x][y];
			int tempNum = 0;
			int cornerCount = 0;

			int numX = 0;
			int numY = 0;
			int numZ = 0;

			T = false;

			boolean left = false;
			boolean right = false;
			boolean up = false;
			boolean down = false;

			boolean NE = false;
			boolean NW = false;
			boolean SE = false;
			boolean SW = false;

			if (inBounds(x + 1, y)) {
				if (board[x + 1][y] == temp) {
					sides++;
					down = true;
				}
			}
			if (inBounds(x - 1, y)) {
				if (board[x - 1][y] == temp) {
					sides++;
					up = true;
				}
			}
			if (inBounds(x, y + 1)) {
				if (board[x][y + 1] == temp) {
					sides++;
					right = true;
				}
			}
			if (inBounds(x, y - 1)) {
				if (board[x][y - 1] == temp) {
					sides++;
					left = true;
				}
			}

			if (inBounds(x - 1, y - 1) && temp == board[x - 1][y - 1]) {
				NW = true;
				cornerCount++;
			}
			if (inBounds(x + 1, y - 1) && temp == board[x + 1][y - 1]) {
				SW = true;
				cornerCount++;
			}
			if (inBounds(x + 1, y + 1) && temp == board[x + 1][y + 1]) {
				SE = true;
				cornerCount++;
			}
			if (inBounds(x - 1, y + 1) && temp == board[x - 1][y + 1]) {
				NE = true;
				cornerCount++;
			}

			// if (sides == 4 || cornerCount >= 3){
			if (sides == 4) {
				return;
			} else if (sides == 1) {

				if (up) {

					if (NE && NW) {
						return;
					}
					if (NE && !NW || !NE && NW) {
						checkT(x - 1, y, num);
					}
					while (inBounds(x - tempNum, y)
							&& temp == board[x - tempNum][y]) {
						tempNum++;
					}
					tempNum--;

					if (inBounds(x - tempNum, y - 1)
							&& temp == board[x - tempNum][y - 1]
							|| inBounds(x - tempNum, y + 1)
							&& temp == board[x - tempNum][y + 1]) {
						checkT(x - tempNum, y, num);
					} else {
						// if the tile tapped was in the top of the T the
						// tempNum must be an odd number
						if ((tempNum + 1) % 2 != 0) {
							checkT(x - (tempNum / 2), y, num);
						}
					}

				} else if (down) {

					if (SE && SW) {
						return;
					}
					if (SE && !SW || !SE && SW) {
						checkT(x + 1, y, num);
					}
					while (inBounds(x + tempNum, y)
							&& temp == board[x + tempNum][y]) {
						tempNum++;
					}
					tempNum--;

					if (inBounds(x + tempNum, y - 1)
							&& temp == board[x + tempNum][y - 1]
							|| inBounds(x + tempNum, y + 1)
							&& temp == board[x + tempNum][y + 1]) {
						checkT(x + tempNum, y, num);
					} else {

						// System.out.println(tempNum % 2);
						// System.out.println((tempNum + 1) % 2);

						// if the tile tapped was in the top of the T the
						// tempNum must be an odd number
						if ((tempNum + 1) % 2 != 0) {
							checkT(x + (tempNum / 2), y, num);
						}
					}

				} else if (left) {

					if (SW && NW) {
						return;
					}
					if (SW && !NW || !SW && NW) {
						checkT(x, y - 1, num);
					}
					while (inBounds(x, y - tempNum)
							&& temp == board[x][y - tempNum]) {
						tempNum++;
					}
					tempNum--;

					if (inBounds(x - 1, y - tempNum)
							&& temp == board[x - 1][y - tempNum]
							|| inBounds(x + 1, y - tempNum)
							&& temp == board[x + 1][y - tempNum]) {
						checkT(x, y - tempNum, num);
					} else {
						// if the tile tapped was in the top of the T the
						// tempNum must be an odd number
						if ((tempNum + 1) % 2 != 0) {
							checkT(x, y - (tempNum / 2), num);
						}
					}

				} else if (right) {

					if (SE && NE) {
						return;
					}
					if (SE && !NE || !SE && NE) {
						checkT(x, y + 1, num);
					}
					while (inBounds(x, y + tempNum)
							&& temp == board[x][y + tempNum]) {
						tempNum++;
					}
					tempNum--;

					if (inBounds(x - 1, y + tempNum)
							&& temp == board[x - 1][y + tempNum]
							|| inBounds(x + 1, y + tempNum)
							&& temp == board[x + 1][y + tempNum]) {
						checkT(x, y + tempNum, num);
					} else {
						// if the tile tapped was in the top of the T the
						// tempNum must be an odd number
						if ((tempNum + 1) % 2 != 0) {
							checkT(x, y + (tempNum / 2), num);
						}
					}
				}
			} else if (sides == 2) {

				if (up && down) {

					if (NE || NW && !SE && !SW) {
						checkT(x - 1, y, num);
					} else if (!NE && !NW && SE || SW) {
						checkT(x + 1, y, num);
					} else {
						while (inBounds(x - numX, y) && inBounds(x + numY, y)) {
							if (temp == board[x - numX][y]) {
								numX++;
							} else {
								break;
							}
							if (temp == board[x + numY][y]) {
								numY++;
							} else {
								break;
							}
						}
					}
					if (inBounds(x - numX, y + 1)
							&& temp == board[x - numX][y + 1]
							&& inBounds(x - numX, y - 1)
							&& temp == board[x - numX][y - 1]) {

						checkT(x - numX, y, num);

					} else if (inBounds(x + numY, y + 1)
							&& temp == board[x + numY][y + 1]
							&& inBounds(x + numY, y - 1)
							&& temp == board[x + numY][y - 1]) {
						checkT(x + numY, y, num);
					}

				} else if (left && right) {

					if (NE || SE && !NW && !SW) {
						checkT(x, y + 1, num);
					} else if (!NE && !SE && NW || SW) {
						checkT(x, y - 1, num);
					} else {
						while (inBounds(x, y - numX) && inBounds(x, y + numY)) {

							if (temp == board[x][y - numX]) {
								numX++;
							} else {
								break;
							}
							if (temp == board[x][y + numY]) {
								numY++;
							} else {
								break;
							}
						}

					}
					if (inBounds(x + 1, y - numX)
							&& temp == board[x + 1][y - numX]
							&& inBounds(x - 1, y - numX)
							&& temp == board[x - 1][y - numX]) {

						checkT(x, y - numX, num);

					} else if (inBounds(x + 1, y + numY)
							&& temp == board[x + 1][y + numY]
							&& inBounds(x - 1, y + numY)
							&& temp == board[x - 1][y + numY]) {
						checkT(x, y + numY, num);
					}

				}
			} else if (sides == 3) {

				if (!up && down && left && right) {

					while (inBounds(x, y - numX) && inBounds(x, y + numY)) {

						if (temp == board[x][y - numX]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y + numY]) {
							numY++;
						} else {
							break;
						}
					}
					while (inBounds(x + numZ, y) && temp == board[x + numZ][y]) {
						numZ++;
					}

				} else if (up && !down && left && right) {

					while (inBounds(x, y - numX) && inBounds(x, y + numY)) {

						if (temp == board[x][y - numX]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x][y + numY]) {
							numY++;
						} else {
							break;
						}
					}
					while (inBounds(x - numZ, y) && temp == board[x - numZ][y]) {
						numZ++;
					}

				} else if (up && down && !left && right) {

					while (inBounds(x - numX, y) && inBounds(x + numY, y)) {

						if (temp == board[x - numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x + numY][y]) {
							numY++;
						} else {
							break;
						}
					}
					while (inBounds(x, y + numZ) && temp == board[x][y + numZ]) {
						numZ++;
					}
				} else if (up && down && left && !right) {

					while (inBounds(x - numX, y) && inBounds(x + numY, y)) {

						if (temp == board[x - numX][y]) {
							numX++;
						} else {
							break;
						}
						if (temp == board[x + numY][y]) {
							numY++;
						} else {
							break;
						}
					}
					while (inBounds(x, y - numZ) && temp == board[x][y - numZ]) {
						numZ++;
					}
				}

				if (num > 4) {

					if (numX != numY || numX > numZ || numY > numZ) {
						return;
					}

					if (num == ((numX + numY) - 1) + (numZ - 1)) {
						T = true;
					}
				} else {
					if (num == (numX + numY) + numZ) {
						T = true;
					}
				}
			}
		}
	}

	public boolean CheckSingle(Point p) {
		int count = countTiles(p);
		if (count > 1) {
			return false;
		} else {
			return true;
		}
	}

	public void checkBounds(int x, int y) {
		if (x > 0 && x < BDWIDTH - 1) {
			if (y > 0 && y < BDHEIGHT - 1) {
				inBounds = true;
			} else {
				inBounds = false;
			}
		} else {
			inBounds = false;
		}
	}

	public boolean inBounds(int x, int y) {
		if (x >= 0 && x < BDHEIGHT && y >= 0 && y < BDWIDTH) {
			return true;
		} else {
			return false;
		}
	}

	public void CalculateScore(int s, String str) {

		// 0.7684x2 + 0.511x

		int tempScore = (int) ((0.7684 * (Math.pow(s, 2))) + (0.511 * s));
		double tempComboScore = (0.7942 * (Math.pow(combo, 2)));
		int tempShapeScore = (int) ((tempScore * ((s + 1) / 2)) + (tempScore * tempComboScore));

		// System.out.println("score: " + ((tempScore * ((s + 1) / 2))));
		// System.out.println("Combo: " + combo);
		// System.out.println("comboScore: " + tempComboScore);
		// System.out.println("temp and combo: " + (tempScore *
		// tempComboScore));

		prevScore = score;
		prevBonusPoints = bonusPoints;

		if (str == "") {
			combo = 0;

			// if(boardCleared){
			// tempScore = 0;
			// }

			score += tempScore;

		} else if (str == "square" || str == "corner" || str == "cross") {
			score += tempShapeScore;
			AddBonusPoints(s, 1);
			combo++;
		} else if (str == "line" || str == "stairs") {
			score += tempShapeScore;
			AddBonusPoints(s, 2);
			combo++;
		} else if (str == "l" || str == "t") {
			score += tempShapeScore;
			AddBonusPoints(s, 2);
			combo++;
		}

		scoreStr = "" + (score - prevScore);

		if (bonusPoints - prevBonusPoints > 0) {
			bonusPtStr = "+" + (bonusPoints - prevBonusPoints)
					+ " Bonus Points!";

		} else {
			bonusPtStr = "";

		}
		if (combo > 1) {
			comboStr = "Combo x " + combo + "!";
		} else {
			comboStr = "";
		}

		if (tileTypeCount() == 0) {
			// scoreStr = "";
			// bonusPtStr = "";
			// comboStr = "";
			score += 1000;
			bonusPoints += 100;
		}

		saveGame();

	}

	public void AddBonusPoints(int num, int type) {

		if (gameMode != infinity) {
			// bonusPoints += num;
			if (type == 1) {
				bonusPoints += (int) ((.7 * num) + .6);
			} else if (type == 2) {
				bonusPoints += (num - 2);
			} else if (type == 3) {
				bonusPoints += (int) ((.45 * num) + .85);
			}
		}
	}

	public void SubtractBonusPoints(int num) {
		bonusPoints -= num;
	}

	public int tileTypeCount() {
		int temp = 0;
		int one = 0;
		int two = 0;
		int three = 0;
		int four = 0;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] == 1) {
					one = 1;
				} else if (board[x][y] == 2) {
					two = 1;
				} else if (board[x][y] == 3) {
					three = 1;
				} else if (board[x][y] == 4) {
					four = 1;
				}
			}
		}
		temp = (one + two + three + four);
		return temp;
	}

	public int CheckZeros() {
		int zeroCount = 0;
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (gameMode == criticalMass || gameMode == infinity) {
					if (board[x][y] == 0) {
						zeroCount++;
					}
				} else if (gameMode == classic || gameMode == gravity) {
					if (direction == down) {
						if (x < BDHEIGHT - 1) {
							if (board[x][y] != 0 && board[x + 1][y] == 0) {
								zeroCount++;
							}
						}
					} else if (direction == up) {
						if (x > 0) {
							if (board[x][y] != 0 && board[x - 1][y] == 0) {
								zeroCount++;
							}
						}
					} else if (direction == left) {
						if (y > 0) {
							if (board[x][y] != 0 && board[x][y - 1] == 0) {
								zeroCount++;
							}
						}
					} else if (direction == right) {
						if (y < BDWIDTH - 1) {
							if (board[x][y] != 0 && board[x][y + 1] == 0) {
								zeroCount++;
							}
						}
					}
				}
				if (gameMode == classic) {
					if (y > 0) {
						if (x == 0 && colEmpty(y) && colEmpty(y - 1) == false)
							zeroCount++;
					}
				}

			}
		}
		return zeroCount;
	}

	public void Update() {

		// down
		if (direction == down) {

			for (int x = BDHEIGHT - 1; x >= 0; x--) {
				for (int y = BDWIDTH - 1; y >= 0; y--) {
					if (board[x][y] == 0 && x == 0) {
						if (gameMode == criticalMass || gameMode == infinity) {
							board[x][y] = generateRandom();
						} else
							board[x][y] = 0;
					}
					if (x > 0) {
						if (board[x][y] == 0 && board[x - 1][y] != 0) {
							board[x][y] = board[x - 1][y];
							board[x - 1][y] = 0;
						}
					}
					if (gameMode == classic) {
						if (y > 0) {
							if (colEmpty(y) && colEmpty(y - 1) == false) {
								for (int i = BDHEIGHT - 1; i >= 0; i--) {
									board[i][y] = board[i][y - 1];
									board[i][y - 1] = 0;
								}
							}
						}

					}
				}
			}

		}
		// up
		else if (direction == up) {

			for (int x = 0; x < BDHEIGHT; x++) {
				for (int y = 0; y < BDWIDTH; y++) {
					if (board[x][y] == 0 && x == BDHEIGHT - 1) {
						if (gameMode == criticalMass || gameMode == infinity) {
							board[x][y] = generateRandom();
						} else
							board[x][y] = 0;
					}
					if (x < BDHEIGHT - 1) {
						if (board[x][y] == 0 && board[x + 1][y] != 0) {
							board[x][y] = board[x + 1][y];
							board[x + 1][y] = 0;
						}
					}
				}
			}

		}
		// left
		else if (direction == left) {

			for (int x = BDHEIGHT - 1; x >= 0; x--) {
				for (int y = 0; y < BDWIDTH; y++) {
					if (board[x][y] == 0 && y == BDWIDTH - 1) {
						if (gameMode == criticalMass || gameMode == infinity) {
							board[x][y] = generateRandom();
						} else
							board[x][y] = 0;
					}
					if (y < BDWIDTH - 1) {
						if (board[x][y] == 0 && board[x][y + 1] != 0) {
							board[x][y] = board[x][y + 1];
							board[x][y + 1] = 0;
						}
					}
				}
			}

		}
		// right
		else if (direction == right) {

			for (int x = 0; x < BDHEIGHT; x++) {
				for (int y = BDWIDTH - 1; y >= 0; y--) {
					if (board[x][y] == 0 && y == 0) {
						if (gameMode == criticalMass || gameMode == infinity) {
							board[x][y] = generateRandom();
						} else
							board[x][y] = 0;
					}
					if (y > 0) {
						if (board[x][y] == 0 && board[x][y - 1] != 0) {
							board[x][y] = board[x][y - 1];
							board[x][y - 1] = 0;
						}
					}

				}
			}

		}

	}

	public void fillBoard() {
		moveCount = 0;

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] == 0) {
					board[x][y] = generateRandom();
				} else {
					board[x][y] = 5;
				}
			}
		}
	}

	public int tilesLeft() {
		int count = 0;
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] != 0) {
					count++;
				}
			}
		}
		return count;
	}

	public float tilesRemaining() {
		int count = 0;
		int solidCount = 0;
		float percentage = 0;
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] != 0 && board[x][y] != 5) {
					count++;
				} else if (board[x][y] == 5) {
					solidCount++;
				}
			}
		}
		percentage = ((float) count / (float) ((BDHEIGHT * BDWIDTH) - solidCount));
		return percentage;
	}

	public void checkCreateRow() {
		if (moveCount == rowDisplayCount - 1) {
			createRow();
		}
	}

	public void checkMoveCount() {

		if (tilesRemaining() > .65) {
			moveCount = 0;
			// System.out.println(tilesRemaining());
		}

		if (moveCount == rowDropCount) {
			dropRow = true;
		}

		if (moveCount >= rowDisplayCount && moveCount < rowDropCount) {
			displayRow = true;
		}

		if (moveCount >= rowDropCount && !dropRow) {
			displayRow = false;
			moveCount = 0;
		}

		if (moveCount < rowDisplayCount) {
			displayRow = false;
			dropRow = false;
		}

	}

	public boolean rowEmpty() {
		int count = 0;

		for (int i = 0; i < BDWIDTH; i++) {
			if (boardRow[i] == 0) {
				count++;
			}
		}
		if (count == BDWIDTH) {
			return true;
		}
		return false;
	}

	public void mergeRow() {
		// prevBoardRow = boardRow;
		int temp = 0;
		// int count = 0;

		// for (int i = 0; i < BDWIDTH; i++) {
		// prevBoardRow[i] = boardRow[i];
		// if (boardRow[i] != 0 && board[0][i] == 0) {
		//
		// while (inBounds(0, i + count) && board[0][i + count] == 0) {
		// count++;
		// }
		//
		// board[0][i + count - 1] = boardRow[i];
		// boardRow[i] = 0;
		//
		// // board[0][i] = boardRow[i];
		// // boardRow[i] = 0;
		// }
		// }

		for (int i = 0; i < BDWIDTH; i++) {
			if (boardRow[i] == 0) {
				temp++;
			}
		}
		if (temp == BDWIDTH) {
			dropRow = false;
			displayRow = false;
			// prevMoveCount = moveCount;
			moveCount = 0;
		}

		// DropTiles();

	}

	public void removeJoiningTiles(Point p, int val) {
		int x = p.x;
		int y = p.y;
		boardVal = val;
		setBoardVal(x, y, 0);

		if (x == 0 && y == 0 || x == BDHEIGHT - 1 && y == 0 || x == 0
				&& y == BDWIDTH - 1 || x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
			if (x == 0 && y == 0) {
				if (board[x + 1][y] == boardVal) {
					removeJoiningTiles(new Point(x + 1, y), val);
				}
				if (board[x][y + 1] == boardVal) {
					removeJoiningTiles(new Point(x, y + 1), val);
				}
			} else if (x == BDHEIGHT - 1 && y == 0) {
				if (board[x - 1][y] == boardVal) {
					removeJoiningTiles(new Point(x - 1, y), val);
				}
				if (board[x][y + 1] == boardVal) {
					removeJoiningTiles(new Point(x, y + 1), val);
				}
			} else if (x == 0 && y == BDWIDTH - 1) {
				if (board[x + 1][y] == boardVal) {
					removeJoiningTiles(new Point(x + 1, y), val);
				}
				if (board[x][y - 1] == boardVal) {
					removeJoiningTiles(new Point(x, y - 1), val);
				}
			} else if (x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
				if (board[x - 1][y] == boardVal) {
					removeJoiningTiles(new Point(x - 1, y), val);
				}
				if (board[x][y - 1] == boardVal) {
					removeJoiningTiles(new Point(x, y - 1), val);
				}
			}
		} else if (x == 0 || x == BDHEIGHT - 1 || y == 0 || y == BDWIDTH - 1) {
			if (x == 0) {
				if (board[x + 1][y] == boardVal) {
					removeJoiningTiles(new Point(x + 1, y), val);
				}
				if (board[x][y + 1] == boardVal) {
					removeJoiningTiles(new Point(x, y + 1), val);
				}
				if (board[x][y - 1] == boardVal) {
					removeJoiningTiles(new Point(x, y - 1), val);
				}
			} else if (x == BDHEIGHT - 1) {
				if (board[x - 1][y] == boardVal) {
					removeJoiningTiles(new Point(x - 1, y), val);
				}
				if (board[x][y + 1] == boardVal) {
					removeJoiningTiles(new Point(x, y + 1), val);
				}
				if (board[x][y - 1] == boardVal) {
					removeJoiningTiles(new Point(x, y - 1), val);
				}
			} else if (y == 0) {
				if (board[x + 1][y] == boardVal) {
					removeJoiningTiles(new Point(x + 1, y), val);
				}
				if (board[x - 1][y] == boardVal) {
					removeJoiningTiles(new Point(x - 1, y), val);
				}
				if (board[x][y + 1] == boardVal) {
					removeJoiningTiles(new Point(x, y + 1), val);
				}
			} else if (y == BDWIDTH - 1) {
				if (board[x + 1][y] == boardVal) {
					removeJoiningTiles(new Point(x + 1, y), val);
				}
				if (board[x - 1][y] == boardVal) {
					removeJoiningTiles(new Point(x - 1, y), val);
				}
				if (board[x][y - 1] == boardVal) {
					removeJoiningTiles(new Point(x, y - 1), val);
				}
			}
		} else {
			if (board[x + 1][y] == boardVal) {
				removeJoiningTiles(new Point(x + 1, y), val);
			}
			if (board[x - 1][y] == boardVal) {
				removeJoiningTiles(new Point(x - 1, y), val);
			}
			if (board[x][y + 1] == boardVal) {
				removeJoiningTiles(new Point(x, y + 1), val);
			}
			if (board[x][y - 1] == boardVal) {
				removeJoiningTiles(new Point(x, y - 1), val);
			}
		}
	}

	public int countCriticalMass(Point p) {
		int x = p.x;
		int y = p.y;
		int count = 1;
		boardVal = board[x][y];

		if (boardVal == 5) {
			setBoardVal(x, y, 0);

			if (x == 0 && y == 0 || x == BDHEIGHT - 1 && y == 0 || x == 0
					&& y == BDWIDTH - 1 || x == BDHEIGHT - 1
					&& y == BDWIDTH - 1) {
				if (x == 0 && y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countCriticalMass(new Point(x, y + 1));
					}
				} else if (x == BDHEIGHT - 1 && y == 0) {
					if (board[x - 1][y] == boardVal) {
						count += countCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countCriticalMass(new Point(x, y + 1));
					}
				} else if (x == 0 && y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += countCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countCriticalMass(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
					if (board[x - 1][y] == boardVal) {
						count += countCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countCriticalMass(new Point(x, y - 1));
					}
				}
			} else if (x == 0 || x == BDHEIGHT - 1 || y == 0
					|| y == BDWIDTH - 1) {
				if (x == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countCriticalMass(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += countCriticalMass(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1) {
					if (board[x - 1][y] == boardVal) {
						count += countCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countCriticalMass(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += countCriticalMass(new Point(x, y - 1));
					}
				} else if (y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += countCriticalMass(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += countCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += countCriticalMass(new Point(x, y + 1));
					}
				} else if (y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += countCriticalMass(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += countCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += countCriticalMass(new Point(x, y - 1));
					}
				}
			} else {
				if (board[x + 1][y] == boardVal) {
					count += countCriticalMass(new Point(x + 1, y));
				}
				if (board[x - 1][y] == boardVal) {
					count += countCriticalMass(new Point(x - 1, y));
				}
				if (board[x][y + 1] == boardVal) {
					count += countCriticalMass(new Point(x, y + 1));
				}
				if (board[x][y - 1] == boardVal) {
					count += countCriticalMass(new Point(x, y - 1));
				}
			}
		}
		return count;
	}

	public int tempCriticalMass(Point p) {
		int x = p.x;
		int y = p.y;
		int count = 0;
		boardVal = board[x][y];

		if (boardVal == 5 && !tempCritMass.contains(x + "," + y)) {
			count++;

			tempCritMass.add(x + "," + y);
			CMGroups.add(x + "," + y);

			if (x == 0 && y == 0 || x == BDHEIGHT - 1 && y == 0 || x == 0
					&& y == BDWIDTH - 1 || x == BDHEIGHT - 1
					&& y == BDWIDTH - 1) {
				if (x == 0 && y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y + 1));
					}
				} else if (x == BDHEIGHT - 1 && y == 0) {
					if (board[x - 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y + 1));
					}
				} else if (x == 0 && y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1 && y == BDWIDTH - 1) {
					if (board[x - 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y - 1));
					}
				}
			} else if (x == 0 || x == BDHEIGHT - 1 || y == 0
					|| y == BDWIDTH - 1) {
				if (x == 0) {
					if (board[x + 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x + 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y - 1));
					}
				} else if (x == BDHEIGHT - 1) {
					if (board[x - 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y + 1));
					}
					if (board[x][y - 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y - 1));
					}
				} else if (y == 0) {
					if (board[x + 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y + 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y + 1));
					}
				} else if (y == BDWIDTH - 1) {
					if (board[x + 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x + 1, y));
					}
					if (board[x - 1][y] == boardVal) {
						count += tempCriticalMass(new Point(x - 1, y));
					}
					if (board[x][y - 1] == boardVal) {
						count += tempCriticalMass(new Point(x, y - 1));
					}
				}
			} else {
				if (board[x + 1][y] == boardVal) {
					count += tempCriticalMass(new Point(x + 1, y));
				}
				if (board[x - 1][y] == boardVal) {
					count += tempCriticalMass(new Point(x - 1, y));
				}
				if (board[x][y + 1] == boardVal) {
					count += tempCriticalMass(new Point(x, y + 1));
				}
				if (board[x][y - 1] == boardVal) {
					count += tempCriticalMass(new Point(x, y - 1));
				}
			}

		}
		return count;
	}

	public boolean checkCriticalMass(String tempStr) {

		tempStr = tempStr.replace("[", ",");
		tempStr = tempStr.replace("]", ",");

		String[] str = tempStr.split(",");

		if ((Integer.parseInt(str[3]) / 100) >= showCMSize) {
			removeJoiningTiles(
					new Point(Integer.parseInt(str[4]),
							Integer.parseInt(str[5])), 5);
			int count = ((Integer.parseInt(str[3]) * critMassSize) / 100);
			// System.out.println(count);
			score += (count * 25);
			DropTiles();
			return true;
		} else {
			return false;
		}

	}

	public void replaceCMTiles() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] == 0) {
					board[x][y] = 5;
				}
			}
		}
	}

	public void CM(ArrayList<String> strArray, int num, Point pt) {

		String[] str;
		Stack<Integer> x = new Stack<Integer>();
		Stack<Integer> y = new Stack<Integer>();

		int tempX = 0;
		int tempY = 0;
		int tempPercent = 0;

		for (int i = 0; i < strArray.size(); i++) {
			str = strArray.get(i).split(",");

			x.push((Integer.parseInt(str[0]) * tilesize) + tileOriginY
					+ spacing);
			y.push((Integer.parseInt(str[1]) * tilesize) + tileOriginX
					+ spacing);

		}

		while (!x.isEmpty()) {
			tempX += x.peek();
			tempY += y.peek();

			x.pop();
			y.pop();
		}
		tempX = (tempX / num);
		tempY = (tempY / num);

		tempPercent = (int) (((float) num / critMassSize) * 100);

		critMassPercentages.push("[" + tempX + "," + tempY + "]" + tempPercent
				+ "[" + pt.x + "," + pt.y + "]");
	}

	public void CriticalMass() {
		int temp = 0;
		int count = 0;

		int i = 0;

		checkedTiles.clear();
		critMassPercentages.clear();
		tempCritMass.clear();

		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {

				if (board[x][y] == 5) {
					temp = 0;

					if (!tempCritMass.contains(x + "," + y)) {
						i = tempCriticalMass(new Point(x, y));

						if (((float) i / critMassSize) >= showCMSize) {

							checkedTiles.add(x + "," + y + "," + i);

							CM(CMGroups, i, new Point(x, y));
							CMGroups.clear();

						} else {
							CMGroups.clear();
						}
					}

				}
			}
		}
	}

	public void DropTiles() {
		int count = CheckZeros();
		while (count > 0) {
			Update();
			count = CheckZeros();
		}
	}

	public void addTile(int x, int y) {
		int temp = 0;

		if (!checkedTiles.contains(x + "," + y)) {
			checkedTiles.add(x + "," + y);

		}

	}

	public boolean colEmpty(int col) {

		int tempCount = 0;

		for (int x = BDHEIGHT - 1; x >= 0; x--) {
			if (board[x][col] == 0)
				tempCount++;
		}
		if (tempCount == BDHEIGHT) {
			return true;
		}

		return false;
	}

	public void updateUndo() {

	}

	public int generateRandom() {

		Random rand = new Random();
		int randomNum = rand.nextInt((randomMax - randomMin) + 1) + randomMin;
		return randomNum;
	}

	public int generateRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	// use this method to determine what rage the random numbers will have
	public void setRandomRange(int min, int max) {
		// default range
		randomMin = min;
		randomMax = max;
	}

	public void addUndo() {
		String rowStr = "";

		undoListStr = "";
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				undoListStr += board[x][y];
				undoListStr += ",";
			}
		}

		undoList.push(undoListStr);
		scoreUndoList.push((int) score);
		bonusPtUndoList.push(bonusPoints);

		if (gameMode == 1 && moveCount > rowDisplayCount - 1) {
			for (int i = 0; i < BDWIDTH; i++) {
				rowStr += boardRow[i];
				rowStr += ",";
			}
			undoRowList.push(rowStr);
		}
	}

	public void Undo() {
		String rowStr = "";

		if (gameMode == 1 && !undoRowList.isEmpty()) {
			rowStr = undoRowList.peek();
			undoRowList.pop();

			String[] undoRowArray = rowStr.split(",");

			for (int i = 0; i < BDWIDTH; i++) {
				boardRow[i] = Integer.parseInt(undoRowArray[i]);
			}
		}

		if (!undoList.isEmpty()) {

			// removes the top object from the undoList
			gameBoardStr = undoList.peek();
			undoList.pop();

			String[] undoArray = gameBoardStr.split(",");
			int i = 0;

			for (int x = 0; x < BDHEIGHT; x++) {
				for (int y = 0; y < BDWIDTH; y++) {
					board[x][y] = Integer.parseInt(undoArray[i]);
					i++;
				}
			}

			// if (gameMode == 1 && !undoRowList.isEmpty()) {
			// rowStr = undoRowList.peek();
			// undoRowList.pop();
			//
			// String[] undoRowArray = rowStr.split(",");
			//
			// for (int j = 0; j < BDWIDTH; j++) {
			// boardRow[j] = Integer.parseInt(undoRowArray[j]);
			// }
			// }

			// for (int b = 0; b < BDWIDTH; b++) {
			// boardRow[b] = prevBoardRow[b];
			// }

			score = scoreUndoList.peek();
			scoreUndoList.pop();

			bonusPoints = bonusPtUndoList.peek();
			bonusPtUndoList.pop();

			combo = 0;

			if (score - 25 < 0) {
				score = 0;
			} else {
				score -= 25;
			}
		}
	}

	public void outputBoard() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				System.out.print(board[x][y]);
			}
			System.out.println();
		}
	}

	public void outputRow() {
		for (int i = 0; i < BDWIDTH; i++) {
			System.out.print(boardRow[i] + ", ");
		}
		System.out.println();
	}

	public void boardCleared() {
		int count = 0;
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				if (board[x][y] != 0) {
					count++;
				}
			}
		}
		if (count != 0) {
			boardCleared = false;
		} else {
			boardCleared = true;
		}
	}

	public boolean GameOver() {
		for (int x = 0; x < BDHEIGHT; x++) {
			for (int y = 0; y < BDWIDTH; y++) {
				int temp = board[x][y];
				if (temp != 0 && temp != 5) {
					if (x < BDHEIGHT - 1) {
						int tempX = board[x + 1][y];
						if (temp == tempX)
							return false;
					}
					if (y < BDWIDTH - 1) {
						int tempY = board[x][y + 1];
						if (temp == tempY)
							return false;
					}
				}
			}
		}

		// if (boardCleared) {
		// return false;
		// } else {
		// return true;
		// }

		return true;
	}

	public void updateScores() {

	}

	public void resetGameScores() {
		classicHighScore = 0;
		gravityHighScore = 0;
		critMassHighScore = 0;
		infinityHighScore = 0;
	}

	public void saveCurrentGame() {
		BufferedWriter writer = null;

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File currentGame = new File(classLoader.getResource("currentGame.txt")
				.getFile());
		File undoListTxt = new File(classLoader.getResource("undoList.txt")
				.getFile());

		try {
			writer = new BufferedWriter(new FileWriter(currentGame));

			String gm = new String("");
			String dir = new String("");

			// sets gamemode int to approptiate string
			if (gameMode == 1) {
				gm = "Classic";
			} else if (gameMode == 2) {
				gm = "Gravity";
			} else if (gameMode == 3) {
				gm = "CriticalMass";
			} else if (gameMode == 4) {
				gm = "Infinity";
			}
			writer.write("GameMode:" + gm);
			writer.newLine();

			boardCleared();
			writer.write("GameOver:" + boardCleared);
			writer.newLine();

			// sets the Move Count
			writer.write("MoveCount:" + moveCount);
			writer.newLine();

			// sets the gravity direction
			if (direction == 1) {
				dir = "Down";
			} else if (direction == 2) {
				dir = "Up";
			} else if (direction == 3) {
				dir = "Left";
			} else if (direction == 4) {
				dir = "Right";
			}
			writer.write("GravityDirection:" + dir);
			writer.newLine();

			// writes current score to file
			writer.write("Score:" + score);
			writer.newLine();

			// writes current bonuspoints to file
			writer.write("BonusPoints:" + bonusPoints);
			writer.newLine();

			// writes the row to file
			writer.write("BoardRow:");
			for (int i = 0; i < BDWIDTH; i++) {
				writer.write(boardRow[i] + ",");
			}
			writer.newLine();

			// writes the board to file
			writer.write("BoardLayout:");
			writer.newLine();
			for (int i = 0; i < BDHEIGHT; i++) {
				for (int j = 0; j < BDWIDTH; j++) {
					writer.write("[" + i + "," + j + "]" + getBoardVal(i, j));
				}
				writer.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
		try {
			writer = new BufferedWriter(new FileWriter(undoListTxt));

			// Writes Title to txt file
			writer.write("Undo List:");
			writer.newLine();

			for (int l = 0; l < undoList.size(); l++) {
				writer.write(undoList.get(l));
				writer.newLine();
				writer.write(scoreUndoList.get(l) + "");
				writer.newLine();
				writer.write(bonusPtUndoList.get(l) + "");
				writer.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public void loadCurrentGame() {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File currentGame = new File(classLoader.getResource("currentGame.txt")
				.getFile());
		File undoListTxt = new File(classLoader.getResource("undoList.txt")
				.getFile());

		try (BufferedReader br = new BufferedReader(new FileReader(currentGame))) {
			String line = br.readLine();

			while (line != null) {

				if (line.substring(0, 5).equals("Score")) {
					score = Integer.parseInt(line.substring(6));
				} else if (line.substring(0, 8).equals("GameMode")) {
					if (line.substring(9, 16).equals("Classic")) {
						gameMode = classic;
					} else if (line.substring(9, 16).equals("Gravity")) {
						gameMode = gravity;
					} else if (line.substring(9, 17).equals("Infinity")) {
						gameMode = infinity;
					} else if (line.substring(9, 21).equals("CriticalMass")) {
						gameMode = criticalMass;
						randomMax = randomMaxCM;

					}
				} else if (line.substring(0, 8).equals("GameOver")) {
					if (line.substring(9, 13).equals("true")) {

						gameOver = true;
						timeLeft = 0;
						displayClearBoard = false;

					} else if (line.substring(9, 14).equals("false")) {
						gameOver = false;
						timeLeft = 5;
					}

				} else if (line.substring(0, 8).equals("BoardRow")) {

					String[] str = line.substring(9).split(",");

					for (int i = 0; i < BDWIDTH; i++) {
						setBoardRowVal(i, Integer.parseInt(str[i]));
					}

				} else if (line.substring(0, 9).equals("MoveCount")) {
					moveCount = Integer.parseInt(line.substring(10));
					if (moveCount >= rowDisplayCount) {
						displayRow = true;
					}
				} else if (line.substring(0, 11).equals("BonusPoints")) {
					bonusPoints = Integer.parseInt(line.substring(12));
				} else if (line.substring(0, 11).equals("BoardLayout")) {
					line = br.readLine();

					while (line != null) {
						String regx = "[]";
						char[] ca = regx.toCharArray();
						for (char c : ca) {
							line = line.replace("" + c, ",");
						}
						String[] str = line.split(",");
						int k = 1;
						while (k + 2 < str.length) {
							setBoardVal(Integer.parseInt(str[k]),
									Integer.parseInt(str[k + 1]),
									Integer.parseInt(str[k + 2]));
							k += 3;
						}
						line = br.readLine();
					}
				} else if (line.substring(0, 16).equals("GravityDirection")) {
					if (line.substring(17, 19).equals("Up")) {
						direction = up;
					} else if (line.substring(17, 21).equals("Down")) {
						direction = down;
					} else if (line.substring(17, 21).equals("Left")) {
						direction = left;
					} else if (line.substring(17, 22).equals("Right")) {
						direction = right;
					}
				}

				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(undoListTxt))) {
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {

				// if (line.substring(0, 8).equals("Undo List")) {
				//
				// }

				// System.out.println(line);
				undoList.push(line);
				line = br.readLine();
				// System.out.println(line);
				scoreUndoList.push(Integer.parseInt(line));
				line = br.readLine();
				bonusPtUndoList.push(Integer.parseInt(line));
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveGame() {

		BufferedWriter writer = null;

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File scores = new File(classLoader.getResource("scores.txt").getFile());

		try {
			writer = new BufferedWriter(new FileWriter(scores));

			writer.write("BonusPoints:" + bonusPoints);
			writer.newLine();
			if (gameMode == classic && score > classicHighScore) {
				highScore = true;
				classicHighScore = score;
			}
			writer.write("Classic:" + classicHighScore);
			writer.newLine();
			if (gameMode == gravity && score > gravityHighScore) {
				highScore = true;
				gravityHighScore = score;
			}
			writer.write("Gravity:" + gravityHighScore);
			writer.newLine();
			if (gameMode == criticalMass && score > critMassHighScore) {
				highScore = true;
				critMassHighScore = score;
			}
			writer.write("CriticalMass:" + critMassHighScore);
			writer.newLine();
			if (gameMode == infinity && score > infinityHighScore) {
				highScore = true;
				infinityHighScore = score;
			}
			writer.write("Infinity:" + infinityHighScore);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}

	}

	public void loadGame() {

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File scores = new File(classLoader.getResource("scores.txt").getFile());

		try (BufferedReader br = new BufferedReader(new FileReader(scores))) {
			String line = br.readLine();

			while (line != null) {

				if (line.substring(0, 7).equals("Classic")) {
					classicHighScore = Integer.parseInt(line.substring(8));
				} else if (line.substring(0, 7).equals("Gravity")) {
					gravityHighScore = Integer.parseInt(line.substring(8));
				} else if (line.substring(0, 8).equals("Infinity")) {
					infinityHighScore = Integer.parseInt(line.substring(9));
				} else if (line.substring(0, 11).equals("BonusPoints")) {
					bonusPoints = Integer.parseInt(line.substring(12));
				} else if (line.substring(0, 12).equals("CriticalMass")) {
					critMassHighScore = Integer.parseInt(line.substring(13));
				}

				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMoveCount() {
		prevMoveCount = moveCount;
		moveCount++;
	}

	public void subtractMoveCount() {
		if (moveCount > 0) {
			moveCount--;
		} else if (moveCount == 0) {
			if (prevMoveCount == rowDropCount - 1) {
				moveCount = rowDropCount - 1;
			} else {
				moveCount = prevMoveCount;
			}
		}
	}

	// Getters and Setters

	public boolean isDisplayClearBoard() {
		return displayClearBoard;
	}

	public void setDisplayClearBoard(boolean displayClearBoard) {
		this.displayClearBoard = displayClearBoard;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean getBoardCleared() {
		return boardCleared;
	}

	public void setBoardCleared(boolean b) {
		boardCleared = b;
	}

	public int getCritMassSize() {
		return critMassSize;
	}

	public Stack<String> getCritMassPercentages() {
		return critMassPercentages;
	}

	public void setCritMassPercentages(Stack<String> critMassPercentages) {
		this.critMassPercentages = critMassPercentages;
	}

	public int getRowVal(int i) {
		return boardRow[i];
	}

	public void setRowVal(int i, int val) {
		boardRow[i] = val;
	}

	public void setPrevRowVal(int i, int val) {
		prevBoardRow[i] = val;
	}

	public boolean getDropRow() {
		return dropRow;
	}

	public void setDropRow(boolean b) {
		dropRow = b;
	}

	public void setMoveCount(int mc) {
		moveCount = mc;
	}

	public boolean isSquare() {
		return square;
	}

	public void setSquare(boolean square) {
		this.square = square;
	}

	public boolean isCross() {
		return cross;
	}

	public void setCross(boolean cross) {
		this.cross = cross;
	}

	public boolean isStairs() {
		return stairs;
	}

	public void setStairs(boolean stairs) {
		this.stairs = stairs;
	}

	public boolean isCorner() {
		return corner;
	}

	public void setCorner(boolean corner) {
		this.corner = corner;
	}

	public boolean isT() {
		return T;
	}

	public void setT(boolean t) {
		T = t;
	}

	public boolean isL() {
		return L;
	}

	public void setL(boolean l) {
		L = l;
	}

	public boolean isLine() {
		return line;
	}

	public void setLine(boolean line) {
		this.line = line;
	}

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		TileGame.gameMode = gameMode;
	}

	public static int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		TileGame.direction = direction;
		// DropTiles();
	}

	public int getScore() {
		return (int) score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public int getBoardRowVal(int i) {
		return boardRow[i];
	}

	public void setBoardRowVal(int i, int val) {
		boardRow[i] = val;
	}

	public int getBoardVal(int x, int y) {
		return board[x][y];
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] b) {
		board = b;
	}

	public void setBoardVal(int x, int y, int val) {
		board[x][y] = val;
	}

	public Stack<String> getUndoList() {
		return undoList;
	}

	public void setUndoList(Stack<String> undoList) {
		this.undoList = undoList;
	}

	public Stack<Integer> getScoreList() {
		return scoreUndoList;
	}

	public void setScoreList(Stack<Integer> scoreList) {
		this.scoreUndoList = scoreList;
	}

	public String getScoreStr() {
		return scoreStr;
	}

	public void setScoreStr(String scoreStr) {
		this.scoreStr = scoreStr;
	}

	public String getBonusPtStr() {
		return bonusPtStr;
	}

	public void setBonusPtStr(String bonusPtStr) {
		this.bonusPtStr = bonusPtStr;
	}

	public String getComboStr() {
		return comboStr;
	}

	public int getBDWIDTH() {
		return BDWIDTH;
	}

	public int getBDHEIGHT() {
		return BDHEIGHT;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public boolean getHighScore() {
		return highScore;
	}

	public void setHighScore(boolean b) {
		highScore = b;
	}

	public boolean getDisplayRow() {
		return displayRow;
	}

}

package package1;

import java.awt.image.BufferedImage;

public class Textures {

	public BufferedImage[] blueTile = new BufferedImage[4];
	public BufferedImage[] redTile = new BufferedImage[4];
	public BufferedImage[] goldTile = new BufferedImage[4];
	public BufferedImage[] silverTile = new BufferedImage[4];

	public BufferedImage[] cmTile = new BufferedImage[4];

	public BufferedImage[] upTile = new BufferedImage[2];
	public BufferedImage[] downTile = new BufferedImage[2];
	public BufferedImage[] leftTile = new BufferedImage[2];
	public BufferedImage[] rightTile = new BufferedImage[2];

	public BufferedImage[] centerTile = new BufferedImage[2];

	private SpriteSheet ss;
	private SpriteSheet ssMisc;

	public Textures(Game game) {
		ss = new SpriteSheet(game.getSpriteSheet());
		ssMisc = new SpriteSheet(game.getSpriteSheetMisc());

		getTextures();
	}

	private void getTextures() {
		blueTile[0] = ss.grabImage(1, 1, 32, 32);
		blueTile[1] = ss.grabImage(1, 2, 32, 32);
		blueTile[2] = ss.grabImage(1, 3, 32, 32);
		blueTile[3] = ss.grabImage(1, 4, 32, 32);

		redTile[0] = ss.grabImage(2, 1, 32, 32);
		redTile[1] = ss.grabImage(2, 2, 32, 32);
		redTile[2] = ss.grabImage(2, 3, 32, 32);
		redTile[3] = ss.grabImage(2, 4, 32, 32);

		goldTile[0] = ss.grabImage(3, 1, 32, 32);
		goldTile[1] = ss.grabImage(3, 2, 32, 32);
		goldTile[2] = ss.grabImage(3, 3, 32, 32);
		goldTile[3] = ss.grabImage(3, 4, 32, 32);

		silverTile[0] = ss.grabImage(4, 1, 32, 32);
		silverTile[1] = ss.grabImage(4, 2, 32, 32);
		silverTile[2] = ss.grabImage(4, 3, 32, 32);
		silverTile[3] = ss.grabImage(4, 4, 32, 32);

		cmTile[0] = ss.grabImage(5, 1, 32, 32);
		cmTile[1] = ss.grabImage(5, 2, 32, 32);
		cmTile[2] = ss.grabImage(5, 3, 32, 32);
		cmTile[3] = ss.grabImage(5, 4, 32, 32);

		upTile[0] = ssMisc.grabImage(1, 1, 32, 32);
		upTile[1] = ssMisc.grabImage(1, 2, 32, 32);

		leftTile[0] = ssMisc.grabImage(2, 1, 32, 32);
		leftTile[1] = ssMisc.grabImage(2, 2, 32, 32);

		downTile[0] = ssMisc.grabImage(3, 1, 32, 32);
		downTile[1] = ssMisc.grabImage(3, 2, 32, 32);

		rightTile[0] = ssMisc.grabImage(4, 1, 32, 32);
		rightTile[1] = ssMisc.grabImage(4, 2, 32, 32);

		centerTile[0] = ssMisc.grabImage(5, 1, 32, 32);
		centerTile[1] = ssMisc.grabImage(5, 2, 32, 32);

	}
}

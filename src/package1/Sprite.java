package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sprite implements Cloneable, Serializable {

	private transient BufferedImage spriteImg;

	private float x;
	private float y;

	private float refX;
	private float refY;

	private int tileVal = 0;

	// For serialization
	private static final long serialVersionUID = 1L;

	public Sprite(BufferedImage img) {
		spriteImg = toCompatibleImage(img);
	}

	@Override
	public Sprite clone() {
		try {
			return (Sprite) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Clone failed.");
			return null;
		}
	}

	/*
	 * Paints the sprite. If splitSprite has been used, it will paint the
	 * current frame.
	 */
	public void paint(Graphics g) {
		g.drawImage(this.getImage(), Math.round(this.getRealX()),
				Math.round(this.getRealY()), null);
	}

	/*
	 * Paints the original Sprite if setAnimation has been used
	 */
	public void paintOrig(Graphics g) {
		g.drawImage(spriteImg, Math.round(x), Math.round(y), null);
	}

	/*
	 * Sets the position based on the parameters
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * Sets the position based on the parameters
	 */
	public void setPosition(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	// Defines which reference pixel (i.e where the image will be placed on the
	// x/y coordinates)
	public void setRefPixel(float x, float y) {
		refX = x;
		refY = y;
	}

	// Creates a BufferedImage that is optimized for this system.
	private static BufferedImage createCompatibleImage(int width, int height) {
		GraphicsConfiguration gfx = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		return gfx.createCompatibleImage(width, height,
				Transparency.TRANSLUCENT);
	}

	private static BufferedImage toCompatibleImage(BufferedImage image) {
		// Create a new compatible image
		BufferedImage bimg = createCompatibleImage(image.getWidth(),
				image.getHeight());

		// Get the graphics of the image and paint the original image onto it.
		Graphics2D g = (Graphics2D) bimg.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		// Return the new, compatible image.
		return bimg;
	}

	/*
	 * Collision detection between the current sprite and another sprite
	 */
	public boolean collidesWith(Sprite otherSprite, boolean pixelPerfect) {
		boolean isColliding = false;

		Rectangle r1 = getBounds(this);
		Rectangle r2 = getBounds(otherSprite);

		if (r1.intersects(r2)) {
			if (pixelPerfect) {
				isColliding = pixelPerfectCollision(otherSprite, r1, r2);
			} else {
				isColliding = true;
			}
		}

		return isColliding;
	}

	/*
	 * pixelPerfectCollision(); first determines the area where the sprites
	 * collides AKA the collision-rectangle. It then grabs the pixels from both
	 * sprites which are inside the rectangle. It then checks every pixel from
	 * the arrays given by grabPixels();, and if 2 pixels at the same position
	 * are opaque, (alpha value over 0) it will return true. Otherwise it will
	 * return false.
	 */
	private boolean pixelPerfectCollision(Sprite sprite, Rectangle r1,
			Rectangle r2) {
		int cornerTopX = -1;
		int cornerTopY = -1;

		int cornerBottomX = 1;
		int cornerBottomY = 1;

		/*
		 * Get the X-values for the two coordinates where the sprites collide
		 * Seriously, don't use the for loop, I don't know what I was thinking.
		 * Solution found below.
		 */
		// for(int i=0;i<r1.getWidth();i++) {
		// if(r1.getX()+i >= r2.getX() & r1.getX()+i < r2.getX()+r2.getWidth())
		// {
		// if(cornerTopX==-1)cornerTopX = (int) (r1.getX() + i);
		// cornerBottomX = (int) (r1.getX() + i);
		// }
		// }

		cornerTopX = (r1.x > r2.x) ? r1.x : r2.x;
		cornerBottomX = ((r1.x + r1.width) < (r2.x + r2.width)) ? (r1.x + r1.width)
				: (r2.x + r2.width);

		/*
		 * Get the Y-values for the two coordinates where the sprites collide
		 * Solution found below.
		 */
		// for(int i=0;i<r1.getHeight();i++) {
		// if(r1.getY()+i >= r2.getY() & r1.getY()+i < r2.getY()+r2.getHeight())
		// {
		// if(cornerTopY==-1)cornerTopY = (int) (r1.getY() + i);
		// cornerBottomY = (int) (r1.getY() + i);
		// }
		// }

		cornerTopY = (r1.y > r2.y) ? r1.y : r2.y;
		cornerBottomY = ((r1.y + r1.height) < (r2.y + r2.height)) ? (r1.y + r1.height)
				: (r2.y + r2.height);

		// Determine the width and height of the collision rectangle
		int width = cornerBottomX - cornerTopX;
		int height = cornerBottomY - cornerTopY;

		// Create arrays to hold the pixels
		int[] pixels1 = new int[width * height];
		int[] pixels2 = new int[width * height];

		// Create the pixelgrabber and fill the arrays
		PixelGrabber pg1 = new PixelGrabber(getImage(), Math.round(cornerTopX
				- getRealX()), Math.round(cornerTopY - getRealY()), width,
				height, pixels1, 0, width);
		PixelGrabber pg2 = new PixelGrabber(sprite.getImage(),
				Math.round(cornerTopX - sprite.getRealX()),
				Math.round(cornerTopY - sprite.getRealY()), width, height,
				pixels2, 0, width);

		// Grab the pixels
		try {
			pg1.grabPixels();
			pg2.grabPixels();
		} catch (InterruptedException ex) {
			Logger.getLogger(Sprite.class.getName())
					.log(Level.SEVERE, null, ex);
		}

		// Check if pixels at the same spot from both arrays are not
		// transparent.
		for (int i = 0; i < pixels1.length; i++) {
			int a = (pixels1[i] >>> 24) & 0xff;
			int a2 = (pixels2[i] >>> 24) & 0xff;

			/*
			 * Awesome, we found two pixels in the same spot that aren't
			 * completely transparent! Thus the sprites are colliding!
			 */
			if (a > 0 && a2 > 0)
				return true;

		}
		return false;
	}

	// Invokes transparency on the selected color
	public void invokeTransparency(Color color) {
		spriteImg = makeTransparent(spriteImg, color);
	}

	public void invokeTransparency(Color color, int newAlphaValue) {
		spriteImg = makeTransparent(spriteImg, color, newAlphaValue);
	}

	public static BufferedImage makeTransparent(BufferedImage img,
			final Color color) {
		ImageFilter filter = new RGBImageFilter() {

			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB)
					return 0x00FFFFFF & rgb;
				else
					return rgb;
			}
		};

		ImageProducer ip = new FilteredImageSource(img.getSource(), filter);

		Image temp = Toolkit.getDefaultToolkit().createImage(ip);

		BufferedImage bufImg = createCompatibleImage(img.getWidth(),
				img.getHeight());
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		return bufImg;
	}

	public static BufferedImage makeTransparent(BufferedImage img,
			final Color color, final int newColor) {
		ImageFilter filter = new RGBImageFilter() {

			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					return newColor & rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(img.getSource(), filter);

		Image temp = Toolkit.getDefaultToolkit().createImage(ip);

		BufferedImage bufImg = createCompatibleImage(img.getWidth(),
				img.getHeight());
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		return bufImg;
	}

	/*
	 * Returns the width of the current sprite
	 */
	public int getWidth() {
		return this.getImage().getWidth();
	}

	/*
	 * Returns the height of the sprite
	 */
	public int getHeight() {
		return this.getImage().getHeight();
	}

	public void setTileVal(int i) {
		tileVal = i;
	}

	public int getTileVal() {
		return tileVal;
	}

	/*
	 * Returns the X-position of the sprite getRealX() returns the X-Position of
	 * the Sprite's upper-left corner
	 */
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getRefX() {
		return refX;
	}

	public float getRealX() {
		return (x - refX);
	}

	/*
	 * Returns the Y-position of the sprite getRealY() returns the Y-position of
	 * the Sprite's upper-left corner
	 */
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getRefY() {
		return refY;
	}

	public float getRealY() {
		return (y - refY);
	}

	public Point getLocation() {
		return new Point((int) x, (int) y);
	}

	/*
	 * Returns the boundaries for the sprite, used for collision detection
	 */
	public static Rectangle getBounds(Sprite sprite) {
		return new Rectangle(Math.round(sprite.getRealX()), Math.round(sprite
				.getRealY()), sprite.getWidth(), sprite.getHeight());
	}

	/*
	 * Returns the boundaries for this sprite, used for collision detection
	 */
	public Rectangle getBounds() {
		return new Rectangle(Math.round(this.getRealX()), Math.round(this
				.getRealY()), this.getWidth(), this.getHeight());
	}

	/*
	 * Returns the image this sprite is using (if it was split, it will return
	 * the current frame. Else it will return the whole image.)
	 */
	public BufferedImage getImage() {
		return spriteImg;
	}

	public void setImage(BufferedImage img) {
		spriteImg = img;
	}

	// Returns the whole image, no matter if it has been split or not.
	public BufferedImage getOrigImage() {
		return spriteImg;
	}

	// Rotates the image the amount of degrees specified by the user
	public void rotateImage(double degrees) {

		double sin = Math.abs(Math.sin(Math.toRadians(degrees)));
		double cos = Math.abs(Math.cos(Math.toRadians(degrees)));
		int w = this.getOrigImage().getWidth();
		int h = this.getOrigImage().getHeight();
		int neww = (int) Math.floor(w * cos + h * sin);
		int newh = (int) Math.floor(h * cos + w * sin);

		BufferedImage result = createCompatibleImage(neww, newh);
		Graphics2D g = result.createGraphics();
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(degrees), w / 2, h / 2);
		g.drawRenderedImage(this.getOrigImage(), null);
		g.dispose();

		this.spriteImg = result;
	}

	// Rotates the image by 90 degrees
	public void rotateImage90() {

		double i = Math.toRadians(90);
		double sin = Math.abs(Math.sin(i));
		double cos = Math.abs(Math.cos(i));
		int w = this.getOrigImage().getWidth();
		int h = this.getOrigImage().getHeight();
		int neww = (int) Math.floor(w * cos + h * sin);
		int newh = (int) Math.floor(h * cos + w * sin);

		BufferedImage result = createCompatibleImage(neww, newh);
		Graphics2D g = result.createGraphics();
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(i, w / 2, h / 2);
		g.drawRenderedImage(this.getOrigImage(), null);
		g.dispose();

		this.spriteImg = result;

	}

	// Flips the sprite (horizontal/vertical)
	public void flipHorizontal() {
		int w = this.getOrigImage().getWidth();
		int h = this.getOrigImage().getHeight();

		BufferedImage bimg = new BufferedImage(w, h,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bimg.createGraphics();

		g.drawImage(this.getOrigImage(), 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();

		this.spriteImg = toCompatibleImage(bimg);
	}

	public void flipVertical() {
		int w = this.getOrigImage().getWidth();
		int h = this.getOrigImage().getHeight();

		BufferedImage bimg = new BufferedImage(w, h,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bimg.createGraphics();

		g.drawImage(this.getOrigImage(), 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();

		this.spriteImg = toCompatibleImage(bimg);
	}

	// Call one of these methods after the sprite has been de-serialized.
	public void reloadSprite(BufferedImage img) {
		this.spriteImg = img;
	}

	public static BufferedImage duplicateAndReverse(BufferedImage bimg) {
		BufferedImage temp = createCompatibleImage(bimg.getWidth() * 2,
				bimg.getHeight());

		int w = bimg.getWidth();
		int h = bimg.getHeight();

		Graphics2D g = temp.createGraphics();

		g.drawImage(bimg, 0, 0, null);

		g.drawImage(bimg, w, 0, w * 2, h, w, 0, 0, h, null);
		g.dispose();

		return temp;
	}

	public Rectangle getVisibleBounds(int i) {
		if (i == 12) {
			return new Rectangle(Math.round(this.getRealX()), Math.round(this
					.getRealY()), this.getWidth(), this.getHeight());
		} else {
			return new Rectangle(Math.round(this.getRealX()), Math.round(this
					.getRealY()), this.getWidth() - 22, this.getHeight());
		}
	}

	public Rectangle getVisibleBounds(int i, int visCount) {
		if (i == (visCount - 1)) {
			return new Rectangle(Math.round(this.getRealX()), Math.round(this
					.getRealY()), this.getWidth(), this.getHeight());
		} else {
			return new Rectangle(Math.round(this.getRealX()), Math.round(this
					.getRealY()), this.getWidth() - 22, this.getHeight());
		}
	}
}

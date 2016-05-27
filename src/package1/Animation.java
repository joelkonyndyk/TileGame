package package1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class Animation {

	// private StopWatch time = new StopWatch();

	private Sprite sprite;

	private AnimationController animCont;

	private int destX, destY;

	// distance to destination
	private float distX, distY;

	// increment to move per turn
	private float incX, incY;

	private float incRotate;

	private float rotate = 0;

	private boolean animateUsingSpeed;

	// Number of ticks animation will be (Shoud mabye be swapped in the future
	// to actual time)
	private int duration;

	// Constructors
	public Animation(AnimationController ac, Sprite s, Point destPt,
			int duration) {
		sprite = s;

		animCont = ac;

		destX = destPt.x;
		destY = destPt.y;

		this.duration = duration;

		init();
	}
	
	public Animation(AnimationController ac, Sprite s, Point destPt,
			int duration, boolean b) {
		sprite = s;

		animCont = ac;

		destX = destPt.x;
		destY = destPt.y;

		this.duration = duration;
		
		animateUsingSpeed = b;

		init();
	}

	public Animation(AnimationController ac, Sprite s, Point destPt,
			int rotate, int duration) {
		sprite = s;

		animCont = ac;

		destX = destPt.x;
		destY = destPt.y;

		this.duration = duration;
		this.rotate = rotate;

		init();
	}

	// Methods

	public void init() {
		distX = destX - sprite.getX();
		distY = destY - sprite.getY();

		if (animateUsingSpeed && distX < 0 || distY < 0) {
			duration = -(duration);
		} 
		
		incX = distX / duration;
		incY = distY / duration;

		incRotate = rotate / duration;

		distX = Math.abs(distX);
		distY = Math.abs(distY);

		if (animateUsingSpeed) {
			incX = duration;
			incY = duration;
		} 
//		else {
//			distX = Math.abs(distX);
//			distY = Math.abs(distY);
//		}
	}

	public void tick() {

		if (distX > Math.abs(incX)) {
			sprite.setPosition(sprite.getX() + incX, sprite.getY());
			distX -= Math.abs(incX);
		} else {
			sprite.setPosition(destX, sprite.getY());
		}

		if (distY > Math.abs(incY)) {
			sprite.setPosition(sprite.getX(), sprite.getY() + incY);
			distY -= Math.abs(incY);
		} else {
			sprite.setPosition(sprite.getX(), destY);
		}

		if (sprite.getX() == destX && sprite.getY() == destY) {
			animCont.removeAnimation(this);
		}

		// This is where the sprite gets rotated
		if (rotate != 0) {
			// System.out.println(rotate);
			// System.out.println(incRotate);

			sprite.rotateImage(incRotate);
			rotate -= incRotate;
		}

	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	}

	// Getters and Setters

	public void setAnimUsingSpeed(boolean b) {
		animateUsingSpeed = b;
	}

}

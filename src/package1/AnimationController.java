package package1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

public class AnimationController {
	// This is where we will control the cards movements on the screen

	private LinkedList<Animation> animList = new LinkedList<Animation>();

	private StopWatch time = new StopWatch();

	private Animation anim;

	// distance to destination
	private int distX, distY;

	// Constructors
	public AnimationController() {

	}

	// Methods

	public void createAnimation(Animation a) {
		addAnimation(a);
	}

	public void tick() {

		for (int i = 0; i < animList.size(); i++) {
			anim = animList.get(i);
			anim.tick();
		}

	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	}

	// Getters and Setters
	public void addAnimation(Animation block) {
		animList.add(block);
	}

	public void removeAnimation(Animation block) {
		animList.remove(block);
	}

	public void clearAnimations() {
		animList.clear();
	}

	public boolean isEmpty() {
		if (animList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}

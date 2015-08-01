package package1;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;

public class Controller {
	private LinkedList<EntityA> ea = new LinkedList<EntityA>();
	private LinkedList<EntityB> eb = new LinkedList<EntityB>();

	// private LinkedList<Text> textList = new LinkedList<Text>();

	EntityA enta;
	EntityB entb;
	Textures tex;
	Random r = new Random();
	private Game game;

	private StopWatch time = new StopWatch();

	public Controller(Textures tex, Game game) {

		this.tex = tex;
		this.game = game;

		// for (int i = 0; i < 20; i++) {
		// addEntity(new Enemy(r.nextInt(640), 10, tex));
		// }

		time.start();

	}

	public void createText(Text t) {
		addEntity(t);
	}

	public void createTile(int tile_count) {
		for (int i = 0; i < tile_count; i++) {
			// addEntity(new Enemy(r.nextInt(640), -10, tex, this, game));
		}
	}

	public void tick() {

		// System.out.println(time.getElapsedTime() % 50);

		if (time.getElapsedTime() > 50) {
			// B CLASS
			for (int i = 0; i < eb.size(); i++) {
				entb = eb.get(i);

				entb.tick();
			}
			time.reset();
		}

		// if (time.getElapsedTimeSecs() > 500) {
		//
		// // B CLASS
		// for (int i = 0; i < eb.size(); i++) {
		// entb = eb.get(i);
		//
		// entb.tick();
		// }
		//
		// }

		// A CLASS
		for (int i = 0; i < ea.size(); i++) {
			enta = ea.get(i);

			enta.tick();
		}

		// // B CLASS
		// for (int i = 0; i < eb.size(); i++) {
		// entb = eb.get(i);
		//
		// entb.tick();
		// }
	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		// g2d.setFont(fnt0);

		// A CLASS
		for (int i = 0; i < ea.size(); i++) {
			enta = ea.get(i);

			enta.render(g2d);
		}
		// B CLASS
		for (int i = 0; i < eb.size(); i++) {
			entb = eb.get(i);

			entb.render(g2d);
		}

	}

	public void addEntity(EntityA block) {
		ea.add(block);
	}

	public void removeEntity(EntityA block) {
		ea.remove(block);
	}

	public void addEntity(EntityB block) {
		eb.add(block);
	}

	public void removeEntity(EntityB block) {
		eb.remove(block);
	}

	public LinkedList<EntityA> getEntityA() {
		return ea;
	}

	public LinkedList<EntityB> getEntityB() {
		return eb;
	}
}

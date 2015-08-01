package package1;

public class Physics {
	public static boolean Collision(EntityA enta, EntityA entb) {

		if (enta.getBounds().intersects(entb.getBounds())) {
			return true;
		}

		return false;
	}

	// public static boolean Collision(EntityB entb, EntityA enta) {
	//
	// if (entb.getBounds().intersects(enta.getBounds())) {
	// return true;
	// }
	//
	// return false;
	// }
}

package package1;

public class StopWatch {

	private long startTime;
	private long stopTime;
	private boolean running;
	private boolean timeFire;

	public StopWatch() {
		startTime = 0;
		stopTime = 0;
		running = false;
		timeFire = false;
	}

	public void start() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	public void reset() {
		this.startTime = System.currentTimeMillis();
	}

	// elaspsed time in milliseconds
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}

	// elaspsed time in seconds
	public long getElapsedTimeSecs() {
		long elapsed;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000);
		} else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		return elapsed;
	}

	public boolean timeFire(int t) {

		return timeFire;
	}

	public boolean isRunning() {
		return running;
	}
}
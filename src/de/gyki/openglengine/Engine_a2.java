package de.gyki.openglengine;

import de.gyki.game.Game;
import de.gyki.game.Projekttage;
import de.gyki.openglengine.core.CoreThread;

/**
 * This Class starts the engine thread and waits until it has ended to shutdown
 * the application.
 * 
 * @author Florian Albrecht
 * @date 25.07.2015
 */

public class Engine_a2 {
	/**
	 * SIZE represents the time the resolution is multiplied to fit the screen.
	 */
	public final static int SIZE = 60;

	/**
	 * WRES represents the resolution of the width.
	 */
	public final static float WRES = 16.0f;

	/**
	 * HRES represents the resolution of the height.
	 */
	public final static float HRES = 9.0f;

	/**
	 * WIDTH represents the actual width of the window.
	 */
	public final static int WIDTH = (int) WRES * SIZE;

	/**
	 * HEIGHT represents the actual height of the window.
	 */
	public final static int HEIGHT = (int) HRES * SIZE;

	/**
	 * TITLE represents the title of the window.
	 */
	public final static String TITLE = "Engine_a02";

	/**
	 * PATH stores the path the shaders and the textures are stored in.
	 */
	public final static String PATH = "res/";

	/**
	 * QUIT handles the game loop.
	 */
	public static boolean QUIT;

	/**
	 * CURRENTGAME is the game currentl running on the Engine.
	 */
	public final static Game CURRENTGAME = new Projekttage();

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CoreThread core = new CoreThread();
		core.start();

		Math.abs(0);
		try {
			core.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

}

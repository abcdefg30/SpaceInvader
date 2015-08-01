package de.gyki.openglengine.core;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.physics.PhysicEngine;

public class MeshHandler extends GLFWKeyCallback {

	/**
	 * The {@code PhysicEngine} that runs on the Engine.
	 */
	public static PhysicEngine PhysicEngine;

	/**
	 * Initializes the game and the physicEngine
	 */
	public MeshHandler() {
		PhysicEngine = new PhysicEngine();
	}

	/**
	 * Just calls game.init() to initializes the game. Necessary because of the
	 * order OpenGL is initialized.
	 */
	public void init() {
		Engine_a2.CURRENTGAME.init();
	}

	/**
	 * Renders the game.
	 */
	public void render() {
		Engine_a2.CURRENTGAME.render();
	}

	/**
	 * Updates the game and the PhysicEngine and passes the results to the game.
	 */
	public void update() {
		Engine_a2.CURRENTGAME.update(PhysicEngine.MYupdate());

	}

	/**
	 * Deletes everything.
	 */
	public void delete() {
		Engine_a2.CURRENTGAME.destroy();
		PhysicEngine.destroy();
	}

	/**
	 * Updates the keyInput and passes all pressed/released keys to the game. If
	 * the escape key is pressed the Game is closed.
	 */
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {

		if (action == GLFW_PRESS && key != GLFW_KEY_ESCAPE)
			Engine_a2.CURRENTGAME.updateKeyPress(key);
		if (action == GLFW_RELEASE)
			Engine_a2.CURRENTGAME.updateKeyReleas(key);

		if (action == GLFW_PRESS && key == GLFW_KEY_ESCAPE)
			Engine_a2.QUIT = true;
	}

}

package de.gyki.openglengine.core;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_ALPHA_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.util.OpenGLErrorCatcher;

/**
 * The class {@code CoreThread} contains the gameLoop that controls the
 * sequences of events and the order of the events.
 * 
 * @author Flo
 *
 */
public class CoreThread extends Thread {

	/**
	 * The {@code long} that refers to the window with the current glContext.
	 */
	private long window;
	/**
	 * The {@code long} that refers to the current {@code GlContext}.
	 */
	private long glContext;

	/**
	 * The MeshHandler handles the input and the {@code Game}.
	 */
	private MeshHandler meshHandler;

	/**
	 * The GLFWErrorCallback handles GLFW Errors.
	 */
	private GLFWErrorCallback glfwErrorCallback;

	/**
	 * Just initilizes the MeshHandler.
	 */
	public CoreThread() {
		// Create MeshHandler
		meshHandler = new MeshHandler();
	}

	/**
	 * Run contains the order the Engine runs'. Initialization, loop and
	 * termination.
	 */
	public void run() {
		// Enable the gameloop
		Engine_a2.QUIT = false;
		// Initialize glfw and openGL
		init();
		OpenGLErrorCatcher.check("Initialization");
		// Initialize the Meshes
		meshHandler.init();
		OpenGLErrorCatcher.check("Mesh Initialization");
		// Start the gameloop
		loop();

		// Delete the Meshes
		meshHandler.delete();

		// Release window and window callbacks
		glfwDestroyWindow(window);
		meshHandler.release();
		// Terminate GLFW and release the GLFWerrorfun
		glfwTerminate();
		glfwErrorCallback.release();

		System.out.println("LWJGL Shutdown successful ;)");
	}

	/**
	 * Initializes all needed components such as GLFW, OpenGL and InputHandler.
	 */
	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(glfwErrorCallback = errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Print out the LWJGL version
		System.out.println("Current LWJGL version: " + Sys.getVersion());

		// Configure our window
		glfwWindowHint(GLFW_RED_BITS, 8); // Setting the Bits per color
		glfwWindowHint(GLFW_GREEN_BITS, 8);
		glfwWindowHint(GLFW_BLUE_BITS, 8);
		glfwWindowHint(GLFW_ALPHA_BITS, 8);

		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be
													// resizable

		// Create the window
		window = glfwCreateWindow(Engine_a2.WIDTH, Engine_a2.HEIGHT, Engine_a2.TITLE, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, meshHandler);

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - Engine_a2.WIDTH) / 2,
				(GLFWvidmode.height(vidmode) - Engine_a2.HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		GLContext.createFromCurrent();
		glContext = glfwGetCurrentContext();
		if (glContext <= 0)
			throw new RuntimeException("Failed to create the OpenGL Context!!!");

		System.out.println("Current OpenGL version: " + glGetString(GL_VERSION));
		System.out.println("Current GLSL version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
	}

	/**
	 * Contains the Gameloop and checks for Errors during the game. Renders and
	 * updates all components at the MeshHandler.
	 */
	private void loop() {
		long lastTime = System.nanoTime(); // Start time
		long timer = System.currentTimeMillis(); // when one sceond is over
		double delta = 0.0D; // when to Update
		double ns = 1000000000.0D / 60.0D; // when 1/60 of a second is over

		// Counter variables
		int frames = 0;
		int updates = 0;

		long now = 0L;
		glEnable(GL_DEPTH_TEST);

		while (Engine_a2.QUIT == false) {
			// now = System.nanoTime();
			// delta += (now - lastTime) / ns;
			// lastTime = now;
			// if (delta >= 0.95) {
			// delta = 0;
			updates++;
			meshHandler.update();
			glfwPollEvents();
			// }

			// Clear
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

			// Render and check for errors
			meshHandler.render();
			if (OpenGLErrorCatcher.check("Rendering")) {
				// TODO: ERROR Handling
			}

			// Swap the buffers
			glfwSwapBuffers(window);
			frames++;

			// Print out the stats
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " UPS, " + frames + " FPS");
				frames = 0;
				updates = 0;
			}

			// If the window should close
			if (glfwWindowShouldClose(window) == GL_TRUE)
				Engine_a2.QUIT = true;

			// .QUIT = true;
			// meshHandler.update();
		}
	}
}

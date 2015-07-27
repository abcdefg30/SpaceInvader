package de.gyki.openglengine.util;

import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

/**
 * This class is used to catch OpenGL errors while the program is running.
 * 
 * @author Flo
 * @version 1.1
 */
public class OpenGLErrorCatcher {

	/**
	 * Lets no one instantiate this class.
	 */
	private OpenGLErrorCatcher() {
	}

	/**
	 * Checks for OpenGL errors and prints their ID followed by the special
	 * error mesage out to the console.
	 * 
	 * @param msg
	 *            the special error message that is printed out with the error
	 *            ID
	 * @return if there was an error or not
	 */
	public static boolean check(String msg) {
		int err = glGetError();
		if (err != GL_NO_ERROR) {
			printErrMessage(err, msg);
			return true;
		}
		return false;
	}

	/**
	 * This method prints of the error and the special error message.
	 * 
	 * @param err
	 *            the ID of the error
	 * @param msg
	 *            the special error message
	 */
	private static void printErrMessage(int err, String msg) {
		String error = "Unknown error";
		switch (err) {
		case GL_INVALID_OPERATION:
			error = "INVALID_OPERATION";
			break;
		case GL_INVALID_ENUM:
			error = "INVALID_ENUM";
			break;
		case GL_INVALID_VALUE:
			error = "INVALID_VALUE";
			break;
		case GL_OUT_OF_MEMORY:
			error = "OUT_OF_MEMORY";
			break;
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			error = "INVALID_FRAMEBUFFER_OPERATION";
			break;
		}
		System.err.println("OpenGL Error : " + error + ", number: " + err + ", At: " + msg);
	}
}

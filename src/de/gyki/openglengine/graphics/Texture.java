package de.gyki.openglengine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.util.BufferUtils;

/**
 * This class is used to use textures with OpenGL.
 * 
 * @author Flo
 * @version 1.1
 */
public class Texture {

	/**
	 * The width of the image.
	 */
	private int width;

	/**
	 * The height of the image.
	 */
	private int height;

	/**
	 * The integer referring to the texture on the graphics card.
	 */
	private int textureID;

	/**
	 * Sets the textureID using the {@code load} method.
	 * 
	 * @param name
	 */
	public Texture(String name) {
		textureID = load(Engine_a2.PATH + "texture/" + name);
	}

	/**
	 * Sets the textureID using the {@code load} method.
	 * 
	 * @param name
	 * @param mode
	 */
	public Texture(String name, int glmode) {
		textureID = load(Engine_a2.PATH + "texture/" + name, glmode);
	}

	/**
	 * This method loads a image file from the hard drive and converts it to an
	 * GL_TEXTURE for use in opnenGL.
	 * 
	 * @param texPath
	 *            the path of the image file
	 * @return the ID of the texture
	 */
	private int load(String texPath) {
		int pixels[] = null;
		try {
			BufferedImage image = ImageIO.read(new File(texPath));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff) >> 0;

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		// BufferUtils.createIntBuffer(data);
		// IntBuffer dataBuffer = BufferUtils.createIntBuffer(data.length);
		// dataBuffer.put(data);
		// dataBuffer.flip();

		int tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, tex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
				BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		return tex;
	}

	/**
	 * This method loads a image file from the hard drive and converts it to an
	 * GL_TEXTURE for use in opnenGL using the passed in mode.
	 * 
	 * @param texPath
	 *            the path of the image file
	 * @param mode
	 *            the GL_MODE the texture should be drawn
	 * @return the ID of the texture
	 */
	private int load(String texPath, int mode) {
		int pixels[] = null;
		try {
			BufferedImage image = ImageIO.read(new File(texPath));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff) >> 0;

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		// BufferUtils.createIntBuffer(data);
		// IntBuffer dataBuffer = BufferUtils.createIntBuffer(data.length);
		// dataBuffer.put(data);
		// dataBuffer.flip();

		int tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, tex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
				BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		return tex;
	}

	/**
	 * Binds the texture to unit({@code unit}) for rendering.
	 * 
	 * @param unit
	 *            the unit the texture should be bind to
	 */
	public void bind(int unit) {
		if (unit > 32 || unit < 0) {
			System.err.println("Texture unit out of bounds!!!");
			return;
		}
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	/**
	 * Releases the texture bound to a unit({@code unit}).
	 * 
	 * @param unit
	 *            the unit the texture should be released from
	 */
	public void unbind(int unit) {
		if (unit > 32 || unit < 0) {
			System.err.println("Texture unit out of bounds!!!");
			return;
		}
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * @return the width of the image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height of the image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the ID of the texture
	 */
	public int getID() {
		return textureID;
	}

	/**
	 * Deletes the texture from the graphics card
	 */
	public void destroy() {
		glDeleteTextures(textureID);
	}
}

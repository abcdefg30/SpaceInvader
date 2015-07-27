package de.gyki.openglengine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * This class is used to create Buffers for use in OpenGl from simple arrays.
 * 
 * @author Flo
 * @version 1.1
 */
public class BufferUtils {

	/**
	 * Lets no one instantiate the class.
	 */
	private BufferUtils() {
	}

	/**
	 * This method creates a {@code ByteBuffer} form a byte Array.
	 * 
	 * @param array
	 *            of bytes
	 * @return the {@code ByteBuffer} created form the byte Array
	 */
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}

	/**
	 * This method creates a {@code FloatBuffer} form a float Array.
	 * 
	 * @param array
	 *            of floats
	 * @return the {@code FloatBuffer} created form the float Array
	 */
	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}

	/**
	 * This method creates a {@code IntBuffer} form a integer Array.
	 * 
	 * @param array
	 *            of integers
	 * @return the {@code IntBuffer} created form the integer Array
	 */
	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}
}

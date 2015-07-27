package de.gyki.openglengine.math;

public abstract class Vector {

	protected int Size = 0;
	protected float x = 0.0f;
	protected float y = 0.0f;
	protected float z = 0.0f;
	protected float w = 0.0f;

	public final static int X_POS = 1;
	public final static int Y_POS = 2;
	public final static int Z_POS = 3;
	public final static int W_POS = 4;

	public final static int R_POS = 1;
	public final static int G_POS = 2;
	public final static int B_POS = 3;
	public final static int A_POS = 4;

	/**
	 * Is used to add a Vector "a" to a Vector "b" (+)
	 * 
	 * @param Vector
	 *            vec
	 * @return Vector
	 */
	public Vector sum(Vector vec) {
		if (Size != vec.getSize()) {
			System.err.println("Different sizes at addition!!!");
			return null;
		}
		Vector result = null;
		switch (vec.getSize()) {
		case 2:
			result = new Vector2f(x + vec.get(X_POS), y + vec.get(Y_POS));
			break;
		case 3:
			result = new Vector3f(x + vec.get(X_POS), y + vec.get(Y_POS), z + vec.get(Z_POS));
			break;
		case 4:
			result = new Vector4f(x + vec.get(X_POS), y + vec.get(Y_POS), z + vec.get(Z_POS), w + vec.get(W_POS));
			break;
		}

		return result;
	}

	/**
	 * Is used to subtract a Vector "b" from a Vector "a" (-)
	 * 
	 * @param Vector
	 *            vec
	 * @return Vector
	 */

	public Vector sub(Vector vec) {
		if (Size != vec.getSize()) {
			System.err.println("Differnt sizes at subtraction!!!");
			return null;
		}
		Vector result = null;
		switch (vec.getSize()) {
		case 2:
			result = new Vector2f(x - vec.get(X_POS), y - vec.get(Y_POS));
			break;
		case 3:
			result = new Vector3f(x - vec.get(X_POS), y - vec.get(Y_POS), z - vec.get(Z_POS));
			break;
		case 4:
			result = new Vector4f(x - vec.get(X_POS), y - vec.get(Y_POS), z - vec.get(Z_POS), w - vec.get(W_POS));
			break;
		}

		return result;
	}

	/**
	 * Is used to multiply a Vector with a scalar "sca"
	 * 
	 * @param float
	 *            scalar
	 * @return Vector
	 */

	public Vector mul(float sca) {
		Vector result = null;
		switch (Size) {
		case 2:
			result = new Vector2f(x * sca, y * sca);
			break;
		case 3:
			result = new Vector3f(x * sca, y * sca, z * sca);
			break;
		case 4:
			result = new Vector4f(x * sca, y * sca, z * sca, w * sca);
			break;
		}

		return result;
	}

	/**
	 * Calculates the dot product of two vectors.
	 * 
	 * @param vec
	 * @return
	 */
	public float dot(Vector vec) {
		float res = x * vec.get(X_POS) + y * vec.get(Y_POS) + z * vec.get(Z_POS) + w * vec.get(W_POS);
		return res;
	}

	/**
	 * Sets the 'X' and 'Y' Values of this Vector to x and y
	 * 
	 * @param x
	 * @param y
	 */
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the 'X', 'Y' and 'Z' Values of this Vector to x, y and z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setXYZ(float x, float y, float z) {
		if (Size < 3) {
			System.out.println("Vector is to small");
			return;
		}

		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Sets the 'X', 'Y', 'Z' and 'W' Values of this Vector to x, y, z and w.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void setXYZW(float x, float y, float z, float w) {
		if (Size < 4) {
			System.out.println("Vector is to small");
			return;
		}

		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Sets a specific value of the Vector to the input value.
	 * 
	 * @param position
	 * @param value
	 */
	public void set(int position, float value) {

		switch (position) {
		case X_POS:
			x = value;
			break;
		case Y_POS:
			y = value;
			break;
		case Z_POS:
			if (Size < 3)
				System.out.println("Index out of Bounds !");
			z = value;
			break;
		case W_POS:
			if (Size < 4)
				System.out.println("Index out of Bounds !");
			w = value;
			break;
		default:
			break;
		}

	}

	/**
	 * Returns the length of the Vector (|vector|)
	 * 
	 * @return float
	 */
	public float ammount() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Returns the float of this Vector at position "pos". Positions are defined
	 * in the Vector class.
	 * 
	 * @param int
	 *            position Valid positions(X_POS, Y_POS, Z_POS, W_POS)
	 * @return float
	 */
	public float get(int position) {

		switch (position) {
		case X_POS:
			return x;
		case Y_POS:
			return y;
		case Z_POS:
			if (Size < 3)
				System.out.println("Index out of Bounds !");
			return z;
		case W_POS:
			if (Size < 4)
				System.out.println("Index out of Bounds !");
			return w;
		default:
			return 0.0f;
		}

	}

	/**
	 * Sums a specific value of the Vector (+)
	 * 
	 * @param position
	 * @param value
	 */
	public void add(int position, float value) {

		switch (position) {
		case X_POS:
			x += value;
			break;
		case Y_POS:
			y += value;
			break;
		case Z_POS:
			if (Size < 3)
				System.out.println("Index out of Bounds !");
			z += value;
			break;
		case W_POS:
			if (Size < 4)
				System.out.println("Index out of Bounds !");
			w += value;
			break;
		default:
			System.out.println("Index out of Bounds !");
		}
	}

	/**
	 * Swaps the algebraic sign of all Components
	 */
	public void swap() {
		x *= -1.0f;
		y *= -1.0f;
		z *= -1.0f;
		w *= -1.0f;
	}

	/**
	 * @return the size of the {@code Vector}
	 */
	public int getSize() {
		return Size;
	}
}

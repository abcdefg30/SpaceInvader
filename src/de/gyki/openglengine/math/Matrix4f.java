package de.gyki.openglengine.math;

import java.nio.FloatBuffer;

import de.gyki.openglengine.util.BufferUtils;


public class Matrix4f {

	private int		Size		= 4 * 4;
	private float[]	elements	= new float[Size];

	public Matrix4f() {
		elements[0 + 0 * 4] = 1.0f;
		elements[1 + 1 * 4] = 1.0f;
		elements[2 + 2 * 4] = 1.0f;
		elements[3 + 3 * 4] = 1.0f;
	}

	public Matrix4f adi( Matrix4f mat ) {
		Matrix4f result = new Matrix4f();

		float sum = 0.0f;
		for ( int c = 0; c < 4; c++ ) {
			for ( int d = 0; d < 4; d++ ) {
				for ( int k = 0; k < 4; k++ ) {
					sum = sum + elements[c + k * 4] * mat.elements[k + d * 4];
				}

				result.elements[c + d * 4] = sum;
				sum = 0;
			}
		}

//		for ( int i = 0; i < 4; i++ ) {
//			String tmp = "";
//			for ( int e = 0; e < 4; e++ ) {
//				tmp += result.elements[e + i * 4] + ", ";
//			}
//			System.out.println("colum " + i + "[" + tmp);
//		}

		return result;
	}

	public Vector4f sum( Vector4f vec ) {

		float x = 0.0f;
		for ( int i = 0; i < 4; i++ ) {
			x += elements[0 + i * 4] * vec.get(i + 1);
		}

		float y = 0.0f;
		for ( int i = 0; i < 4; i++ ) {
			y += elements[1 + i * 4] * vec.get(i + 1);
		}

		float z = 0.0f;
		for ( int i = 0; i < 4; i++ ) {
			z += elements[2 + i * 4] * vec.get(i + 1);
		}

		float w = 0.0f;
		for ( int i = 0; i < 4; i++ ) {
			w += elements[3 + i * 4] * vec.get(i + 1);
		}

		Vector4f result = new Vector4f(x, y, z, w);

		return result;
	}

	public static Matrix4f createOrtographicMatrix( float left, float right, float bottom, float top, float near,
			float far ) {
		Matrix4f result = new Matrix4f();

		result.elements[0 + 0 * 4] = 2.0F / (right - left);
		result.elements[1 + 1 * 4] = 2.0F / (top - bottom);
		result.elements[2 + 3 * 4] = -2.0F / (far - near);

		result.elements[3 + 0 * 4] = -((right + left) / (right - left));
		result.elements[3 + 1 * 4] = -((top + bottom) / (top - bottom));
		result.elements[3 + 2 * 4] = ((far + near) / (far - near));

		return result;
	}

	public static Matrix4f createTranslationMatrix4f( Vector vec ) {
		Matrix4f result = new Matrix4f();

		result.elements[0 + 3 * 4] = vec.get(Vector.X_POS);
		result.elements[1 + 3 * 4] = vec.get(Vector.Y_POS);
		result.elements[2 + 3 * 4] = vec.get(Vector.Z_POS);

		return result;
	}

	public static Matrix4f createScaleMatrix4f( Vector vec ) {
		Matrix4f result = new Matrix4f();

		result.elements[0 + 0 * 4] = vec.get(Vector.X_POS);
		result.elements[1 + 1 * 4] = vec.get(Vector.Y_POS);
		// result.elements[2 + 2 * 4] = vec.get(Vector.Z_POS);

		return result;
	}

	public static Matrix4f createRotationMatrix4f( float angle ) {
		Matrix4f result = new Matrix4f();

		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		result.elements[0 + 0 * 4] = cos;
		result.elements[0 + 1 * 4] = -sin;

		result.elements[1 + 0 * 4] = sin;
		result.elements[1 + 1 * 4] = cos;

		return result;
	}

	public void set( int pos, float value ) {
		if ( pos >= Size ) {
			System.err.println("Index out of Bounds!!!");
			return;
	}

		elements[pos] = value;
	}

	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
}

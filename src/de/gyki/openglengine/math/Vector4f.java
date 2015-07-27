package de.gyki.openglengine.math;

public class Vector4f extends Vector {

	public Vector4f() {
		super.Size = 4;
		super.x = 0.0f;
		super.y = 0.0f;
		super.z = 0.0f;
		super.w = 0.0f;
	}

	public Vector4f(float c) {
		super.Size = 4;
		super.x = c;
		super.y = c;
		super.z = c;
		super.w = c;
	}

	public Vector4f(float x, float y) {
		super.Size = 4;
		super.x = x;
		super.y = y;
		super.z = 0.0f;
		super.w = 0.0f;
	}

	public Vector4f(float x, float y, float z) {
		super.Size = 4;
		super.x = x;
		super.y = y;
		super.z = z;
		super.w = 0.0f;
	}

	public Vector4f(float x, float y, float z, float w) {
		super.Size = 4;
		super.x = x;
		super.y = y;
		super.z = z;
		super.w = w;
	}

	public Vector4f(Vector3f vec, float w) {
		super.Size = 4;
		super.x = vec.get(Vector.X_POS);
		super.y = vec.get(Vector.Y_POS);
		super.z = vec.get(Vector.Z_POS);
		super.w = w;
	}

}

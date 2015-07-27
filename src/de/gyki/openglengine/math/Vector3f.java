package de.gyki.openglengine.math;

public class Vector3f extends Vector {

	public Vector3f() {
		super.Size = 3;
		super.x = 0.0f;
		super.y = 0.0f;
		super.z = 0.0f;
	}

	public Vector3f(float c) {
		super.Size = 3;
		super.x = c;
		super.y = c;
		super.z = c;
	}

	public Vector3f(float x, float y) {
		super.Size = 3;
		super.x = x;
		super.y = y;
		super.z = 0.0f;
	}

	public Vector3f(float x, float y, float z) {
		super.Size = 3;
		super.x = x;
		super.y = y;
		super.z = z;
	}

	public Vector3f(Vector2f vec, float z) {
		super.Size = 3;
		super.x = vec.get(Vector.X_POS);
		super.y = vec.get(Vector.Y_POS);
		super.z = z;
	}

}

package de.gyki.openglengine.math;

public class Vector2f extends Vector {

	public Vector2f() {
		super.Size = 2;
		super.x = 0.0f;
		super.y = 0.0f;
	}
	
	public Vector2f(float c) {
		super.Size = 2;
		super.x = c;
		super.y = c;
	}
	
	public Vector2f( float x, float y ) {
		super.Size = 2;
		super.x = x;
		super.y = y;
	}

}

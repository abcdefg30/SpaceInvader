package de.gyki.openglengine.physics;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector4f;

public class BoundingBox {

	private Vector4f position;
	private Vector2f lT, rB;
	private final Vector2f calibrateVector = new Vector2f(1.0f, 0.0f);
	private float width, height;

	public BoundingBox(Vector4f position, float width, float height) {
		lT = new Vector2f(position.get(Vector.X_POS) - width, position.get(Vector.Y_POS) + height);
		rB = new Vector2f(position.get(Vector.X_POS) + width, position.get(Vector.Y_POS) - height);
		this.position = position;
		this.width = width;
		this.height = height;
		// System.out.println("Width = " + width);
		// System.out.println("Height = " + height);
		// System.out.println("Center = (" + position.get(Vector.X_POS) + "/" +
		// position.get(Vector.Y_POS) + ")");
		// System.out.println("lT = (" + lT.get(Vector.X_POS) + "/" +
		// lT.get(Vector.Y_POS) + ")");
		// System.out.println("rB = (" + rB.get(Vector.X_POS) + "/" +
		// rB.get(Vector.Y_POS) + ")");
	}

	public float[] MYintersection(BoundingBox bc) {
		float[] ret = new float[1];
		if (bc.getPosition().get(Vector.Z_POS) != position.get(Vector.Z_POS))
			return ret;

		float xDis = position.get(Vector.X_POS) - bc.position.get(Vector.X_POS);
		float yDis = position.get(Vector.Y_POS) - bc.position.get(Vector.Y_POS);

		float xDif = Math.abs(xDis) - (width + bc.width);
		float yDif = Math.abs(yDis) - (height + bc.height);

		if (xDif <= 0 && yDif <= 0) {
			ret[0] = 1;
		}

		return ret;
	}

	public float[] intersection(BoundingBox bc, Vector2f playerDir) {
		float[] ret = new float[3];
		if (bc.getPosition().get(Vector.Z_POS) != position.get(Vector.Z_POS))
			return ret;

		float xDis = position.get(Vector.X_POS) - bc.position.get(Vector.X_POS);
		float yDis = position.get(Vector.Y_POS) - bc.position.get(Vector.Y_POS);

		float xDif = Math.abs(xDis) - (width + bc.width);
		float yDif = Math.abs(yDis) - (height + bc.height);

		if (xDif <= 0 && yDif <= 0) {
			ret[0] = 1;

			// Calculating the move direction to bc
			if (playerDir.get(Vector.X_POS) < 0)
				ret[1] = -Mesh.X_DIR;
			if (playerDir.get(Vector.X_POS) > 0)
				ret[1] = Mesh.X_DIR;

			if (playerDir.get(Vector.Y_POS) < 0)
				ret[1] = -Mesh.Y_DIR;
			if (playerDir.get(Vector.Y_POS) > 0)
				ret[1] = Mesh.Y_DIR;

			if (playerDir.get(Vector.X_POS) == 0 && playerDir.get(Vector.Y_POS) == 0) {
				ret[1] = Mesh.Y_DIR;
				System.err.println("Warning: 0 movement in collision");
			}

			float xDepth = 0.0f;
			float yDepth = 0.0f;
			if (playerDir.get(Vector.X_POS) != 0 && playerDir.get(Vector.Y_POS) != 0) {
				// float xDepth = 0.0f;
				// float yDepth = 0.0f;

				// xDepth
				if (playerDir.get(Vector.X_POS) > 0) {
					xDepth = rB.get(Vector.X_POS) - bc.lT.get(Vector.X_POS);
				}

				if (playerDir.get(Vector.X_POS) < 0) {
					xDepth = bc.rB.get(Vector.X_POS) - lT.get(Vector.X_POS);
				}

				// yDepth
				if (playerDir.get(Vector.Y_POS) > 0) {
					yDepth = lT.get(Vector.Y_POS) - bc.rB.get(Vector.Y_POS);
				}

				if (playerDir.get(Vector.Y_POS) < 0) {
					yDepth = bc.lT.get(Vector.Y_POS) - rB.get(Vector.Y_POS);
				}

				if (yDepth > xDepth && playerDir.get(Vector.X_POS) > 0)
					ret[1] = Mesh.X_DIR;
				if (yDepth > xDepth && playerDir.get(Vector.X_POS) < 0)
					ret[1] = -Mesh.X_DIR;
				if (yDepth < xDepth && playerDir.get(Vector.Y_POS) > 0)
					ret[1] = Mesh.Y_DIR;
				if (yDepth < xDepth && playerDir.get(Vector.Y_POS) < 0)
					ret[1] = -Mesh.Y_DIR;
			}

			// Calculating the distance of Intersection
			if (ret[1] == -Mesh.X_DIR)
				ret[2] = -(lT.get(Vector.X_POS) - bc.rB.get(Vector.X_POS));

			if (ret[1] == Mesh.X_DIR)
				ret[2] = -(rB.get(Vector.X_POS) - bc.lT.get(Vector.X_POS));

			if (ret[1] == -Mesh.Y_DIR)
				ret[2] = -(rB.get(Vector.Y_POS) - bc.lT.get(Vector.Y_POS));

			if (ret[1] == Mesh.Y_DIR)
				ret[2] = -(lT.get(Vector.Y_POS) - bc.rB.get(Vector.Y_POS));

			if (ret[2] != 0 && ret[2] < 0)
				ret[2] -= 0.00001f;

			if (ret[2] != 0 && ret[2] > 0)
				ret[2] += 0.00001f;

			// System.out.println("xDis " + xDis);
			// System.out.println("yDis " + yDis);
			// System.out.println("playerDir.get(Vector.X_Pos) " +
			// playerDir.get(Vector.X_POS));
			// System.out.println("playerDir.get(Vector.Y_Pos) " +
			// playerDir.get(Vector.Y_POS));
			// System.out.println("xDepth " + xDepth);
			// System.out.println("yDepth " + yDepth);
			//
			// System.out.println("ret[1] " + ret[1]);
			// System.out.println("ret[2] " + ret[2]);

			return ret;
		}

		return ret;
	}

	public void move(Vector4f position) {
		lT = new Vector2f(position.get(Vector.X_POS) - width, position.get(Vector.Y_POS) + height);
		rB = new Vector2f(position.get(Vector.X_POS) + width, position.get(Vector.Y_POS) - height);
		this.position = position;
	}

	public Vector4f getPosition() {
		return position;
	}

	public Vector2f getLeftTop() {
		return lT;
	}

	public Vector2f getRightBottom() {
		return rB;
	}

}

package de.gyki.openglengine.physics;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector4f;

public class BoundingBox {

	/**
	 * The position of the bounding box.
	 */
	private Vector4f position;

	/**
	 * The position of the left top corner of the bounding box.
	 */
	private Vector2f lT;

	/**
	 * The position of the right bottom corner of the bounding box.
	 */
	private Vector2f rB;

	/**
	 * Half the width of the actual mesh.
	 */
	private float width;

	/**
	 * Half the height of the actual mesh.
	 */
	private float height;

	/**
	 * Creates the actual bounding box with calculating the left top and right
	 * bottom corners.
	 * 
	 * @param position
	 *            the position of the bounding box
	 * @param width
	 *            the width of the mesh
	 * @param height
	 *            the height of the mesh
	 */
	public BoundingBox(Vector4f position, float width, float height) {
		lT = new Vector2f(position.get(Vector.X_POS) - width, position.get(Vector.Y_POS) + height);
		rB = new Vector2f(position.get(Vector.X_POS) + width, position.get(Vector.Y_POS) - height);
		this.position = position;
		this.width = width / 2;
		this.height = height / 2;
		// System.out.println("Width = " + width);
		// System.out.println("Height = " + height);
		// System.out.println("Center = (" + position.get(Vector.X_POS) + "/" +
		// position.get(Vector.Y_POS) + ")");
		// System.out.println("lT = (" + lT.get(Vector.X_POS) + "/" +
		// lT.get(Vector.Y_POS) + ")");
		// System.out.println("rB = (" + rB.get(Vector.X_POS) + "/" +
		// rB.get(Vector.Y_POS) + ")");
	}

	/**
	 * Checks if the bounding box is intersecting with another bounding box (
	 * {@code bc}). The motion direction({@code motionDir}) is used to calculate
	 * the intersection depth.
	 * 
	 * @param bc
	 *            the box that should be checked for intersection
	 * @param motionDir
	 *            the motion of this bounding box
	 * @return an array covering a float if there is an intersection (0/1), a
	 *         float for the direction of intersection and a float covering the
	 *         intersection depth
	 */
	public float[] intersection(BoundingBox bc, Vector2f motionDir) {
		float[] ret = new float[3];
		if (bc.getPosition().get(Vector.Z_POS) != position.get(Vector.Z_POS))
			return ret;

		// if( (lT.get(Vector.X_POS) > bc.lT.get(Vector.X_POS) &&
		// bc.lT.get(Vector.X_POS) > rB.get(Vector.X_POS)) ||
		// (lT.get(Vector.X_POS) > bc.rB.get(Vector.X_POS) &&
		// bc.rB.get(Vector.X_POS) > rB.get(Vector.X_POS)) &&
		// (lT.get(Vector.Y_POS) > bc.lT.get(Vector.Y_POS) &&
		// bc.lT.get(Vector.Y_POS) > rB.get(Vector.Y_POS)) ||
		// (lT.get(Vector.Y_POS) > bc.rB.get(Vector.Y_POS) &&
		// bc.rB.get(Vector.Y_POS) > rB.get(Vector.Y_POS)))
		// return true;

		// System.out.println("x1 = " + lT.get(Vector.X_POS));
		// System.out.println("x2 = " + rB.get(Vector.X_POS));
		// System.out.println("x3 = " + bc.lT.get(Vector.X_POS));
		// System.out.println("x4 = " + bc.rB.get(Vector.X_POS));
		//
		// System.out.println("y1 = " + lT.get(Vector.Y_POS));
		// System.out.println("y2 = " + rB.get(Vector.Y_POS));
		// System.out.println("y3 = " + bc.lT.get(Vector.Y_POS));
		// System.out.println("y4 = " + bc.rB.get(Vector.Y_POS));
		//
		// System.out.println("x1 < x3 < x2 = " + (lT.get(Vector.X_POS) <=
		// bc.lT.get(Vector.X_POS) && bc.lT.get(Vector.X_POS) <=
		// rB.get(Vector.X_POS))); System.out.println("x1 < x4 < x2 = " +
		// (lT.get(Vector.X_POS) <= bc.rB.get(Vector.X_POS) &&
		// bc.rB.get(Vector.X_POS) <= rB.get(Vector.X_POS)));
		// System.out.println("y1 < y3 < y2 = " + (lT.get(Vector.Y_POS) >=
		// bc.lT.get(Vector.Y_POS) && bc.lT.get(Vector.Y_POS) >=
		// rB.get(Vector.Y_POS))); System.out.println("y1 < y4 < y2 = " +
		// (lT.get(Vector.Y_POS) >= bc.lT.get(Vector.Y_POS) &&
		// bc.lT.get(Vector.Y_POS) >= rB.get(Vector.Y_POS)));

		// if ( ((lT.get(Vector.X_POS) <= bc.lT.get(Vector.X_POS) &&
		// bc.lT.get(Vector.X_POS) <= rB.get(Vector.X_POS))
		// || (lT.get(Vector.X_POS) <= bc.rB.get(Vector.X_POS) &&
		// bc.rB.get(Vector.X_POS) <= rB.get(Vector.X_POS)))
		// &&
		// ((lT.get(Vector.Y_POS) >= bc.lT.get(Vector.Y_POS) &&
		// bc.lT.get(Vector.Y_POS) >= rB.get(Vector.Y_POS))
		// ||
		// (lT.get(Vector.Y_POS) >= bc.rB.get(Vector.Y_POS) &&
		// bc.rB.get(Vector.Y_POS) >= rB.get(Vector.Y_POS))) )
		// return 1;
		//
		// // other way around
		// if ( ((bc.lT.get(Vector.X_POS) <= lT.get(Vector.X_POS) &&
		// lT.get(Vector.X_POS) <= bc.rB.get(Vector.X_POS))
		// || (bc.lT.get(Vector.X_POS) <= rB.get(Vector.X_POS) &&
		// rB.get(Vector.X_POS) <= bc.rB.get(Vector.X_POS)))
		// &&
		// ((bc.lT.get(Vector.Y_POS) >= lT.get(Vector.Y_POS) &&
		// lT.get(Vector.Y_POS) >= bc.rB.get(Vector.Y_POS))
		// ||
		// (bc.lT.get(Vector.Y_POS) >= rB.get(Vector.Y_POS) &&
		// rB.get(Vector.Y_POS) >= bc.rB.get(Vector.Y_POS))) )
		// return 1;

		float xDis = position.get(Vector.X_POS) - bc.position.get(Vector.X_POS);
		float yDis = position.get(Vector.Y_POS) - bc.position.get(Vector.Y_POS);

		float xDif = Math.abs(xDis) - (width + bc.width);
		float yDif = Math.abs(yDis) - (height + bc.height);

		if (xDif <= 0 && yDif <= 0) {
			ret[0] = 1;

			// Calculating the move direction to bc
			if (motionDir.get(Vector.X_POS) < 0)
				ret[1] = -Mesh.X_DIR;
			if (motionDir.get(Vector.X_POS) > 0)
				ret[1] = Mesh.X_DIR;

			if (motionDir.get(Vector.Y_POS) < 0)
				ret[1] = -Mesh.Y_DIR;
			if (motionDir.get(Vector.Y_POS) > 0)
				ret[1] = Mesh.Y_DIR;

			if (motionDir.get(Vector.X_POS) == 0 && motionDir.get(Vector.Y_POS) == 0) {
				ret[1] = Mesh.Y_DIR;
				System.err.println("Warning: 0 mouvement in collision");
			}

			float xDepth = 0.0f;
			float yDepth = 0.0f;
			if (motionDir.get(Vector.X_POS) != 0 && motionDir.get(Vector.Y_POS) != 0) {
				// float xDepth = 0.0f;
				// float yDepth = 0.0f;

				// xDepth
				if (motionDir.get(Vector.X_POS) > 0) {
					xDepth = rB.get(Vector.X_POS) - bc.lT.get(Vector.X_POS);
					System.out.println("Right");
				}

				if (motionDir.get(Vector.X_POS) < 0) {
					xDepth = bc.rB.get(Vector.X_POS) - lT.get(Vector.X_POS);
					System.out.println("Left");
				}

				// yDepth
				if (motionDir.get(Vector.Y_POS) > 0) {
					yDepth = lT.get(Vector.Y_POS) - bc.rB.get(Vector.Y_POS);
					System.out.println("Up");
				}

				if (motionDir.get(Vector.Y_POS) < 0) {
					yDepth = bc.lT.get(Vector.Y_POS) - rB.get(Vector.Y_POS);
					System.out.println("Down");
				}

				if (yDepth > xDepth && motionDir.get(Vector.X_POS) > 0)
					ret[1] = Mesh.X_DIR;
				if (yDepth > xDepth && motionDir.get(Vector.X_POS) < 0)
					ret[1] = -Mesh.X_DIR;
				if (yDepth < xDepth && motionDir.get(Vector.Y_POS) > 0)
					ret[1] = Mesh.Y_DIR;
				if (yDepth < xDepth && motionDir.get(Vector.Y_POS) < 0)
					ret[1] = -Mesh.Y_DIR;
			}

			// Calculating the distance of Intersection
			if (ret[1] == -Mesh.X_DIR)
				ret[2] = (-(lT.get(Vector.X_POS) - bc.rB.get(Vector.X_POS))) - 1;

			if (ret[1] == Mesh.X_DIR)
				ret[2] = (-(rB.get(Vector.X_POS) - bc.lT.get(Vector.X_POS))) + 1;

			if (ret[1] == -Mesh.Y_DIR)
				ret[2] = (-(rB.get(Vector.Y_POS) - bc.lT.get(Vector.Y_POS))) - 1;

			if (ret[1] == Mesh.Y_DIR)
				ret[2] = (-(lT.get(Vector.Y_POS) - bc.rB.get(Vector.Y_POS))) + 1;

			if (ret[2] != 0 && ret[2] < 0)
				ret[2] -= 0.00001f;

			if (ret[2] != 0 && ret[2] > 0)
				ret[2] += 0.00001f;

			// System.out.println("xDis " + xDis);
			// System.out.println("yDis " + yDis);
			// System.out.println("motionDir.get(Vector.X_Pos) " +
			// motionDir.get(Vector.X_POS));
			// System.out.println("motionDir.get(Vector.Y_Pos) " +
			// motionDir.get(Vector.Y_POS));
			// System.out.println("xDepth " + xDepth);
			// System.out.println("yDepth " + yDepth);
			//
			// System.out.println("ret[1] " + ret[1]);
			// System.out.println("ret[2] " + ret[2]);

			return ret;
		}

		return ret;
	}

	/**
	 * Moves the bounding box to the inserted position.
	 * 
	 * @param position
	 *            the bounding box should be moved to
	 */
	public void move(Vector4f position) {
		lT = new Vector2f(position.get(Vector.X_POS) - width, position.get(Vector.Y_POS) + height);
		rB = new Vector2f(position.get(Vector.X_POS) + width, position.get(Vector.Y_POS) - height);
		this.position = position;
	}

	/**
	 * @return the position of the bounding box.
	 */
	public Vector4f getPosition() {
		return position;
	}

	/**
	 * @return the position of the left top corner.
	 */
	public Vector2f getLeftTop() {
		return lT;
	}

	/**
	 * @return the position of the right bottom corner.
	 */
	public Vector2f getRightBottom() {
		return rB;
	}

}

package de.gyki.openglengine.physics;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.math.Vector4f;
import de.gyki.openglengine.mesh.Player;

public class PhysicEngine {

	private BoundingBox[] stackB;
	private Mesh[] meshes;
	private Player player;
	private int countB;
	private float[][][] lastCollision;
	private boolean gravity, jump, jumpBlock, jumpMode;
	private boolean firstJump = true;
	private float gravitySpeed = -0.2f;
	private float jumpHeight = 0.0f;

	// the calculated movement
	private Vector3f playerMovement;

	public PhysicEngine() {
		stackB = new BoundingBox[101];
		countB = 0;
		playerMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	}

	public void init(Mesh[] obstacles, Player player, boolean gravity, float gravitySpeed, boolean jumpMode) {
		this.gravity = gravity;
		this.gravitySpeed = gravitySpeed;
		this.jumpMode = jumpMode;
		if (gravity == true)
			this.addPlayerMovement(new Vector2f(0.0f, gravitySpeed));

		this.player = player;
		this.meshes = obstacles;

		registerBox(player.getPosition(), player.getWidth(), player.getHeight());
		for (Mesh m : meshes)
			registerBox(m.getPosition(), m.getWidth(), m.getHeight());
	}

	/**
	 * Moves an boundingbox to another position.
	 * 
	 * @param pos
	 *            {@code int} The position of the boundingbox in the stack
	 * @param position
	 *            {@code Vector4f} the position it should be moved to
	 */
	public void updatePositionB(int pos, Vector4f position) {
		if (pos >= 101)
			return;
		stackB[pos].move(position);
	}

	/**
	 * Registers a new boundingbox in the stack
	 * 
	 * @param position
	 *            {@code Vector4f} The position the boundingbox has in space
	 * @param width
	 *            {@code float} The width of the boundignbox
	 * @param height
	 *            {@code float} The height of the bounding box
	 */
	public void registerBox(Vector4f position, float width, float height) {
		if (countB >= 101)
			return;

		stackB[countB] = new BoundingBox(position, width, height);
		countB++;
	}

	/**
	 * Checks for collisions and stores them in a three dimensional float array.
	 * 
	 * @return The collisions array
	 */
	public float[][][] updateB() {
		lastCollision = new float[1][countB][3];

		for (int e = 1; e < lastCollision[0].length; e++) {
			lastCollision[0][e] = stackB[0].intersection(stackB[e],
					new Vector2f(playerMovement.get(Vector.X_POS), playerMovement.get(Vector.Y_POS)));
		}

		return lastCollision;
	}

	public void addPlayerMovement(Vector2f vec) {
		playerMovement = (Vector3f) playerMovement.sum(new Vector3f(vec, 0.0f));
	}

	public void remPlayerMovement(Vector2f vec) {
		playerMovement = (Vector3f) playerMovement.sub(vec);
	}

	public void jump(float height) {
		if (gravity == true && jumpBlock == false)
			jump = true;
		this.jumpHeight = height;
		this.jumpMode = jumpMode;
		this.firstJump = false;
	}

	/**
	 * Moves the player about the calculated movement({@code playerMovement)},
	 * if there is a collision it is moved back.
	 * 
	 * @return The collision array
	 * 
	 */
	private float[][][] movePlayer() {

		player.move(playerMovement);
		updatePositionB(0, player.getPosition());

		float[][][] collision = updateB();

		for (int i = 1; i < lastCollision[0].length; i++) {
			if (lastCollision[0][i][0] != 0) {

				Vector3f playerBackMove = null;
				if (lastCollision[0][i][1] == Mesh.X_DIR || lastCollision[0][i][1] == -Mesh.X_DIR)
					playerBackMove = new Vector3f(lastCollision[0][i][2], 0.0f, 0.0f);
				if (lastCollision[0][i][1] == Mesh.Y_DIR || lastCollision[0][i][1] == -Mesh.Y_DIR)
					playerBackMove = new Vector3f(0.0f, lastCollision[0][i][2], 0.0f);

				player.move(playerBackMove);

			}
		}

		return collision;
	}

	/**
	 * Updates the collisions. And handles the jump.
	 * 
	 * @return The collision array
	 */
	public float[][][] update() {

		jumpBlock = true; // jumpBlock is by default true;
		if (jumpMode == true && firstJump == true)
			jumpBlock = false;

		// if a jump is requested using the jump method the playerMovement is
		// set to move up
		if (jump == true) {
			playerMovement.set(Vector.Y_POS, jumpHeight);
			jump = false;
		}

		// if the player does not move down at gravitySpeed he is forced down
		if (playerMovement.get(Vector.Y_POS) > gravitySpeed && gravity == true) {
			playerMovement.set(Vector.Y_POS, playerMovement.get(Vector.Y_POS) - 0.05f);
		}

		// if the jumps are possible during falling, it checks if the player is
		// falling to set the jumpBlock
		if (playerMovement.get(Vector.Y_POS) < (jumpHeight / 2) && jumpMode == true) {
			jumpBlock = false;
		}

		// if the player is moving faster down than gravitySpeed the speed is
		// set to gravitySpeed
		if (playerMovement.get(Vector.Y_POS) < gravitySpeed && gravity == true)
			playerMovement.set(Vector.Y_POS, gravitySpeed);

		float[][][] collisions = movePlayer(); // COLLISION CALCULATION

		for (int s = 1; s < collisions[0].length; s++) {
			if (collisions[0][s][0] != 0) {
				// if jumps are only possible on the ground, it checks if the
				// player is on the ground to set the jumpBlock
				if (jumpMode == false) {
					if (collisions[0][s][1] == -Mesh.Y_DIR)
						jumpBlock = false;
				}

				// if the player hits a obstacle from beneath the jump stops
				if (jumpBlock == true) {
					if (collisions[0][s][1] == Mesh.Y_DIR)
						playerMovement.set(Vector.Y_POS, gravitySpeed);
				}
			}
		}

		return collisions;
	}

	/**
	 * Destroys the meshes.
	 */
	public void destroy() {
		for (Mesh m : meshes)
			m.destroy();
	}
}

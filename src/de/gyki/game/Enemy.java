package de.gyki.game;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.mesh.*;

public class Enemy {

	public int health;

	public boolean Shooting = false;
	private Mesh[] shots = new Mesh[100];
	private boolean[] shot = new boolean[100];
	private int cooldown = 1;
	private Rect rect;

	public Mesh getRect() {
		return rect;
	}

	public Enemy(Vector3f position, Vector2f scale, String textureName) {
		rect = new Rect(position, scale, textureName);

		for (int i = 0; i < shots.length; i++) {
			shots[i] = new Rect(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.1f, 0.5f), "Enemy_Shot.png");
			shot[i] = false;
		}
	}

	private int wiggl = 0;
	private boolean wigR = true;

	public void update() {
		float x = 0.0f;
		if (wigR) {
			x = 0.03f;
			if (wiggl++ == 30) {
				wigR = false;
				x = -0.03f;
			}
		} else {
			x = -0.03f;
			if (wiggl-- == 0) {
				wigR = true;
				x = 0.03f;
			}
		}

		rect.move(new Vector3f(x, -0.005f, 0.0f));

		if (Math.random() > 0.85)
			Shooting = true;

		if (Shooting && cooldown-- == 0)
			shoot();

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].move(new Vector3f(0.0f, -0.1f, 0.0f));
			if (shots[i].getPosition().get(Vector.Y_POS) <= -9.0f)
				shot[i] = false;
		}
	}

	public void draw() {
		rect.draw();

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].draw();
		}
	}

	public void destroy() {
		rect.destroy();

		for (Mesh m : shots)
			m.destroy();
	}

	private void shoot() {
		for (int i = 0; i < shot.length; i++) {
			if (shot[i])
				continue;

			cooldown = 60;
			Shooting = false;
			shot[i] = true;
			shots[i].setPosition(new Vector3f(rect.getPosition().get(Vector.X_POS),
					rect.getPosition().get(Vector.Y_POS), rect.getPosition().get(Vector.Z_POS)));
			break;
		}
	}
}

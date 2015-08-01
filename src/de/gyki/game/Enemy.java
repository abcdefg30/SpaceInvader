package de.gyki.game;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.mesh.*;

public class Enemy {
	public boolean BottomPosition = false;
	public boolean Shooting = false;
	public Mesh[] shots = new Mesh[3];
	public boolean[] shot = new boolean[3];
	private int cooldown = 1;
	private Rect rect;
	public boolean render = true;

	public Mesh getRect() {
		return rect;
	}

	public Enemy(Vector3f position, Vector2f scale, String textureName) {
		rect = new Rect(position, scale, textureName);

		for (int i = 0; i < shots.length; i++) {
			shots[i] = new Rect(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.23f, 0.4f), "Enemy_Shot.png");
			shot[i] = false;
		}
	}

	private int wiggl = 0;
	private boolean wigR = true;

	public void update() {
		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].move(new Vector3f(0.0f, -0.1f, 0.0f));
			if (shots[i].getPosition().get(Vector.Y_POS) <= -10.0f)
				shot[i] = false;
		}

		if (!render)
			return;

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
		if (rect.getPosition().get(Vector.Y_POS) <= -10.0f)
			render = false;

		if (Math.random() > 0.99 && Math.random() > 0.75)
			Shooting = true;

		if (Shooting && cooldown-- == 0)
			shoot();
	}

	public void draw() {
		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].draw();
		}

		if (!render)
			return;

		rect.draw();
	}

	public void destroy() {
		rect.destroy();

		for (Mesh m : shots)
			m.destroy();
	}

	public void Kill() {
		render = false;
		rect.move(new Vector3f(0.0f, 0.0f, 1.0f));
	}

	private void shoot() {
		for (int i = 0; i < shot.length; i++) {
			if (shot[i])
				continue;

			cooldown = 60;
			Shooting = false;
			shot[i] = true;
			shots[i].move(new Vector3f(rect.getPosition().get(Vector.X_POS) - shots[i].getPosition().get(Vector.X_POS),
					rect.getPosition().get(Vector.Y_POS) - rect.getHeight() - shots[i].getPosition().get(Vector.Y_POS),
					rect.getPosition().get(Vector.Z_POS) - shots[i].getPosition().get(Vector.Z_POS)));
			break;
		}
	}
}

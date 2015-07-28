package de.gyki.game;

import de.gyki.openglengine.core.MeshHandler;
import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.graphics.Shader;
import de.gyki.openglengine.graphics.Texture;
import de.gyki.openglengine.graphics.VertexArray;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.math.Vector4f;
import de.gyki.openglengine.mesh.*;
import de.gyki.openglengine.util.OpenGLErrorCatcher;

public class Enemy {

	public int health;

	private Rect rect;

	public Mesh getRect() {
		return rect;
	}

	public Enemy(Vector3f position, Vector2f scale, String textureName) {
		rect = new Rect(position, scale, textureName);
	}

	private int wiggl = 0;
	private boolean wigR = true;

	public void update() {
		if (wigR) {
			rect.move(new Vector3f(0.03f, 0.0f, 0.0f));
			wiggl++;
			if (wiggl == 30) {
				wigR = false;
				rect.move(new Vector3f(-0.03f, 0.0f, 0.0f));
			}
		} else {
			rect.move(new Vector3f(-0.03f, 0.0f, 0.0f));
			wiggl--;
			if (wiggl == 0) {
				wigR = true;
				rect.move(new Vector3f(0.03f, 0.0f, 0.0f));
			}
		}

		rect.move(new Vector3f(0.0f, -0.005f, 0.0f));
	}

	public void draw() {
		rect.draw();
	}

	public void destroy() {
		rect.destroy();
	}

	public void attack(Vector2f vec) {
		MeshHandler.PhysicEngine.addPlayerMovement(vec);
		// vec = (Vector2f) walk.sum(vec);
	}
}

package de.gyki.game;

import de.gyki.openglengine.core.MeshHandler;
import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.mesh.Player;
import de.gyki.openglengine.mesh.Rect;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Projekttage extends Game {

	public boolean GameOver = false;
	public boolean Shooting = false;
	private Mesh[] shots = new Mesh[20];
	private boolean[] shot = new boolean[20];
	private int cooldown = 1;

	private Player player;
	private Mesh over;
	private Enemy[] enemys = new Enemy[15];
	private Mesh[] meshes;

	// x-Achse von -16 bis 16
	// y-Achse von -9 bis 9
	// z-Achse beliebig viele, aber nicht unter -9
	@Override
	public void init() {
		player = new Player(new Vector3f(0.0f, -7.0f, -1.0f), new Vector2f(1.0f, 1.0f));
		over = new Rect(new Vector3f(0.0f, 0.0f, -1.0f), new Vector2f(16.0f, 5.0f), "mission accomplished.png");
		meshes = new Mesh[enemys.length + shots.length];

		for (int i = 0; i < shots.length; i++) {
			shots[i] = new Rect(new Vector3f(0.0f, -7.2f, 0.0f), new Vector2f(0.1f, 0.5f), "Schuss.png");
			shot[i] = false;

			meshes[i] = shots[i];
		}

		String name = Math.random() > 0.5 ? "Enemy_1.png" : "Enemy_2.png";
		for (int i = 0; i < enemys.length; i++) {
			enemys[i] = new Enemy(new Vector3f(-14.5f + (i * 2), 5.0f, -1.0f), new Vector2f(1.0f, 1.0f), name);

			meshes[shots.length + i] = enemys[i].getRect();
		}

		MeshHandler.PhysicEngine.init(meshes, player, false, 0.0f, false);
	}

	@Override
	public void render() {
		if (GameOver) {
			over.draw();
			return;
		}

		player.draw();

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].draw();
		}

		for (int i = 0; i < enemys.length; i++)
			if (enemys[i] != null)
				enemys[i].draw();
	}

	@Override
	public void update(float[][][] collisions) {
		if (GameOver)
			return;

		float xpos = player.getPosition().get(Vector.X_POS);
		if (xpos > 15.0f || xpos < -15.0f)
			player.walk(new Vector2f(-player.getWalk().get(Vector.X_POS), 0.0f));

		if (Shooting && cooldown-- == 0)
			Shoot();

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].move(new Vector3f(0.0f, 0.1f, 0.0f));
			if (shots[i].getPosition().get(Vector.Y_POS) >= 9.0f)
				shot[i] = false;
		}

		for (Enemy e : enemys) {
			if (e != null)
				e.update();

			// if (e.getPosition().get(Vector.Y_POS) <=
			// player.getPosition().get(Vector.Y_POS))
			// GameOver = true;
		}
	}

	@Override
	public void updateKeyPress(int key) {
		if (key == GLFW_KEY_RIGHT)
			movePlayer(1);

		if (key == GLFW_KEY_LEFT)
			movePlayer(-1);

		if (key == GLFW_KEY_SPACE)
			Shooting = true;
	}

	@Override
	public void updateKeyReleas(int key) {
		if (key == GLFW_KEY_RIGHT)
			stopPlayer(-1);

		if (key == GLFW_KEY_LEFT)
			stopPlayer(1);

		if (key == GLFW_KEY_SPACE)
			Shooting = false;
	}

	@Override
	public void destroy() {
		player.destroy();
		over.destroy();
		for (Enemy e : enemys) {
			if (e != null)
				e.destroy();
		}
		for (Mesh m : shots)
			m.destroy();
	}

	public void Shoot() {
		for (int i = 0; i < shot.length; i++) {
			if (shot[i])
				continue;

			cooldown = 20;
			shot[i] = true;
			shots[i].setPosition(new Vector3f(player.getPosition().get(Vector.X_POS),
					player.getPosition().get(Vector.Y_POS), player.getPosition().get(Vector.Z_POS)));
			break;
		}
	}

	private void movePlayer(int a) {
		float x = a * 0.1f;
		float xpos = player.getPosition().get(Vector.X_POS);
		if (xpos > 15.0f && x > 0.0f || xpos < -15.0f && x < 0.0f)
			return;

		player.walk(new Vector2f(x, 0.0f));
	}

	private void stopPlayer(int a) {
		player.walk(new Vector2f(-player.getWalk().get(Vector.X_POS), 0.0f));
	}
}

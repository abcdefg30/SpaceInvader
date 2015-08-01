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
	private int numLines = 3;

	private Player player;
	private Mesh over;
	private Enemy[][] enemys = new Enemy[numLines][15];

	private Mesh[] meshes;
	private int meshCounter = 0;

	// x-Achse von -16 bis 16
	// y-Achse von -9 bis 9
	// z-Achse beliebig viele, aber nicht unter -9
	@Override
	public void init() {
		player = new Player(new Vector3f(0.0f, -7.0f, -1.0f), new Vector2f(0.6f, 0.7f));
		over = new Rect(new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(16.0f, 5.0f), "mission accomplished.png");
		meshes = new Mesh[enemys[0].length * numLines * 4 + shots.length];

		for (int i = 0; i < shots.length; i++) {
			shots[i] = new Rect(new Vector3f(0.0f, -9.0f, 0.0f), new Vector2f(0.1f, 0.5f), "Schuss.png");
			shot[i] = false;

			meshes[meshCounter] = shots[i];
			meshCounter++;
		}

		createLine();
		MeshHandler.PhysicEngine.init(meshes, player, false, 0.0f, 0.0f, false);
	}

	@Override
	public void render() {
		if (GameOver) {
			over.draw();
			return;
		}

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].draw();
		}

		for (int j = 0; j < numLines; j++)
			for (int i = 0; i < enemys[j].length; i++)
				if (enemys[j][i] != null)
					enemys[j][i].draw();

		player.draw();
	}

	@Override
	public void update(float[][][] collisions) {
		if (GameOver)
			return;

		// 0 ist 'player'
		// 1 bis 20 sind 'shots'
		// 21 ist 'Enemy'
		// 22 bis 24 sind 'Enemy shots'
		// 25 ist 'Enemy' usw...
		for (int f = 0; f < collisions.length; f++) {
			for (int g = 0; g < collisions[f].length; g++) {
				if (f != g && collisions[f][g][0] != 0) {
					if (g > 20) {
						if (f == 0) {
							GameOver = true;
						} else if (f < 21) {
							int x = g - 21;
							// true für 'Enemy', ansonsten 'Enemy_shot'
							boolean a = x % 4 == 0;
							int jj = 0;
							int ii = 0;
							if (x % 4 == 0) {
								x /= 4;
								int j = (x / 15) < 2f ? ((x / 15) < 1f ? 0 : 1) : 2;
								int i = x - (j * 15);
								jj = j;
								ii = i;
								enemys[j][i].Kill();
							} else {
								int y = x % 4;
								x -= y;
								x /= 4;
								int j = (x / 15) < 2f ? ((x / 15) < 1f ? 0 : 1) : 2;
								int i = x - (j * 15);
								jj = j;
								ii = i;
								enemys[j][i].shot[y - 1] = false;
								enemys[j][i].shots[y - 1].move(new Vector3f(0.0f, 0.0f, 1.0f));
							}

							shot[f - 1] = false;
							shots[f - 1].move(new Vector3f(0.0f, 0.0f, -1.0f));
						}
					}
				}
			}
		}

		float xpos = player.getPosition().get(Vector.X_POS);
		if (xpos > 15.0f || xpos < -15.0f)
			player.walk(new Vector2f(-player.getWalk().get(Vector.X_POS), 0.0f));

		if (Shooting && cooldown-- == 0)
			Shoot();

		if (!Shooting && cooldown > 0)
			cooldown--;

		for (int i = 0; i < shot.length; i++) {
			if (!shot[i])
				continue;

			shots[i].move(new Vector3f(0.0f, 0.1f, 0.0f));
			if (shots[i].getPosition().get(Vector.Y_POS) >= 9.0f)
				shot[i] = false;
		}

		for (int j = 0; j < numLines; j++)
			for (Enemy e : enemys[j])
				if (e != null)
					e.update();
	}

	@Override
	public void updateKeyPress(int key) {
		if (GameOver)
			return;

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

		for (int j = 0; j < numLines; j++)
			for (Enemy e : enemys[j])
				if (e != null)
					e.destroy();

		for (Mesh m : shots)
			m.destroy();
	}

	public void Shoot() {
		for (int i = 0; i < shot.length; i++) {
			if (shot[i])
				continue;

			cooldown = 20;
			shot[i] = true;
			shots[i].move(
					new Vector3f(player.getPosition().get(Vector.X_POS) - shots[i].getPosition().get(Vector.X_POS),
							player.getHeight() + player.getPosition().get(Vector.Y_POS)
									- shots[i].getPosition().get(Vector.Y_POS),
					player.getPosition().get(Vector.Z_POS) - shots[i].getPosition().get(Vector.Z_POS)));
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

	private void createLine() {
		for (int j = 0; j < numLines; j++) {
			String name = Math.random() > 0.5 ? "Enemy_1.png" : "Enemy_2.png";
			for (int i = 0; i < enemys[0].length; i++) {
				enemys[j][i] = new Enemy(new Vector3f(-14.5f + (i * 2), 5.0f + 2 * j, -1.0f), new Vector2f(0.73f, 0.5f),
						name);

				meshes[meshCounter] = enemys[j][i].getRect();
				meshCounter++;
				meshes[meshCounter] = enemys[j][i].shots[0];
				meshCounter++;
				meshes[meshCounter] = enemys[j][i].shots[1];
				meshCounter++;
				meshes[meshCounter] = enemys[j][i].shots[2];
				meshCounter++;
			}
		}
	}
}

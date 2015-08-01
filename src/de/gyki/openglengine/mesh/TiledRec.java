package de.gyki.openglengine.mesh;

import de.gyki.openglengine.graphics.Mesh;
import de.gyki.openglengine.graphics.Shader;
import de.gyki.openglengine.graphics.Texture;
import de.gyki.openglengine.graphics.VertexArray;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.math.Vector4f;
import de.gyki.openglengine.util.OpenGLErrorCatcher;

/**
 * This class represents a simple quad for use in games.
 * 
 * @author Florian Albrecht
 * @date 26.07.2015
 */

public class TiledRec extends Mesh {

	public TiledRec(Vector3f position, Vector2f scale, String textureName, int wRes, int hRes) {
		super.position = new Vector4f(position, 1.0f);
		// because the space is by default from 0.0 to -2.0
		super.position.set(Vector.Z_POS, super.position.get(Vector.Z_POS) * 0.1f);
		super.position.set(Vector.Z_POS, super.position.get(Vector.Z_POS) - 1.0f);
		// the space is converted to 1 the nearest to -1 the farest
		super.scale = scale;
		super.rotationX = 0.0f;
		super.rotationY = 0.0f;
		super.rotationZ = 0.0f;
		super.textures = new Texture[1];
		super.textures[0] = new Texture(textureName, 0);
		super.activeTexture = 0;
		super.shader = new Shader("std");

		super.vertices = new Vector4f[] { new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f), new Vector4f(1.0f, -1.0f, 0.0f, 1.0f),
				new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), new Vector4f(-1.0f, 1.0f, 0.0f, 1.0f) };

		super.colors = new Vector4f[] { new Vector4f(0.9f, 0.9f, 0.9f, 1.0f), new Vector4f(0.9f, 0.9f, 0.9f, 1.0f),
				new Vector4f(0.9f, 0.9f, 0.9f, 1.0f), new Vector4f(0.9f, 0.9f, 0.9f, 1.0f) };

		super.texCoords = new Vector2f[] { new Vector2f(0.0f, hRes), new Vector2f(wRes, hRes), new Vector2f(wRes, 0.0f),
				new Vector2f(0.0f, 0.0f) };

		super.indices = new byte[] { 0, 1, 2, 0, 2, 3 };

		super.width = scale.get(Vector.X_POS);
		super.height = scale.get(Vector.Y_POS);

		super.vertexData = new VertexArray(this);

		super.shader.setUniformMat4("pr_Mat", PROJECTION_MAT);
		super.shader.setUniform4f("cameraVector", super.CameraPosition);
		super.shader.setUniform4f(names[3], super.position);
		super.shader.setUniform2f(names[4], scale);
		super.shader.setUniform1f(names[5], rotationX);
		super.shader.setUniform1f(names[6], rotationY);
		super.shader.setUniform1f(names[7], rotationZ);

		super.shader.setUniform1i("uftex0", 0);

		OpenGLErrorCatcher.check("Quad creation '" + this.toString() + "'!!!");

	}
}

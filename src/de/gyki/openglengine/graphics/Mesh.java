package de.gyki.openglengine.graphics;

import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.core.MeshHandler;
import de.gyki.openglengine.math.Matrix4f;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.math.Vector4f;
import static org.lwjgl.opengl.GL11.*;

/**
 * This class defines all the components a mesh must have. It also contains some
 * predefined fields for meshes.
 * 
 * @version 1.0
 * @author Flo
 *
 */
public abstract class Mesh {

	/**
	 * The default projection matrix adapted to the size of the current window.
	 * <p>
	 * The resolution is defined in {@link engine2D.Engine_a1_1}.
	 */
	public static final Matrix4f PROJECTION_MAT = Matrix4f.createOrtographicMatrix(-Engine_a2.WRES, Engine_a2.WRES,
			-Engine_a2.HRES, Engine_a2.HRES, 1.0f, -1.0f);

	/**
	 * The camera vector representing the position. This vector has to be used
	 * in all Meshes that should be affected by it.
	 */
	public static Vector4f CameraPosition = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

	/**
	 * Represents the integer the Engine uses for mesh direction X. (1)
	 */
	public final static int X_DIR = 1;

	/**
	 * Represents the integer the Engine uses for mesh direction Y. (2)
	 */
	public final static int Y_DIR = 2;

	/**
	 * Represents the integer the Engine uses for mesh direction Z. (3)
	 */
	public final static int Z_DIR = 3;

	/**
	 * The VertexArray of the mesh;
	 */
	protected VertexArray vertexData;

	/**
	 * The position vector of the mesh
	 */
	protected Vector4f position;

	/**
	 * The scale vector of the mesh.
	 */
	protected Vector2f scale;

	/**
	 * The rotation around the x-axis.
	 */
	protected float rotationX;

	/**
	 * The rotation around the y-axis.
	 */
	protected float rotationY;

	/**
	 * The rotation around the z-axis.
	 */
	protected float rotationZ;

	/**
	 * The array of textures on the mesh.
	 */
	protected Texture[] textures;

	/**
	 * The texture index which is drawn to the mesh.
	 */
	protected int activeTexture = 0;

	/**
	 * The shader the mesh uses.
	 */
	protected Shader shader;

	/**
	 * The vertices the mesh has. {@code Must be filled!!!}
	 */
	protected Vector4f[] vertices;

	/**
	 * The colors to the vertices. {@code Has not to be filled.}
	 */
	protected Vector4f[] colors;

	/**
	 * The texturecoordinates to the vertices.
	 * {@code Must be filled of a texture is applied!!!}
	 */
	protected Vector2f[] texCoords;

	/**
	 * The indices for glDrawElements.
	 */
	protected byte[] indices;

	/**
	 * The attribute names for the graphics card.
	 */
	protected final String[] names = new String[] { "ivPosition", "ivColor", "ivTexCoord", "uvTranslation", "uvScale",
			"uvRotationX", "uvRotationY", "uvRotationZ" };

	/**
	 * The width of the mesh. (for the active {@code PhysicEngine})
	 */
	protected float width;

	/**
	 * The heigt of the mesh. (for the active {@code PhysicEngine})
	 */
	protected float height;

	/**
	 * The normal vector of the mesh, by default set to (0,0).
	 */
	protected Vector2f normal = new Vector2f(0.0f, 0.0f);

	/**
	 * Renders the mesh to the current context with the active texture and in
	 * Arraymode or Elementmode.
	 */
	public void draw() {
		if (activeTexture != -1)
			textures[activeTexture].bind(activeTexture);
		shader.bind();
		vertexData.bind();
		if (indices == null)
			glDrawArrays(GL_TRIANGLES, 0, vertices.length);
		else
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);

		vertexData.unbind();
		shader.unbind();
		if (activeTexture != -1)
			textures[activeTexture].unbind(activeTexture);
	}

	/**
	 * Deletes all components.
	 */
	public void destroy() {
		vertexData.destroy();
		shader.destroy();
		if (activeTexture != -1)
			for (Texture t : textures)
				t.destroy();
	}

	/**
	 * Is used to set the rotation around the x-axis
	 * 
	 * @param rotation
	 */
	public void setRotationX(double rotation) {
		this.rotationX = (float) rotation;
		shader.setUniform1f(names[5], this.rotationX);
	}

	/**
	 * Is used to set the rotation around the y-axis
	 * 
	 * @param rotation
	 */
	public void setRotationY(double rotation) {
		this.rotationY = (float) rotation;
		shader.setUniform1f(names[6], this.rotationY);
	}

	/**
	 * Is used to set the rotation around the z-axis
	 * 
	 * @param rotation
	 */
	public void setRotationZ(double rotation) {
		this.rotationZ = (float) rotation;
		shader.setUniform1f(names[7], this.rotationZ);
	}

	/**
	 * Is used to change the position of a the mesh.
	 */
	public void setPosition(Vector3f position) {
		this.position = new Vector4f(position, 1.0f);
		shader.setUniform4f(names[3], this.position);
	}

	/**
	 * Moves the mesh by the input Vector.
	 * 
	 * @param vec
	 *            the vector the mesh should be moved about
	 */
	public void move(Vector3f vec) {

		position.add(Vector.X_POS, vec.get(Vector.X_POS));
		position.add(Vector.Y_POS, vec.get(Vector.Y_POS));
		position.add(Vector.Z_POS, vec.get(Vector.Z_POS));

		shader.setUniform4f(names[3], position);
	}

	/**
	 * @return position
	 */
	public Vector4f getPosition() {
		return position;
	}

	/**
	 * @return scale
	 */
	public Vector2f getScale() {
		return scale;
	}

	/**
	 * @return width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return height
	 */

	public float getHeight() {
		return height;
	}

	/**
	 * @return the normal vector of the mesh
	 */
	public Vector2f getNormal() {
		return normal;
	}

	/**
	 * @return the rotation around the Y-axis
	 */
	public float getRotationY() {
		return rotationY;
	}

	/**
	 * @return the rotation around the Z-axis
	 */
	public float getRotationZ() {
		return rotationZ;
	}

	/**
	 * Is used to change the position of the mesh and its bounding box. WARNING:
	 * Only works if the mesh is registered at the active PhysicEngine. !!!
	 * 
	 * @param position
	 *            the position the mesh should be moved to
	 * @param pos
	 *            the position the mesh has inside the active PhysicEngine
	 */
	public void setPositionB(Vector3f position, int pos) {
		this.position = new Vector4f(position, 1.0f);
		shader.setUniform4f(names[3], this.position);
		MeshHandler.PhysicEngine.updatePositionB(pos, this.position);
	}

	/**
	 * Applies the camera transformation to the mesh
	 */
	public void applyCameraMouvement() {
		shader.setUniform4f("cameraVector", CameraPosition);
	}
}

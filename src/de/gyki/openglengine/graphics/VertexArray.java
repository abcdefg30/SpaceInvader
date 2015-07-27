package de.gyki.openglengine.graphics;

import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector4f;
import de.gyki.openglengine.util.BufferUtils;
import de.gyki.openglengine.util.OpenGLErrorCatcher;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexArray {

	/**
	 * The bytes a float variable has in memory.
	 */
	public final static int		BYTES_PER_FLOAT			= 4;

	/**
	 * The integer if the mesh has only vertices data.
	 */
	private final static int	ONLY_VERTICES			= 0;

	/**
	 * The integer if the mesh only has got color data apart form vertices.
	 */
	private final static int	ONLY_COLORS				= 1;

	/**
	 * The integer if the mesh only has got texture coordinate data apart form
	 * vertices.
	 */
	private final static int	ONLY_TEXCOORDS			= 2;

	/**
	 * The integer if the mesh only has got vertices data and is rendered with
	 * the use of indices.
	 */
	private final static int	ONLY_INDICES			= 3;

	/**
	 * The integer if the mesh has got color and texture coordinate data apart
	 * form vertices.
	 */
	private final static int	COLORS_AND_TEXCOORDS	= 4;

	/**
	 * The integer if the mesh only has got color data apart form vertices and
	 * is rendered with the use of indices.
	 */
	private final static int	COLORS_AND_INDICES		= 5;

	/**
	 * The integer if the mesh only has got texture coordinate data apart form
	 * vertices and is rendered with the use of indices.
	 */
	private final static int	INDICES_AND_TEXCOORDS	= 6;

	/**
	 * The integer if the mesh has got all possible data.
	 */
	private final static int	EVERYTHING				= 7;

	/**
	 * The data array containing all needed information for the graphics card.
	 */
	private float[]				data;

	/**
	 * The vertices of the mesh.
	 */
	private Vector4f[]			vertices;

	/**
	 * The colors of the mesh.
	 */
	private Vector4f[]			colors;

	/**
	 * The texture coordinates of the mesh.
	 */
	private Vector2f[]			texCoords;

	/**
	 * The indices of the mesh.
	 */
	private byte[]				indices;

	/**
	 * The attribute names of the mesh.
	 */
	private String[]			names;

	/**
	 * The integer referring to the VertexArray.
	 */
	private int					vao;

	/**
	 * The integer referring to the VertexBuffer.
	 */
	private int					vbo;

	/**
	 * The integer referring to the ElementBuffer.
	 */
	private int					ibo;

	/**
	 * The count of vertices.
	 */
	private int					countVertices;

	/**
	 * The count of data that refers to one vertex.
	 */
	private int					floatsPerVertex;

	/**
	 * The integer representing the mode of data.
	 */
	private int					mode;

	/**
	 * The constructor analyzes the data of the mesh and saves the results and
	 * data in local variables.
	 * 
	 * @param mesh
	 *            which uses the {@code VertexArray}.
	 */
	public VertexArray( Mesh mesh ) {
		this.vertices = mesh.vertices;
		countVertices = vertices.length;
		floatsPerVertex = 4;
		names = mesh.names;

		if ( mesh.texCoords == null && mesh.colors != null && mesh.indices == null ) {
			mode = ONLY_COLORS;
			this.colors = mesh.colors;
			floatsPerVertex = 8;
		}
		if ( mesh.texCoords != null && mesh.colors == null && mesh.indices == null ) {
			mode = ONLY_TEXCOORDS;
			this.texCoords = mesh.texCoords;
			floatsPerVertex = 6;
		}
		if ( mesh.texCoords == null && mesh.colors == null && mesh.indices != null ) {
			mode = ONLY_INDICES;
			this.indices = mesh.indices;
		}
		if ( mesh.texCoords == null && mesh.colors != null && mesh.indices != null ) {
			mode = COLORS_AND_INDICES;
			this.colors = mesh.colors;
			this.indices = mesh.indices;
			floatsPerVertex = 8;
		}
		if ( mesh.texCoords != null && mesh.colors != null && mesh.indices == null ) {
			mode = COLORS_AND_TEXCOORDS;
			this.colors = mesh.colors;
			this.texCoords = mesh.texCoords;
			floatsPerVertex = 8;
		}
		if ( mesh.texCoords != null && mesh.colors == null && mesh.indices != null ) {
			mode = INDICES_AND_TEXCOORDS;
			this.texCoords = mesh.texCoords;
			this.indices = mesh.indices;
			floatsPerVertex = 6;
		}
		if ( mesh.texCoords != null && mesh.colors != null && mesh.indices != null ) {
			mode = EVERYTHING;
			this.colors = mesh.colors;
			this.texCoords = mesh.texCoords;
			this.indices = mesh.indices;
			floatsPerVertex = 10;
		}

//		System.out.println("Floats per vertex = " + floatsPerVertex);
		initData();
		init(mesh.shader);
		OpenGLErrorCatcher.check("VertexArray creation VertexArray '" + mesh.toString() + "'!!!");
	}

	/**
	 * This method puts all the data together in the {@code data array} .
	 */
	private void initData() {
		data = new float[countVertices * floatsPerVertex];
		int tmp;
		for ( int c = 0; c < countVertices; c++ ) {
			tmp = 0;
			for ( int e = 0; e < 4; e++ ) {
				data[tmp + c * floatsPerVertex] = vertices[c].get(e + 1);
				tmp++;
			}

			if ( mode == EVERYTHING || mode == COLORS_AND_INDICES || mode == COLORS_AND_TEXCOORDS
					|| mode == ONLY_COLORS ) {
				for ( int e = 0; e < 4; e++ ) {
					data[tmp + c * floatsPerVertex] = colors[c].get(e + 1);
					tmp++;
				}
			}

			if ( mode == EVERYTHING || mode == INDICES_AND_TEXCOORDS || mode == COLORS_AND_TEXCOORDS
					|| mode == ONLY_TEXCOORDS ) {
				for ( int e = 0; e < 2; e++ ) {
					data[tmp + c * floatsPerVertex] = texCoords[c].get(e + 1);
					tmp++;
				}
			}

		}

		// TODO: DELETE
		// Printing out the content of data[] (for debugging)
		// int cur = 0;
		// String tmpS = "";
		// for ( int i = 0; i < countVertices; i++ ) {
		// tmpS = "";
		// for ( int e = 0; e < floatsPerVertex; e++ ) {
		// cur = e + i * floatsPerVertex;
		// tmpS += "data[" + cur + "] = " + data[cur] + ", ";
		// }
		// System.out.println(tmpS);
		// }
		// System.out.println("\n");
	}

	/**
	 * This method passes all the data to the graphics card
	 * 
	 * @param shader
	 *            of the mesh this {@code VertexArray} is used in.
	 */
	private void init( Shader shader ) {

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data), GL_STATIC_DRAW);
		shader.bindAttrib(names[0], 4, floatsPerVertex * BYTES_PER_FLOAT, 0);

		if ( mode == EVERYTHING || mode == COLORS_AND_INDICES || mode == COLORS_AND_TEXCOORDS
				|| mode == ONLY_COLORS ) {
			shader.bindAttrib(names[1], 4, floatsPerVertex * BYTES_PER_FLOAT, 4 * BYTES_PER_FLOAT);
//			System.out.println("Colors init");
		}

		if ( mode == EVERYTHING || mode == COLORS_AND_TEXCOORDS ) {
			shader.bindAttrib(names[2], 2, floatsPerVertex * BYTES_PER_FLOAT, 8 * BYTES_PER_FLOAT);
//			System.out.println("TexCoords init(1)");
		}

		if ( mode == INDICES_AND_TEXCOORDS || mode == ONLY_TEXCOORDS ) {
			shader.bindAttrib(names[2], 2, floatsPerVertex * BYTES_PER_FLOAT, 4 * BYTES_PER_FLOAT);
//			System.out.println("TexCoords init(2)");
		}

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);

		if ( mode == ONLY_INDICES || mode == COLORS_AND_INDICES || mode == INDICES_AND_TEXCOORDS || mode == EVERYTHING ) {
			ibo = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER,
					BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//			System.out.println("Indices init");
		}
	}

	/**
	 * This method binds the needed data for rendering.
	 */
	public void bind() {
		glBindVertexArray(vao);
		if ( mode == ONLY_INDICES || mode == COLORS_AND_INDICES || mode == INDICES_AND_TEXCOORDS || mode == EVERYTHING )
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

	}

	/**
	 * This method releases all the data binded for rendering.
	 */
	public void unbind() {
		glBindVertexArray(0);
		if ( mode == ONLY_INDICES || mode == COLORS_AND_INDICES || mode == INDICES_AND_TEXCOORDS || mode == EVERYTHING )
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * This method deletes all data the {@code VertexArray} has sended to the
	 * graphics card.
	 */
	public void destroy() {
		glDeleteBuffers(vbo);
		glDeleteVertexArrays(vao);
		if ( mode == ONLY_INDICES || mode == COLORS_AND_INDICES || mode == INDICES_AND_TEXCOORDS || mode == EVERYTHING )
			glDeleteBuffers(ibo);
	}

}

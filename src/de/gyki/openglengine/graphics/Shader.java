package de.gyki.openglengine.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import de.gyki.openglengine.Engine_a2;
import de.gyki.openglengine.math.Matrix4f;
import de.gyki.openglengine.math.Vector;
import de.gyki.openglengine.math.Vector2f;
import de.gyki.openglengine.math.Vector3f;
import de.gyki.openglengine.math.Vector4f;
import de.gyki.openglengine.util.OpenGLErrorCatcher;
import de.gyki.openglengine.util.Reader;

/**
 * This class is used to use GLSL shaders in a program.
 * 
 * @author Flo
 * @version 1.1
 */
public class Shader {

	/**
	 * The integer referring to the shader program on the graphics card.
	 */
	private int programID;

	/**
	 * Initializes all needed components and calls the {@code createShader}
	 * method.
	 * 
	 * @param name
	 *            of the shader in the location defined in
	 *            {@link engine2D.Engine_a1_1.java} (PATH)
	 */
	public Shader(String name) {
		programID = -1;
		createShader(Engine_a2.PATH + "shader/" + name);

		OpenGLErrorCatcher.check("Shader creation Shader '" + name + "'!!!");
	}

	/**
	 * Creates the {@code Shader} from the .vert_glsl and .frag_glsl files.
	 * These were defined in the constructor.
	 * 
	 * @param path
	 *            the complete path of the files
	 * @return
	 */
	private int createShader(String path) {
		String vert = Reader.read(path + ".vert_glsl");
		String frag = Reader.read(path + ".frag_glsl");

		programID = glCreateProgram();
		if (programID < 0) {
			System.err.println("Failed to create Shaderprogram " + path + " !!!");
			Engine_a2.QUIT = true;
			return -1;
		}

		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);

		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to Compile Vertexshader " + path + " !!!");
			System.err.println(glGetShaderInfoLog(vertID));
			Engine_a2.QUIT = true;
			return -1;
		}

		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to Compile Fragmentshader " + path + " !!!");
			System.err.println(glGetShaderInfoLog(fragID));
			Engine_a2.QUIT = true;
			return -1;
		}

		glAttachShader(programID, vertID);
		glAttachShader(programID, fragID);
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Failed to link shaderprogram " + path + " !!!");
			System.err.println(glGetProgramInfoLog(programID));
			Engine_a2.QUIT = true;
			return -1;
		}

		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Failed to validate shaderprogram " + path + " !!!");
			System.err.println(glGetProgramInfoLog(programID));
			Engine_a2.QUIT = true;
			return -1;
		}

		glDeleteShader(vertID);
		glDeleteShader(fragID);

		return 0;

	}

	/**
	 * 
	 * @param name
	 *            of the attribute in the shader
	 * @param size
	 *            the size of the data
	 * @param stride
	 *            the amount of bytes that should be skipped. (Must include the
	 *            own size)
	 * @param offset
	 *            the offset after which the data is inserted
	 */
	public void bindAttrib(String name, int size, int stride, int offset) {
		bind();
		int loc = glGetAttribLocation(programID, name);
		if (loc < 0) {
			System.err.println("Unable to find Attrib '" + name + "' in shader " + programID);
			return;
		}
		glEnableVertexAttribArray(loc);
		glVertexAttribPointer(loc, size, GL_FLOAT, false, stride, offset);
		unbind();
	}

	/**
	 * This method gets the ID of an uniform attribute in the shader.
	 * 
	 * @param name
	 *            the name which should be searched in the shader
	 * @return the ID of the uniform attribute with the {@code name}
	 */
	public int getUniform(String name) {
		int result = glGetUniformLocation(programID, name);
		if (result == -1) {
			System.err.println("Could not find Uniform " + name + " !!!");
			return -1;
		} else {
			return result;
		}
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code int value}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniform1i(String name, int value) {
		bind();
		glUniform1i(getUniform(name), value);
		unbind();
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code float value}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniform1f(String name, float value) {
		bind();
		glUniform1f(getUniform(name), value);
		unbind();
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code Vector2f vec}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniform2f(String name, Vector2f vec) {
		bind();
		glUniform2f(getUniform(name), vec.get(Vector.X_POS), vec.get(Vector.Y_POS));
		unbind();
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code Vector3f vec}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniform3f(String name, Vector3f vec) {
		bind();
		glUniform3f(getUniform(name), vec.get(Vector.X_POS), vec.get(Vector.Y_POS), vec.get(Vector.Z_POS));
		unbind();
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code Vector3f vec}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniform4f(String name, Vector4f vec) {
		bind();
		glUniform4f(getUniform(name), vec.get(Vector.X_POS), vec.get(Vector.Y_POS), vec.get(Vector.Z_POS),
				vec.get(Vector.W_POS));
		unbind();
	}

	/**
	 * This method sets a uniform({@code String name}) in the shader to a value(
	 * {@code Matrix4f mat}).
	 * 
	 * @param name
	 *            the name of the uniform the value should be set to
	 * @param value
	 *            the value that should be set to the uniform
	 */
	public void setUniformMat4(String name, Matrix4f mat) {
		bind();
		glUniformMatrix4fv(getUniform(name), false, mat.toFloatBuffer());
		unbind();
	}

	/**
	 * Binds the shader for rendering.
	 */
	public void bind() {
		if (programID != -1)
			glUseProgram(programID);
	}

	/**
	 * Realeses the current shader.
	 */
	public void unbind() {
		glUseProgram(0);
	}

	/**
	 * Deletes the shader program.
	 */
	public void destroy() {
		glDeleteShader(programID);
	}

}

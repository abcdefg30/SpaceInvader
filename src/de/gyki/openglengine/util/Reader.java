package de.gyki.openglengine.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is used to read a file in asci code.
 * 
 * @author Flo
 * @version 1.2
 */
public class Reader {

	/**
	 * Lets no one instantiate this class.
	 */
	private Reader() {
	}

	/**
	 * Reads the data from a file to a {@code String}.
	 * 
	 * @param path
	 *            the path of the file
	 * @return the content of the file as {@code Sting}
	 */
	public static String read(String path) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader input = new BufferedReader(new FileReader(path));
			String line;

			while ((line = input.readLine()) != null) {
				result.append(line + "\n");
			}

			input.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}

package de.gyki.game;

public abstract class Game {

	/**
	 * This method has to initialize every component of the game apart from the
	 * engine except the {@code PhysicEngine} which has to be initialized here.
	 */
	public abstract void init();

	/**
	 * This method should be used to draw all components of the game.
	 */
	public abstract void render();

	/**
	 * 
	 * @param float[][][] collisions
	 */
	public abstract void update( float[][][] collisions );

	public abstract void updateKeyPress( int key );

	public abstract void updateKeyReleas( int key );

	/**
	 * 
	 */
	public abstract void destroy();
}

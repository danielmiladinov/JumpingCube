/**
 * JumpingCubePlayer.java
 * @author Daniel Miladinov
 * @version 1-beta
 */

import java.awt.Color;

/**
 * The JumpingCubePlayer class represents the idea
 * of a player in a JumpingCube game.  As of version
 * 1-beta of this release, a JumpingCubePlayer has
 * a name and a color.  Their name is displayed in
 * the game's status window when it's their turn to
 * go, and the buttons they own take on their color.
 */
public class JumpingCubePlayer extends Object
{
	/** The JumpingCubePlayer's color; Buttons they own become this color.*/
	private Color color;

	/** The JumpingCubePlayer's name; Their name is displayed in the game's status window when it's their turn to go.*/
	private String name;

	/**
	 * The JumpingCubePlayer constructor.
	 * @param playerName The string that will beomce the name of the JumpingCubePlayer.
	 * @param playerColor Buttons that the JumpingCubePlayer owns will be this color.
	 */
	public JumpingCubePlayer(String playerName, Color playerColor)
	{
		this.name = playerName;
	 	this.color = playerColor;
	}

	/**
	 * Returns the JumpingCubePlayer's color.
	 * @return A Color object representing the JumpingCubePlayer's color.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Returns the JumpingCubePlayer's name.
	 * @return A String object representing the JumpingCubePlayer's name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the JumpingCubePlayer's color.
	 * @param newColor A color object which will become the JumpingCubePlayer's new color.
	 */
	public void setColor(Color newColor)
	{
		color = newColor;
	}

	/**
	 * Sets the JumpingCubePlayer's name.
	 * @param newName A string object which will become the JumpingCubePlayer's new name.
	 */
	public void setName(String newName)
	{
		name = newName;
	}

	/**
	 * Returns whether another JumpingCubePlayer object is equal to this one.
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(JumpingCubePlayer p)
	{
		return (p.getColor() == this.getColor());
	}
}

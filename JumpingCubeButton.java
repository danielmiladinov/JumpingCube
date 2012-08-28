/**
 * JumpingCubeButton.java
 * @author Daniel Miladinov
 * @version 1-beta
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The JumpingCubeButton represents an individual box on a
 * JumpingCubeBoard.  It acts like a regular JButton, but also
 * has references to the JumpingCubePlayer that may own it, the 
 * Color that the pertains to that JumpingCubePlayer, as well as
 * the JumpingCubeBoard that contains it.
 */
public class JumpingCubeButton extends JButton
{
	// JumpingCubeButton constants
	/** The default color of an unowned button.*/
	public static final Color UNOWNED_COLOR = Color.lightGray;
	/** The String constant denoting one point.*/
	public static final String ONE_POINT 	= "     .     ";
	/** The String constant denoting two points.*/
	public static final String TWO_POINTS	= "    . .    ";
	/** The String constant denoting three points.*/
	public static final String THREE_POINTS	= "   . . .   ";
	/** The String constant denoting four points.*/
	public static final String FOUR_POINTS	= "  . . . .  ";
	/** The String constant denoting five points.*/
	public static final String FIVE_POINTS	= " . . . . . ";

	private JumpingCubeBoard jcb;

	private JumpingCubePlayer owner;
	private Color btnColor;
	private int points;
	private int myRow;
	private int myCol;

	private JumpingCubeButton northBtn;
	private JumpingCubeButton southBtn;
	private JumpingCubeButton eastBtn;
	private JumpingCubeButton westBtn;

	/**
	 * The JumpingCubeButton constructor.
	 * @param row The row of the JumpingCubeBoard that contains this button.
	 * @param col The column of the JumpingCubeBoard that contains this button.
	 * @param board The JumpingCubeBoard that contains this button.
	 */
	public JumpingCubeButton(int row, int col, JumpingCubeBoard board)
	{
		super("");
		myRow = row;
		myCol = col;
		points = 1;
		owner = null;
		jcb = board;
		redisplay();
	}

	/**
	 * Returns whether or not this button is owned.
	 * @return True if this button contains a non-null JumpingCubePlayer reference, false otherwise.
	 */
	public boolean isOwned()
	{
		return (this.owner != null);
	}

	/**
	 * Returns the owner of this button.
	 * @return The JumpingCubePlayer reference to the owner of this button (can be null).
	 */
	public JumpingCubePlayer getOwner()
	{
		return owner;
	}

	/**
	 * Sets the owner of the button to the given JumpingCubePlayer reference, independent of gameplay considerations.
	 * @param p The JumpingCubePlayer reference that will become the new owner of this button.
	 */
	public void changeOwner(JumpingCubePlayer p)
	{
		owner = p;
	}

	/**
	 * Sets the owner of the button to the given JumpingCubePlayer reference and updates the JumpingCubeBoard's count of owned and unowned butons.
	 * @param p The JumpingCubePlayer reference that will become the new owner of this button.
	 */
	public void setOwner(JumpingCubePlayer p)
	{
		if(p != null)
		{
			if(owner == null)
			{
				jcb.decrementNumBoxesUnowned();
				if(jcb.getPlayer1().equals(p))
				{
					jcb.incrementNumBoxesPlayer1();
				}
				else if(jcb.getPlayer2().equals(p))
				{
					jcb.incrementNumBoxesPlayer2();
				}
			}

			else if(owner != null)
			{
				if(!(owner.equals(p)))
				{
					if(jcb.getPlayer1().equals(p))
					{
						jcb.decrementNumBoxesPlayer2();
						jcb.incrementNumBoxesPlayer1();
					}
					else if(jcb.getPlayer2().equals(p))
					{
						jcb.decrementNumBoxesPlayer1();
						jcb.incrementNumBoxesPlayer2();
					}
				}
			}
		}
		owner = p;
	}

	/**
	 * Updates the background and text properties of the JumpingCubeButton
	 * based on its point value and its owner, if any.
	 */
	public void redisplay()
	{
		if(this.isOwned())
		{
			setBackground(owner.getColor());
		}
		else
		{
			setBackground(UNOWNED_COLOR);
		}
		switch(points)
		{
			case 1:	setText(ONE_POINT);
				break;
			case 2:	setText(TWO_POINTS);
				break;
			case 3:	setText(THREE_POINTS);
				break;
			case 4:	setText(FOUR_POINTS);
				break;
			case 5:	setText(FIVE_POINTS);
				break;
		}
	}

	/**
	 * Increments the JumpingCubeButton's point value and, if necessary,
	 * queues its neighbors' point values to be incremented as well.
	 */
	public void addPoint()
	{
		points++;
		redisplay();

		if(points > sumNeighbors())
		{
			points = 1;

			if(getNorth() != null)
			{
				jcb.queueEvent(getNorth(), owner);
			}

			if(getSouth() != null)
			{
				jcb.queueEvent(getSouth(), owner);
			}

			if(getEast() != null)
			{
				jcb.queueEvent(getEast(), owner);
			}

			if(getWest() != null)
			{
				jcb.queueEvent(getWest(), owner);
			}
		}
		redisplay();
	}

	/**
	 * Gets the number of points that this JumpingCubeButton owns.
	 * @return The number of points that the JumpingCubeButton owns.
	 */
	public int getPoints()
	{
		return points;
	}

	/**
	 * Sets the number of points that this JumpingCubeButton will own.
	 * @param newPoints Should be an integer between 1 and 5.
	 */
	public void setPoints(int newPoints)
	{
		points = newPoints;
	}

	/**
	 * Gets the JumpingCubeButton's color.
	 * @return A color object representing the JumpingCubeButton's color.
	 */
	public Color getColor()
	{
		return btnColor;
	}

	/**
	 * Sets the JumpingCubeButton's color to the color supplied.
	 * @param c The JumpingCubeButton's new color.
	 */
	public void changeColor(Color c)
	{
		btnColor = c;
		setBackground(btnColor);
	}
	
	/**
	 * Sets the JumpingCubeButton's north neighbor.
	 * @param jcbN The JumpingCubeButton neighbor to the north.
	 */
	public void setNorth(JumpingCubeButton jcbN)
	{
		northBtn = jcbN;
	}
	
	/**
	 * Sets the JumpingCubeButton's south neighbor.
	 * @param jcbS The JumpingCubeButton neighbor to the south.
	 */
	public void setSouth(JumpingCubeButton jcbS)
	{
		southBtn = jcbS;
	}

	/**
	 * Sets the JumpingCubeButton's east neighbor.
	 * @param jcbE The JumpingCubeButton neighbor to the east.
	 */
	public void setEast(JumpingCubeButton jcbE)
	{
		eastBtn = jcbE;
	}

	/**
	 * Sets the JumpingCubeButton's west neighbor.
	 * @param jcbW The JumpingCubeButton neighbor to the west.
	 */
	public void setWest(JumpingCubeButton jcbW)
	{
		westBtn = jcbW;
	}
	
	/**
	 * Gets the JumpingCubeButton's north neighbor.
	 * @return The JumpingCubeButton's neighbor to the north.
	 */
	public JumpingCubeButton getNorth()
	{
		return northBtn;
	}
	
	/**
	 * Gets the JumpingCubeButton's south neighbor.
	 * @return The JumpingCubeButton's neighbor to the south.
	 */
	public JumpingCubeButton getSouth()
	{
		return southBtn;
	}
	
	/**
	 * Gets the JumpingCubeButton's east neighbor.
	 * @return The JumpingCubeButton's neighbor to the east.
	 */
	public JumpingCubeButton getEast()
	{
		return eastBtn;
	}
	
	/**
	 * Gets the JumpingCubeButton's west neighbor.
	 * @return The JumpingCubeButton's neighbor to the west.
	 */
	public JumpingCubeButton getWest()
	{
		return westBtn;
	}
	
	private int sumNeighbors()
	{
		int sum = 0;

		if(getNorth() != null) sum++;
		if(getSouth() != null) sum++;
		if(getEast() != null) sum++;
		if(getWest() != null) sum++;

		return sum;
	}
}

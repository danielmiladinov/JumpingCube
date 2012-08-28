/**
 * JumpingCubeBoard.java
 * @author Daniel Miladinov
 * @version 1-beta
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * The JumpingCubeBoard class acts as the container for the JumpingCubeButtons,
 * and manages their behavior.  It uses an inner class to propagate calls to
 * a button's neighbors instead of recursive calls, which got so numerous they
 * blew the call stack during early development tests.  Instead, objects which
 * implement similar functionality are created off the heap at an acceptable
 * higher overhead cost.
 */
public class JumpingCubeBoard extends JPanel implements ActionListener
{
	/** The default number of rows in a JumpingCubeBoard.*/
	public static final int DEFAULT_ROWS = 7;
	/** The default number of columns in a JumpingCubeBoard.*/
	public static final int DEFAULT_COLS = 7;
	/** The maximum number of rows in a JumpingCubeBoard.*/
	public static final int MAX_ROWS = 10;
	/** The maximum number of columns in a JumpingCubeBoard.*/
	public static final int MAX_COLS = 10;
	/** The minumum number of rows in a JumpingCubeBoard.*/
	public static final int MIN_ROWS = 5;
	/** The minumum number of columns in a JumpingCubeBoard.*/
	public static final int MIN_COLS = 5;

	private int rows;
	private int cols;

	private int numBoxes;
	private int numBoxesPlayer1;
	private int numBoxesPlayer2;
	private int numBoxesUnowned;

	private JumpingCubePlayer player1;
	private JumpingCubePlayer player2;
	private JumpingCubePlayer activePlayer;

	private JumpingCube parentWindow;
	private Vector eventVector;
	private JumpingCubeButton[][] btnGrid;

	/**
	 * An inner class which represents a button overflowing
	 * points to its neighbors.  The JumpingCubeBoard only
	 * needs to know which button to increment and upon
	 * which player to bestow its ownership.
	 */
	class ButtonEvent
	{
		JumpingCubeButton button;
		JumpingCubePlayer player;

		public ButtonEvent(JumpingCubeButton b, JumpingCubePlayer p)
		{
			button = b;
			player = p;
		}
	}

	/**
	 * The JumpingCubeBoard constructor.  Creates a new JumpingCubeBoard
	 * belonging to the specified JumpingCube window containing the specified
	 * rows and columns.
	 * @param window The JumpingCube window which owns this JumpingCubeBoard.
	 * @param rows The number of rows in this JumpingCubeBoard.
	 * @param cols The number of cols in this JumpingCubeBoard.
	 */
	public JumpingCubeBoard(JumpingCube window, int rows, int cols)
	{
		parentWindow = window;
		eventVector = new Vector();
		if(rows > MAX_ROWS)
		{
			this.rows = MAX_ROWS;
		}
		else if(rows < MIN_ROWS)
		{
			this.rows = MIN_ROWS;
		}
		else
		{
			this.rows = rows;
		}
		if(cols > MAX_COLS)
		{
			this.cols = MAX_COLS;
		}
		else if(cols < MIN_COLS)
		{
			this.cols = MIN_COLS;
		}
		else
		{
			this.cols = cols;
		}
		this.setLayout(new GridLayout(this.rows, this.cols));
		setBoard(this.rows, this.cols);
	}

	/**
	 * Gets the number of columns the JumpingCubeBoard contains.
	 * @return The number of columns this JumpingCubeBoard contains.
	 */
	public int getCols()
	{
		return cols;
	}

	/**
	 * Gets the number of rows the JumpingCubeBoard contains.
	 * @return The number of rows this JumpingCubeBoard contains.
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * Gets the number of unowned boxes on the JumpingCubeBoard.
	 * @return The number of boxes (buttons) that neither player has clicked on yet.
	 */
	public int getNumBoxesUnowned()
	{
		return numBoxesUnowned;
	}

	/**
	 * Gets the number of boxes on the JumpingCubeBoard.
	 * @return The number of boxes the JumpingCubeBoard contains (rows * cols).
	 */
	public int getNumBoxes()
	{
		return numBoxes;
	}

	/**
	 * Gets the number of boxes that Player 1 owns.
	 * @return The number of boxes that Player 1 owns.
	 */
	public int getNumBoxesPlayer1()
	{
		return numBoxesPlayer1;
	}

	/**
	 * Gets the number of boxes that Player 2 owns.
	 * @return The number of boxes that Player 2 owns.
	 */
	public int getNumBoxesPlayer2()
	{
		return numBoxesPlayer2;
	}

	/**
	 * Returns a reference to the first JumpingCubePlayer object.
	 * @return The JumpingCubePlayer object representing Player 1.
	 */
	public JumpingCubePlayer getPlayer1()
	{
		return player1;
	}

	/**
	 * Returns a reference to the second JumpingCubePlayer object.
	 * @return The JumpingCubePlayer object representing Player 2.
	 */
	public JumpingCubePlayer getPlayer2()
	{
		return player2;
	}

	/**
	 * Returns a reference to the array of JumpingCubeButtons.
	 * @return Returns a two-dimensional array of JumpingCubeButtons, indexed by [rows][cols].
	 */
	public JumpingCubeButton[][] getBoard()
	{
		return btnGrid;
	}

	/**
	 * Sets the number of columns in the JumpingCubeBoard.
	 * @param newCols The new number of columns in the JumpingCubeBoard.
	 */
	public void setCols(int newCols)
	{
		cols = newCols;
	}

	/**
	 * Sets the number of rows in the JumpingCubeBoard.
	 * @param newRows The new number of rows in the JumpingCubeBoard.
	 */
	public void setRows(int newRows)
	{
		rows = newRows;
	}

	/**
	 * Assigns two JumpingCubePlayer objects to the JumpingCubeBoard.
	 * @param p1 A reference to Player 1.
	 * @param p2 A reference to Player 2.
	 */
	public void setPlayers(JumpingCubePlayer p1, JumpingCubePlayer p2)
	{
		this.player1 = p1;
		this.player2 = p2;
		activePlayer = p1;
	}

	/**
	 * Sets the reference to Player 1.
	 * @param p1 A JumpingCubePlayer which will become Player 1.
	 */
	public void setPlayer1(JumpingCubePlayer p1)
	{
		player1 = p1;
	}

	/**
	 * Sets the reference to Player 2.
	 * @param p2 A JumpingCubePlayer which will become Player 2.
	 */
	public void setPlayer2(JumpingCubePlayer p2)
	{
		player2 = p2;
	}

	/**
	 * Decrements the number of unowned boxes.
	 */
	public void decrementNumBoxesUnowned()
	{
		numBoxesUnowned--;
	}

	/**
	 * Increments the number of boxes owned by Player 1.
	 */
	public void incrementNumBoxesPlayer1()
	{
		numBoxesPlayer1++;
	}

	/**
	 * Increments the number of boxes owned by Player 2.
	 */
	public void incrementNumBoxesPlayer2()
	{
		numBoxesPlayer2++;
	}

	/**
	 * Decrements the number of boxes owned by Player 1.
	 */
	public void decrementNumBoxesPlayer1()
	{
		numBoxesPlayer1--;
	}

	/**
	 * Decrements the number of boxes owned by Player 2.
	 */
	public void decrementNumBoxesPlayer2()
	{
		numBoxesPlayer2--;
	}

	/**
	 * As a fulfillment of its ActionListener interface contract,
	 * the JumpingCubeBoard responds to ActionEvents generated by
	 * its buttons with this method.
	 * @param e An ActionEvent generated by the system when a button is clicked.
	 */
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if(o instanceof JumpingCubeButton && (getCursor().getType() == Cursor.DEFAULT_CURSOR))
		{
			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			JumpingCubeButton b = ((JumpingCubeButton)o);

			if((b.getOwner() == null) || (b.getOwner().equals(activePlayer)))
			{
				parentWindow.updateStatus("Doing Move. . .");
				b.setOwner(activePlayer);
				b.addPoint();

				while(eventVector.size() > 0)
				{
					ButtonEvent be = (ButtonEvent)eventVector.remove(0);

					be.button.setOwner(be.player);
					be.button.addPoint();
					if(player1Won())
					{
						winSequence(player1);
						eventVector = new Vector();
						return;
					}
					if(player2Won())
					{
						winSequence(player2);
						eventVector = new Vector();
						return;
					}
				}
				switchActivePlayer();
				parentWindow.updateStatus("On Turn: " + activePlayer.getName());
			}
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return;
		}
	}

	/**
	 * When a JumpingCubeButton has more points than it has neighbors,
	 * it informs the JumpingCubeBoard to add points to them with this
	 * method.
	 * @param b The JumpingCubeButton queued to receive a point from its neighbor.
	 * @param p The JumpingCubePlayer that owns the the calling JumpingCubeButton.
	 */
	public void queueEvent(JumpingCubeButton b, JumpingCubePlayer p)
	{
		eventVector.add(new ButtonEvent(b,p));
	}

	/**
	 * When a game player running Player 1 wishes to change its name or
	 * its color, the board cycles through its buttons and updates all
	 * of them that are owned by Player1.
	 * @param newPlayer1 The JumpingCubePlayer reference containing Player 1's new name and/or color.
	 */
	public void updatePlayer1(JumpingCubePlayer newPlayer1)
	{
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				if(btnGrid[r][c].getOwner() != null)
				{
					if(btnGrid[r][c].getOwner().equals(player1))
					{
						btnGrid[r][c].changeColor(player1.getColor());
						btnGrid[r][c].changeOwner(player1);
					}
				}
			}
		}
		setPlayer1(newPlayer1);
	}

	/**
	 * When a game player running Player 2 wishes to change its name or
	 * its color, the board cycles through its buttons and updates all
	 * of them that are owned by Player2.
	 * @param newPlayer2 The JumpingCubePlayer reference containing Player 2's new name and/or color.
	 */
	public void updatePlayer2(JumpingCubePlayer newPlayer2)
	{
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				if(btnGrid[r][c].getOwner() != null)
				{
					if(btnGrid[r][c].getOwner().equals(player2))
					{
						btnGrid[r][c].changeColor(player2.getColor());
						btnGrid[r][c].changeOwner(player2);
					}
				}
			}
		}
		setPlayer2(newPlayer2);
	}

	private void setBoard(int rows, int cols)
	{
		setRows(rows);
		setCols(cols);
		btnGrid = null;

		btnGrid = new JumpingCubeButton[rows][cols];
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				btnGrid[r][c] = new JumpingCubeButton(r, c, this);
				btnGrid[r][c].addActionListener(this);
				this.add(btnGrid[r][c]);
			}
		}

		numBoxes = rows * cols;
		numBoxesUnowned = numBoxes;
		numBoxesPlayer1 = 0;
		numBoxesPlayer2 = 0;

		registerNeighbors();
	}

	private void registerNeighbors()
	{
		int minRow = 0;
		int maxRow = (rows - 1);
		int minCol = 0;
		int maxCol = (cols - 1);

		for(int r = 0; r < rows; r++)
		{

			for(int c = 0; c < cols; c++)
			{
				if(r > minRow)
				{
					btnGrid[r][c].setNorth(btnGrid[r-1][c]);
				}
				if(r < maxRow)
				{
					btnGrid[r][c].setSouth(btnGrid[r+1][c]);
				}
				if(c > minCol)
				{
					btnGrid[r][c].setWest(btnGrid[r][c-1]);
				}
				if(c < maxCol)
				{
					btnGrid[r][c].setEast(btnGrid[r][c+1]);
				}
			}

		}
	}

	private void switchActivePlayer()
	{
		if(activePlayer.equals(player1))
		{
			activePlayer = player2;
		}
		else
		{
			activePlayer = player1;
		}
	}

	private void winSequence(JumpingCubePlayer p)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		parentWindow.setWinner(p);
	}

	private boolean player1Won()
	{
		return ((numBoxesPlayer1 == numBoxes) && (numBoxesPlayer2 == 0));
	}

	private boolean player2Won()
	{
		return ((numBoxesPlayer2 == numBoxes) && (numBoxesPlayer1 == 0));
	}

}

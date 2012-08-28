/**
 * JumpingCube.java
 * @author Daniel Miladinov
 * @version 1-beta
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * The JumpingCube class is the top-level container for the game.
 * It contains the game menu, the game board, and a status bar.
 */
public class JumpingCube extends JFrame
{
	private UIManager.LookAndFeelInfo looks[];
	private JLabel gameStatus;

	private ButtonGroup playfieldGroup;
	private JRadioButtonMenuItem playfieldItems[];

	private JumpingCubeBoard board;

	private JumpingCubePlayer player1;
	private JumpingCubePlayer player2;

	private int rows;
	private int cols;

	private int myWidth;
	private int myHeight;
	
	/** The starting width of the JumpingCube game window.*/
	public static final int START_WIDTH = 600;
	/** The starting height of the JumpingCube game window.*/
	public static final int START_HEIGHT = 400;

	/**
	 * The default constructor. 
	 * Creates a new game window with the defaults:
	 * 7x7 JumpingCubeBoard
	 * JumpingCubePlayer player1 with name "Player 1", and color Color.red
	 * JumpingCubePlayer player2 with name "Player 2", and color Color.blue
	 */
	public JumpingCube()
	{
		super("JumpingCube");
		this.rows = JumpingCubeBoard.DEFAULT_ROWS;
		this.cols = JumpingCubeBoard.DEFAULT_COLS;
		
		addComponentListener(
				new ComponentAdapter()
				{
					public void componentResized(ComponentEvent ce)
					{
						myWidth = getWidth();
						myHeight = getHeight();
					}
				}
				);

		
		setUpMenu();
		
		player1 = new JumpingCubePlayer("Player 1", Color.red);
		player2 = new JumpingCubePlayer("Player 2", Color.blue);

		board = new JumpingCubeBoard(this, rows, cols);
		board.setPlayers(player1, player2);
		
		gameStatus = new JLabel("On Turn: " + player1.getName());

		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(gameStatus, BorderLayout.SOUTH);

		myWidth = START_WIDTH;
		myHeight = START_HEIGHT;

		setSize(myWidth, myHeight);

		looks = UIManager.getInstalledLookAndFeels();

		try
		{
			UIManager.setLookAndFeel(looks[2].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		show();
	}

	/**
	 * This method is called by the JumpingCubeBoard to notify the
	 * game that there is a winner.
	 * @param p The reference to the winning JumpingCubePlayer
	 */
	public void setWinner(JumpingCubePlayer p)
	{
		JOptionPane.showMessageDialog(this, 
				"Winner is " + 
				p.getName() + 
				"!", 
				"Winner", 
				JOptionPane.PLAIN_MESSAGE);
		clearBoard();
	}

	/**
	 * Clears the JumpingCubeBoard, resetting all the
	 * colors and point values for a new game.
	 */
	public void clearBoard()
	{
		getContentPane().remove(board);

		board = new JumpingCubeBoard(this, rows, cols);
		board.setPlayers(player1, player2);
		getContentPane().add(board, BorderLayout.CENTER);
		updateStatus("On Turn: " + player1.getName());
		setSize(myWidth, myHeight);
		repaint();
		show();
	}

	/**
	 * Instantiates and initializes a new JumpingCube game,
	 * ready for play.
	 */
	public static void main(String args[])
	{
		JumpingCube app = new JumpingCube();

		app.addWindowListener(
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						System.exit(0);
					}
				}
				);
	}

	private void setUpMenu()
	{
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
	
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		
		JMenuItem newItem = new JMenuItem("New");
		newItem.setMnemonic('N');
		newItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						clearBoard();
					}
				}
				);

		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('Q');
		quitItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					}
				}
				);
		
		gameMenu.add(newItem);
		gameMenu.addSeparator();
		gameMenu.add(quitItem);
		
		JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic('S');

		String grids[] = {"5x5", "6x6", "7x7", "8x8", "9x9", "10x10"};

		JMenu playfieldMenu = new JMenu("Playfield");
		playfieldMenu.setMnemonic('P');
		playfieldItems = new JRadioButtonMenuItem[grids.length];
		playfieldGroup = new ButtonGroup();
		for(int i = 0; i < grids.length; i++)
		{
			playfieldItems[i] = new JRadioButtonMenuItem(grids[i]);
			playfieldMenu.add(playfieldItems[i]);
			playfieldGroup.add(playfieldItems[i]);
			playfieldItems[i].addActionListener(new PlayfieldHandler());
		}
		
		playfieldItems[2].setSelected(true);
		
		JMenuItem player1MenuItem = new JMenuItem("Player 1");
		player1MenuItem.setMnemonic('1');
		player1MenuItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						new JumpingCubePlayerDialog(player1, JumpingCube.this, 1);
						board.updatePlayer1(player1);
					}
				}
				);

		JMenuItem player2MenuItem = new JMenuItem("Player 2");
		player2MenuItem.setMnemonic('2');
		player2MenuItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						new JumpingCubePlayerDialog(player2, JumpingCube.this, 2);
						board.updatePlayer2(player2);
					}
				}
				);

		settingsMenu.add(playfieldMenu);
		settingsMenu.addSeparator();
		settingsMenu.add(player1MenuItem);
		settingsMenu.add(player2MenuItem);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		JMenuItem aboutItem = new JMenuItem("About...");
		aboutItem.setMnemonic('A');
		aboutItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JOptionPane.showMessageDialog(JumpingCube.this, "JumpingCube, a two-player strategy game\nwritten in Java by Daniel Miladinov", "About JumpingCube", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				);

		helpMenu.add(aboutItem);

		bar.add(gameMenu);
		bar.add(settingsMenu);
		bar.add(helpMenu);

	}

	/**
	 * Sends messages to the game window's status bar.
	 * @param status The new String object containing the status update.
	 */
	public void updateStatus(String status)
	{
		gameStatus.setText(status);
	}

	/**
	 * A helping inner class that handles playfield menu selection events.
	 */
	class PlayfieldHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(playfieldItems[0].isSelected())
			{
				rows = 5;
				cols = 5;
				myWidth = 430;
				myHeight = 290;
			}
			if(playfieldItems[1].isSelected())
			{
				rows = 6;
				cols = 6;
				myWidth = 515;
				myHeight = 345;
			}
			if(playfieldItems[2].isSelected())
			{
				rows = 7;
				cols = 7;
				myWidth = 600;
				myHeight = 400;
			}
			if(playfieldItems[3].isSelected())
			{
				rows = 8;
				cols = 8;
				myWidth = 685;
				myHeight = 457;
			}
			if(playfieldItems[4].isSelected())
			{
				rows = 9;
				cols = 9;
				myWidth = 770;
				myHeight = 514;
			}
			if(playfieldItems[5].isSelected())
			{
				rows = 10;
				cols = 10;
				myWidth = 790;
				myHeight = 560;
			}
			clearBoard();
			show();
			setSize(myWidth, myHeight);
		}
	}
}




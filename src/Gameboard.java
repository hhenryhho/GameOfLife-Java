/**
 * Game board creates the game board and handles game rules.
 * 
 * @author Henry Ho
 * @version 1.0
 * @since 2019-09-17
 */

import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gameboard implements ActionListener {
	
	// Instance Variables
	private JFrame frame;
	private JPanel container, grid, menu;
	private JButton start, end, pause, clear;
	private Timer timer;
	private Cell[][] gameboard;
	
	// Constants
	private final int WIDTH = 20;
	private final int HEIGHT = 20;
	private final int DELAY = 250;
	private static final int[][] NEIGHBOURS = { { -1, -1 }, { -1, 0 }, { -1, +1 }, 
												{ 0, -1 }, 			   { 0, +1 }, 
												{ +1, -1 }, { +1, 0 }, { +1, +1 } };

	/**
	 * Constructs a Gameboard object
	 */
	public Gameboard() {
		// The JFrame
		frame = new JFrame("Game Of Life");
		
		// The JPanels
		container = new JPanel();
		grid = new JPanel();
		menu = new JPanel();
		
		// The JButtons
		start = new JButton("Start");
		end = new JButton("End");
		pause = new JButton("Pause");
		clear = new JButton("Clear");
		
		// The multidimensional array that holds the Cells
		gameboard = new Cell[WIDTH][HEIGHT];
	}

	/**
	 * Sets up the GUI representation of this Gameboard
	 */
	public void setUpBoard() {
		// Set up the frame
		frame.setSize(1000, 1000);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set up the layout of the container: Gameboard on top, then menu on the bottom
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		// Set up the layout of the Gameboard as a grid
		grid.setLayout(new GridLayout(WIDTH, HEIGHT));

		// Create the grid of Cells
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				gameboard[i][j] = new Cell();
				// Add each Cell to the grid
				grid.add(gameboard[i][j]);
			}
		}

		// Create action listeners and add them to each menu button
		start.addActionListener(this);
		end.addActionListener(this);
		pause.addActionListener(this);
		clear.addActionListener(this);

		// Ensure that all buttons are properly disabled until game starts
		pause.setEnabled(false);
		clear.setEnabled(false);

		// Add each button to the menu panel
		menu.add(start);
		menu.add(end);
		menu.add(pause);
		menu.add(clear);

		// Sets the menu panel to be restricted to the size of the JButton
		menu.setMaximumSize(new Dimension(400, 0));

		// Add the grid and menu to the container, then add the container to the frame
		container.add(grid);
		container.add(menu);
		frame.add(container);
		frame.setVisible(true);
	}

	/**
	 * Action listener for the menu buttons
	 * 
	 * @param evt the event of the button being pressed
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JButton) {
			if (evt.getActionCommand().equals("Start")) {
				// If a timer was cancelled, create a new one
				timer = new Timer();
				// Ensure all buttons make sense
				start.setEnabled(false);
				end.setEnabled(true);
				pause.setEnabled(true);
				clear.setEnabled(true);
				// Begin the game loop immediately and repeat every 0.25 seconds
				timer.schedule(new TimerTask() {
					public void run() {
						iterateNextGeneration();
					}
				}, 0, DELAY);
			} else if (evt.getActionCommand().equals("End")) {
				// Exit the game
				endGame();
			} else if (evt.getActionCommand().equals("Pause")) {
				// Ensure all buttons make sense
				start.setEnabled(true);
				end.setEnabled(true);
				pause.setEnabled(false);
				clear.setEnabled(true);
				// Terminate the timer and discard all scheduled tasks
				timer.cancel();
			} else if (evt.getActionCommand().equals("Clear")) {
				// Ensure all buttons make sense
				start.setEnabled(true);
				end.setEnabled(true);
				pause.setEnabled(false);
				clear.setEnabled(false);
				clearGame();
				// Terminate the timer and discard all scheduled tasks
				timer.cancel();
			}
		}
	}

	/**
	 * Calculates the number of live neighbors for each Cell, then assigns each Cell its new state
	 */
	public void getLiveNeighbors() {
		int numberOfLiveNeighbors = 0;
		
		// Iterate through each Cell
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				// Calculate the number of live neighbors the current Cell has
				for (int[] offset : NEIGHBOURS) {
					// The Cells outside the grid are ignored
					try {
						if (gameboard[i + offset[1]][j + offset[0]].getStatus()) {
							numberOfLiveNeighbors++;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				// Any live cell with two or three live neighbors lives on to the next
				// generation.
				if ((numberOfLiveNeighbors == 2 || numberOfLiveNeighbors == 3) && gameboard[i][j].getStatus()) {
					gameboard[i][j].setNextStatus(true);
					// Any dead cell with exactly three live neighbors becomes a live cell, as if by
					// reproduction.
				} else if (numberOfLiveNeighbors == 3 && !gameboard[i][j].getStatus()) {
					gameboard[i][j].setNextStatus(true);
					// Any live cell with fewer than two live neighbors dies, as if by
					// underpopulation.
				} else if (numberOfLiveNeighbors < 2) {
					gameboard[i][j].setNextStatus(false);
					// Any live cell with more than three live neighbors dies, as if by
					// overpopulation.
				} else if (numberOfLiveNeighbors > 3) {
					gameboard[i][j].setNextStatus(false);
				}
				// Resets
				numberOfLiveNeighbors = 0;
			}
		}
	}

	/**
	 * Set each Cell its assigned next generation
	 */
	public void iterateNextGeneration() {
		getLiveNeighbors();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				gameboard[i][j].setStatus(gameboard[i][j].getNextStatus());
			}
		}
	}

	/**
	 * Ends the game and exits the frame
	 */
	public void endGame() {
		System.exit(0);
	}

	/**
	 * Iterates through each Cell and sets it to its default settings
	 */
	public void clearGame() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				gameboard[i][j].setStatus(false);
			}
		}
	}
}

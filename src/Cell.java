/**
 * Cell object is a JButton that knows its status and next status.
 * 
 * @author Henry Ho
 * @version 1.0
 * @since 2019-09-17
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Cell extends JButton implements ActionListener {

	// Instance variables
	boolean status;
	boolean nextStatus;

	/**
	 * Constructs a Cell object
	 */
	public Cell() {
		status = false;
		nextStatus = false;
		addActionListener(this);
	}

	/**
	 * Action listener: Whenever a Cell is pressed, change its status to its
	 * opposite
	 */
	public void actionPerformed(ActionEvent e) {
		changeStatus();
	}

	/**
	 * Returns the status of this Cell
	 * 
	 * @return the status of this Cell
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Helper method to the actionPerformed method
	 */
	public void changeStatus() {
		if (this.getStatus() == false) {
			this.status = true;
			this.setBackground(Color.RED);
		} else {
			this.status = false;
			this.setBackground(null);
		}
	}

	/**
	 * Sets the status of this Cell to the given status
	 * 
	 * @param status the new status of the Cell
	 */
	public void setStatus(boolean status) {
		this.status = status;
		if (status) {
			this.setBackground(Color.RED);
		} else {
			this.setBackground(null);
		}
	}

	/**
	 * Sets the next status the given status.
	 * 
	 * @param nextStatus
	 */
	public void setNextStatus(boolean nextStatus) {
		this.nextStatus = nextStatus;
	}

	/**
	 * Return the next status of this Cell
	 * 
	 * @return the next status of this Cell
	 */
	public boolean getNextStatus() {
		return nextStatus;
	}

}

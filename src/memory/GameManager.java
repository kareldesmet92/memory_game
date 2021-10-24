package memory;

import java.awt.BorderLayout;
//import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

 abstract public class GameManager {

	protected JFrame mainFrame;		// Frame for the actual game
	protected GameMode gamemode;	// gamemode contains all the game components like cards, players, difficulty, ...
		
	public GameManager() {
		this.setMainFrame();
	}
	
	abstract public JPanel generateSidePanel();		// must be implemented by subclasses
	abstract public JPanel generateField();			// must be implemented by subclasses
	abstract public void addToCardBuffer(NormalCard c);	//the cardbuffer serves as buffer to store temporarily the cards that are clicked and turned. 
	abstract public boolean checkGameEnd();
	abstract public void stop();
	
	public JFrame getMainFrame() {
		return mainFrame;
	}

	// Create the frame for the actual game.
	public void setMainFrame() {
		this.mainFrame = new JFrame("Memory Game");
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.mainFrame.getContentPane().setLayout(new BorderLayout());
		//this.mainFrame.getContentPane().setPreferredSize(new Dimension(1400, 750));
		//this.mainFrame.getContentPane().setMaximumSize(this.mainFrame.getContentPane().getPreferredSize());
	}	
}

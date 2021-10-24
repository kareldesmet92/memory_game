package memory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManagerHuman extends GameManager implements ActionListener {
	
	private final GameManagerHuman self = this;
	private ArrayList<NormalCard> cardBuffer;		// buffer to store cards there were just clicked and turned by the player
	private ArrayList<PenaltyCard> penaltyBuffer;	// buffer to store penaltycards that were turned
    private Timer cardTimer;						// timer for turning cards back on their back when there were not forming a pair. 
    private Timer penaltyTimer;						// timer for turning back a penaltycard
    
    private int pairsFound;
    private int pairsToFind;
    private boolean bonusCardTurned;
    
    private int playerTurn;							// Variable to remember which player has to choose cards.
    JLabel player1Name;
    JLabel player1Score;
	JLabel player2Name;
    JLabel player2Score;
    
    // Constructor: 
	public GameManagerHuman(GameMode gamemode) {
		super();
		this.setGamemode(gamemode);
		this.mainFrame.getContentPane().add(this.generateField(), BorderLayout.CENTER);
		this.pairsFound = 0;
		this.pairsToFind = this.gamemode.getCarddeck().getPairs();
		this.playerTurn = 1;
		this.bonusCardTurned = false;
		this.gamemode.randomPlayerOrder();
		this.player1Name = new  JLabel("Player 1: "+this.gamemode.getPlayer1().getName());
		this.player1Score = new JLabel("Score: "+this.gamemode.getPlayer1().getScore());
		this.player2Name= new  JLabel("Player 2: "+this.gamemode.getPlayer2().getName());
		this.player2Score = new JLabel("Score: "+this.gamemode.getPlayer2().getScore());
		this.mainFrame.getContentPane().add(this.generateSidePanel(), BorderLayout.EAST);
		this.mainFrame.pack();
		this.mainFrame.setVisible(true);
		this.cardBuffer = new ArrayList<NormalCard>(2);
		this.penaltyBuffer = new ArrayList<PenaltyCard>();
		this.cardTimer = new Timer(500, this);
		this.cardTimer.setRepeats(false);
		this.cardTimer.setActionCommand("cardtimer");
		this.penaltyTimer = new Timer(500, this);
		this.penaltyTimer.setRepeats(false);
		this.penaltyTimer.setActionCommand("penaltytimer");
	}
	
	public JPanel generateSidePanel() {
		JPanel side = new JPanel();
		side.setLayout(new BorderLayout());
		
		JLabel player = new  JLabel("Players: ");
		
		JPanel up = new JPanel();
		up.setLayout(new BoxLayout(up, BoxLayout.PAGE_AXIS));
		up.add(Box.createRigidArea(new Dimension(5,30)));
		up.add(player);
		up.add(Box.createRigidArea(new Dimension(5,30)));
		up.add(this.player1Name);
		up.add(Box.createRigidArea(new Dimension(5,10)));
		up.add(this.player1Score);
		up.add(Box.createRigidArea(new Dimension(5,40)));
		up.add(this.player2Name);
		up.add(Box.createRigidArea(new Dimension(5,10)));
		up.add(this.player2Score);
		player1Name.setForeground(Color.GREEN);
		player1Score.setForeground(Color.GREEN);
		
		//up.setMinimumSize(new Dimension(150, 100));
		//up.setMaximumSize(new Dimension(150,1500));
		
		JPanel middle = new JPanel();
		middle.setLayout(new BoxLayout(middle, BoxLayout.PAGE_AXIS));
		middle.add(Box.createRigidArea(new Dimension(5,30)));
		JLabel time = new JLabel("Time left: ");
		middle.add(time);
		//middle.add(timeLeft);
		//middle.setMinimumSize(new Dimension(150, 100));
		//middle.setMaximumSize(new Dimension(150,1500));
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		JButton newgame = new JButton("New Game");
		JButton exit = new JButton("Exit");
		JButton highScores = new JButton("High Scores");
		newgame.setActionCommand("newgame");
		exit.setActionCommand("exit");
		highScores.setActionCommand("highscores");
		newgame.addActionListener(this);
		exit.addActionListener(this);
		highScores.addActionListener(this);
		buttons.add(newgame);
		buttons.add(exit);
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		bottom.add(buttons);
		bottom.add(Box.createRigidArea(new Dimension(5,20)));
		bottom.add(highScores, JPanel.LEFT_ALIGNMENT);
		bottom.add(Box.createRigidArea(new Dimension(5,5)));
		
		side.add(up, BorderLayout.NORTH);
		//side.add(timeLeft, BorderLayout.CENTER);
		side.add(bottom, BorderLayout.SOUTH);
		return side;
	}
	
	// This method generates the grid with different tiles and adds actionlistener to each Card.
	public JPanel generateField() {
		int rows = this.gamemode.getRows();
		int columns = this.gamemode.getColumns();
		JPanel field = new JPanel(new GridLayout(rows, columns));
		for (int i=0; i<(rows*columns); i++) {
			Card card = this.gamemode.getCarddeck().getCardList().get(i);
			field.add(card);
			card.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				
					if (card instanceof PenaltyCard) {
						// handling penalty card actions
						if (card.isTurned() == false) {
							card.setIcon(card.getImg());
							card.setTurned(true);
							self.penaltyTurnedUp((PenaltyCard)card);
						}						
					}
					
					else if (card instanceof BonusCard) {
						// handling bonus card actions
						card.setIcon(card.getImg());
						card.setTurned(true);
						self.bonusCardTurned = true;
					}
					
					else {
						// handling normal card actions
						if (card.isTurned() == false) {
							card.setIcon(card.getImg());
							card.setTurned(true);
							System.out.println("card "+card.getId()+" is turned");
							self.turnedUp((NormalCard)card);
						}
						else ;
					}
				}});
		}
		return field;	
	}

	// Actions to be performed when cards or buttons are clicked: 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cardtimer")) {
			for(int i = 0; i<this.cardBuffer.size(); i++) {
				NormalCard card = this.cardBuffer.get(i);
				card.setTurned(false);
				card.setMatched(false);
				card.setIcon(card.getImgback());
			}
			this.cardBuffer.clear();
			this.switchTurn();
		}
		else if (e.getActionCommand().equals("penaltytimer")) {
			PenaltyCard card = this.penaltyBuffer.get(this.penaltyBuffer.size()-1);
			card.setTurned(false);
			card.setIcon(card.getImgback());
		}
		else if (e.getActionCommand().equals("newgame")) {
			String theme = this.gamemode.getTheme();
			Player p1 = this.gamemode.getPlayer1();
			p1.setScore(0);
			Player p2 = this.gamemode.getPlayer2();
			p2.setScore(0);
			int difficulty = this.gamemode.difficulty;
			GameMode gamemode = new GameMode(theme, p1, p2, difficulty);
			this.getMainFrame().dispose();
			new GameManagerHuman(gamemode);
		}
		else if (e.getActionCommand().equals("exit")) {
			this.getMainFrame().dispose();
		}	
		else if (e.getActionCommand().equals("highscores")) {
			new HighScores();
		}	
	}
	
	public void turnedUp(NormalCard c) {
		if(this.cardBuffer.size() < 2) {
			this.addToCardBuffer(c);
		} 
	}
	
	// This methods stores temporarily the cards that are clicked and checks if they for a pair. If not, cardTimer is called to turn the cards back. 
	public void addToCardBuffer(NormalCard c) {
		this.cardBuffer.add(c);
		if(this.cardBuffer.size() == 2) {
			NormalCard other = this.cardBuffer.get(0);
			if(other.getPairId() == c.getId()) {
				other.setMatched(true);
				c.setMatched(true);
				this.cardBuffer.clear();
				this.pairsFound++;
				if (this.pairsFound != 1) {
					if (playerTurn == 1) {
						this.gamemode.player1.setScore(this.gamemode.player1.getScore()+10);
						this.updateScoreLabel(this.player1Score, this.gamemode.player1.getScore());
					}
					else {
						this.gamemode.player2.setScore(this.gamemode.player2.getScore()+10);
						this.updateScoreLabel(this.player2Score, this.gamemode.player2.getScore());
					}
					this.checkGameEnd();
				}
				else {
					if (playerTurn == 1) {
						this.gamemode.player1.setScore(this.gamemode.player1.getScore()+5);
						this.updateScoreLabel(this.player1Score, this.gamemode.player1.getScore());
					}
					else {
						this.gamemode.player2.setScore(this.gamemode.player2.getScore()+5);
						this.updateScoreLabel(this.player2Score, this.gamemode.player2.getScore());
					}
				}
			}
			else {
				this.cardTimer.start();
			}
		}		
	}
	
	// checks if the game must be ended by checking if the pairs are all found. 
	public boolean checkGameEnd() {
		if (this.pairsToFind == this.pairsFound) {
			this.stop();
			if (this.gamemode.getPlayer1().getScore() > this.gamemode.getPlayer2().getScore()) {
				JOptionPane.showMessageDialog(this.mainFrame, this.gamemode.getPlayer1().getName()+" wins! Score: "+this.gamemode.getPlayer1().getScore(),"Game ended", JOptionPane.PLAIN_MESSAGE);
			}
			else if (this.gamemode.getPlayer1().getScore() < this.gamemode.getPlayer2().getScore()) {
				JOptionPane.showMessageDialog(this.mainFrame, this.gamemode.getPlayer2().getName()+" wins! Score: "+this.gamemode.getPlayer2().getScore(),"Game ended", JOptionPane.PLAIN_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(this.mainFrame, " No winner!" ,"Game ended", JOptionPane.PLAIN_MESSAGE);
			return true;
		}
		else return false;
	}
	
	// When turn has to be switched, this method takes care of necessary adaptations in GUI and variable playerTurn:
	public void switchTurn() {
		if (this.bonusCardTurned == false) {
			if (this.playerTurn == 1) {
				this.player1Name.setForeground(Color.BLACK);
				this.player1Score.setForeground(Color.BLACK);
				this.player2Name.setForeground(Color.GREEN);
				this.player2Score.setForeground(Color.GREEN);
				this.playerTurn = 2;
			}
			else {
				this.player1Name.setForeground(Color.GREEN);
				this.player1Score.setForeground(Color.GREEN);
				this.player2Name.setForeground(Color.BLACK);
				this.player2Score.setForeground(Color.BLACK);
				this.playerTurn = 1;
			}
		}
		else this.bonusCardTurned = false;
	}
	
	// Method to remember which penaltycards are already turned once and which not. The first time a penaltycard is turned, there will be no 'penalty'
	public void penaltyTurnedUp(PenaltyCard c) {
		this.bonusCardTurned = false;
		if (playerTurn == 1) {
			this.gamemode.player1.setScore(this.gamemode.player1.getScore()-8);
			this.updateScoreLabel(this.player1Score, this.gamemode.player1.getScore());
		}
		else {
			this.gamemode.player2.setScore(this.gamemode.player2.getScore()-8);
			this.updateScoreLabel(this.player2Score, this.gamemode.player2.getScore());
		}
		if (this.penaltyBuffer.contains(c)) {
			// set c at the end of the list:
			this.penaltyBuffer.remove(c);
			this.penaltyBuffer.add(c);
			this.penaltyTimer.start();
		}
		else {
			this.penaltyBuffer.add(c);
			this.penaltyTimer.start();
		}
		this.cardTimer.start();
	}
	
	// Removes all actionlisteners from the Cards so that no action can be performed when the game has ended. 
	public void stop() {
		for (int i = 0; i<this.gamemode.getCarddeck().getCardList().size(); i++) {
    		//if (this.gamemode.getCarddeck().getCardList().get(i).isTurned() == false) {
    			this.gamemode.getCarddeck().getCardList().get(i).setIcon(this.gamemode.getCarddeck().getCardList().get(i).getImg());
    			this.gamemode.getCarddeck().getCardList().get(i).removeActionListener(this.gamemode.getCarddeck().getCardList().get(i).getActionListeners()[0]);
    		//}
    	}
    }
    
	// Update score on the GUI when a player has found a pair
    public void updateScoreLabel(JLabel label, int score) {
    	label.setText("Score: "+score);
    }
	
	public void setGamemode(GameMode gamemode) {
		super.gamemode = gamemode;
	}
}

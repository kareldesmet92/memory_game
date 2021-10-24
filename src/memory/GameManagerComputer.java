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

public class GameManagerComputer extends GameManager implements ActionListener {

	private final GameManagerComputer self = this;
	private ArrayList<NormalCard> cardBuffer;			// buffer to store cards there were just clicked and turned by the player
	private ArrayList<NormalCard> computerBuffer;		// buffer for the computer, to remember which card from the memory has to be turned
	private ArrayList<PenaltyCard> penaltyBuffer;		// buffer to store penaltycards that were turned
    private Timer cardTimer;							// timer for turning cards back on their back when there were not forming a pair. 
    private Timer penaltyTimer;							// timer for turning back a penaltycard
    private Timer humanTimer;							// timer for time how long the human play has to turn his/her cards
    private Timer computerTimer;						// timer for the computer to turn a random card
    private Timer computerTimer2;						// second timer for the computer when he has to turn a second card from the memory
    
    private int pairsFound;
    private int pairsToFind;
    private boolean bonusCardTurned;
    
    private int playerTurn;
    JLabel player1Name;
    JLabel player1Score;
	JLabel player2Name;
    JLabel player2Score;
    
    private int computerMemoryMaxSize;					// memory size of the computer
    private ArrayList<NormalCard> computerMemory;		// memory of the computer
    private ArrayList<NormalCard> turnedCardsList;		// list of cards that are already turned
    private ArrayList<NormalCard> notTurnedCardList;	// list of cards that are not turned
    private int computerTurn;
    
	public GameManagerComputer(GameMode gamemode) {
		super();
		this.setGamemode(gamemode);
		this.notTurnedCardList = gamemode.getCarddeck().getCardPairList(); 
		this.turnedCardsList = new ArrayList<NormalCard>();
		this.computerTurn = 0;
		this.mainFrame.getContentPane().add(this.generateField(), BorderLayout.CENTER);
		this.pairsFound = 0;
		this.pairsToFind = this.gamemode.getCarddeck().getPairs();
		this.playerTurn = 1;
		this.bonusCardTurned = false;
		this.setComputerSettings(gamemode.getDifficulty());
		this.player1Name = new  JLabel("Player 1: "+this.gamemode.getPlayer1().getName());
		this.player1Score = new JLabel("Score: "+this.gamemode.getPlayer1().getScore());
		this.player2Name= new  JLabel("Player 2: "+this.gamemode.getPlayer2().getName());
		this.player2Score = new JLabel("Score: "+this.gamemode.getPlayer2().getScore());
		this.mainFrame.getContentPane().add(this.generateSidePanel(), BorderLayout.EAST);
		this.mainFrame.pack();
		this.mainFrame.setVisible(true);
		this.cardBuffer = new ArrayList<NormalCard>(2);
		this.computerBuffer = new ArrayList<NormalCard>(2);

		this.penaltyBuffer = new ArrayList<PenaltyCard>();
		this.cardTimer = new Timer(500, this);
		this.cardTimer.setRepeats(false);
		this.cardTimer.setActionCommand("cardtimer");
		this.penaltyTimer = new Timer(500, this);
		this.penaltyTimer.setRepeats(false);
		this.penaltyTimer.setActionCommand("penaltytimer");
		this.humanTimer = new Timer(10000, this);
		this.humanTimer.setRepeats(false);
		this.humanTimer.setActionCommand("humantimer");
		this.computerTimer = new Timer(1000, this);
		this.computerTimer.setRepeats(false);
		this.computerTimer.setActionCommand("computertimer");
		this.computerTimer2 = new Timer(1000, this);
		this.computerTimer2.setRepeats(false);
		this.computerTimer2.setActionCommand("computertimer2");
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
			card.addActionListener(this); 
			card.setActionCommand("card");
		}
		return field;	
	}

	// to switch turn from player to computer or the other way around:
	public void switchTurn() {
		if (this.bonusCardTurned == false) {
			if (this.playerTurn == 1) {
				this.humanTimer.stop();
				this.player1Name.setForeground(Color.BLACK);
				this.player1Score.setForeground(Color.BLACK);
				this.player2Name.setForeground(Color.GREEN);
				this.player2Score.setForeground(Color.GREEN);
				this.playerTurn = 2;
				for (int i=0; i<this.gamemode.getCarddeck().getCardList().size(); i++) {
					this.gamemode.getCarddeck().getCardList().get(i).removeActionListener(this);
				}
				this.computerTimer.start();
			}
			else {
				this.player1Name.setForeground(Color.GREEN);
				this.player1Score.setForeground(Color.GREEN);
				this.player2Name.setForeground(Color.BLACK);
				this.player2Score.setForeground(Color.BLACK);
				this.playerTurn = 1;
				for (int i=0; i<this.gamemode.getCarddeck().getCardList().size(); i++) {
					this.gamemode.getCarddeck().getCardList().get(i).addActionListener(this);
					this.gamemode.getCarddeck().getCardList().get(i).setActionCommand("card");
				}
				this.humanTimer.start();
			}
		}
		else this.bonusCardTurned = false;
	}
	
	// Actions to be performed for different timers	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("computertimer")) {
			// First checks if there are pairs in the memory, otherwise the computer takes a random card
			NormalCard card = this.checkPairsInMemory();
			if((this.computerTurn == 0) && (card == null)){
				card = (NormalCard) this.notTurnedCardList.get((int)Math.floor(Math.random()*this.notTurnedCardList.size()));		// Random card is chosen by computer
			}
			else if(this.computerTurn == 1) {
				card = (NormalCard) this.notTurnedCardList.get((int)Math.floor(Math.random()*this.notTurnedCardList.size()));		// Random card is chosen by computer
			}
			card.setIcon(card.getImg());
			card.setTurned(true);
			System.out.println("card "+card.getId()+" is turned");
			this.notTurnedCardList.remove(card);
			this.turnedCardsList.add(card);
			this.addToComputerMemory(card);
			this.addToCardBuffer(card); // misschien andere buffer gebruiken
			this.computerTurn++;
			if (this.computerTurn == 1) {
				this.checkInMemory();
			}
			else this.computerTurn = 0;
		}
		else if (e.getActionCommand().equals("computertimer2")) {
			// If a matching card was found in the memory, the computer takes this card and turns it
			NormalCard card = this.computerBuffer.get(this.computerBuffer.size()-1);
			card.setIcon(card.getImg());
			card.setTurned(true);
			this.notTurnedCardList.remove(card);
			this.turnedCardsList.add(card);
			System.out.println("card "+card.getId()+" is turned");
			this.computerTurn = 0;
			this.addToCardBuffer(card);
			this.computerBuffer.clear();
			
		}
		else if (e.getActionCommand().equals("humantimer")){
			this.cardTimer.start();
		}
		
		else if (e.getActionCommand().equals("cardtimer")) {
			// clears the cardbuffer and turns card back after 0.5s on the back and switch turn
			for(int i = 0; i<this.cardBuffer.size(); i++) {
				NormalCard card = this.cardBuffer.get(i);
				card.setTurned(false);
				this.notTurnedCardList.add(card);
				this.turnedCardsList.remove(card);
				card.setMatched(false);
				card.setIcon(card.getImgback());
			}
			this.cardBuffer.clear();
			this.switchTurn();
		}
		else if (e.getActionCommand().equals("card")) {
			Card card = (Card)e.getSource();
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
					this.turnedCardsList.add((NormalCard)card);
					this.notTurnedCardList.remove(card);
					this.addToComputerMemory((NormalCard)card);
					System.out.println("card "+card.getId()+" is turned");
					this.turnedUp((NormalCard)card);
				}
				else ;
			}
		}
		
		else if (e.getActionCommand().equals("penaltytimer")) {
			PenaltyCard card = this.penaltyBuffer.get(this.penaltyBuffer.size()-1);
			card.setTurned(false);
			card.setIcon(card.getImgback());
		}
		else if (e.getActionCommand().equals("newgame")) {
			String theme = this.gamemode.getTheme();
			this.checkName(this.gamemode.getPlayer1());
			Player p1 = this.gamemode.getPlayer1();
			p1.setScore(0);
			this.checkName(this.gamemode.getPlayer2());
			Player p2 = this.gamemode.getPlayer2();
			p2.setScore(0);
			int difficulty = this.gamemode.difficulty;
			GameMode gamemode = new GameMode(theme, p1, p2, difficulty);
			this.getMainFrame().dispose();
			new GameManagerComputer(gamemode);
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
	
	// Adds a card to cardbuffer and checks if they are equal. If so, scores are adapted. 
	public void addToCardBuffer(NormalCard c) {
		this.cardBuffer.add(c);
		if(this.cardBuffer.size() == 2) {
			NormalCard other = this.cardBuffer.get(0);
			if(other.getPairId() == c.getId()) {
				other.setMatched(true);
				c.setMatched(true);
				this.removeFromComputerMemory(this.cardBuffer.get(0));
				this.removeFromComputerMemory(this.cardBuffer.get(1));
				this.cardBuffer.clear();
				this.pairsFound++;
				if (this.pairsFound != 1) {
					if (playerTurn == 1) {
						this.gamemode.player1.setScore(this.gamemode.player1.getScore()+10);
						this.updateScoreLabel(this.player1Score, this.gamemode.player1.getScore());
						this.humanTimer.restart();
						this.checkGameEnd();
					}
					else {
						this.gamemode.player2.setScore(this.gamemode.player2.getScore()+10);
						this.updateScoreLabel(this.player2Score, this.gamemode.player2.getScore());
						boolean end = this.checkGameEnd();
						if (end == false) {
							this.computerTimer.start();
						}
						else return;
					}
				}
				// The first one who finds a pair just wins 5 points. 
				else {
					if (playerTurn == 1) {
						this.gamemode.player1.setScore(this.gamemode.player1.getScore()+5);
						this.updateScoreLabel(this.player1Score, this.gamemode.player1.getScore());
						this.humanTimer.restart();
					}
					else {
						this.gamemode.player2.setScore(this.gamemode.player2.getScore()+5);
						this.updateScoreLabel(this.player2Score, this.gamemode.player2.getScore());
						this.computerTimer.start();
					}
				}
			}
			else {
				this.cardTimer.start();
			}
		}		
	}
	
	// Adds a cards to computer memory:
	public void addToComputerMemory(NormalCard card) {
		if (this.computerMemory.contains(card) == false) {
			if (this.computerMemory.size() < this.computerMemoryMaxSize) {
				this.computerMemory.add(card);
			}
			else {
				this.computerMemory.remove(0);
				this.computerMemory.add(card);
			}
		}
		else return;
	}
	
	// checks if a matching card can be found in the memory
	public void checkInMemory() {
		if (this.cardBuffer.size() == 1) {
			boolean inbuffer = false;
			for (int i=0; i<this.computerMemory.size(); i++) {
				if (this.computerMemory.get(i).getPairId() == this.cardBuffer.get(0).getId()) {
					inbuffer = true;
					this.computerBuffer.add(this.computerMemory.get(i));
					this.computerTimer2.start();
					return;
				}
			}
			if (inbuffer == false) {
				this.computerTimer.start();
			}
		}
		else {
			this.computerTimer.start();
		}
	}
	
	// Checks for a complete pair in the memory
	public NormalCard checkPairsInMemory() {
		for (int i = 0; i < this.computerMemory.size(); i++) {
			NormalCard card1 = this.computerMemory.get(i);
			for (int j = i+1; j < this.computerMemory.size(); j++) {
				if (card1.getPairId() == this.computerMemory.get(j).getId()) {
					return card1;
				}
			}
		}
		return null;
	}
	
	// Removes card from memory
	public void removeFromComputerMemory(NormalCard card) {
		if(this.computerMemory.contains(card)) {
			this.computerMemory.remove(card);
		}
	}
	
	// Sets memory size:
	public void setComputerSettings(int difficulty) {
		this.computerMemoryMaxSize = difficulty;
		this.computerMemory = new ArrayList<NormalCard>(this.computerMemoryMaxSize);
	}
	
	// Checks if the game has ended
	public boolean checkGameEnd() {
		if (this.pairsToFind == this.pairsFound) {
			this.stop();
			if (this.gamemode.getPlayer1().getScore() > this.gamemode.getPlayer2().getScore()) {
				JOptionPane.showMessageDialog(this.mainFrame, this.gamemode.getPlayer1().getName()+" wins! Score: "+this.gamemode.getPlayer1().getScore(),"Game ended", JOptionPane.PLAIN_MESSAGE);
			}
			else if (this.gamemode.getPlayer1().getScore() < this.gamemode.getPlayer2().getScore()) {
				JOptionPane.showMessageDialog(this.mainFrame, this.gamemode.getPlayer2().getName()+" wins! Score: "+this.gamemode.getPlayer2().getScore(),"Game ended", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(this.mainFrame, " No winner!" ,"Game ended", JOptionPane.PLAIN_MESSAGE);
			}
			HighScores.writeComputerScore(this.gamemode.player1);
			return true;
		}
		else return false;
	}
	
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
	
	// stops the timers and removes action listeners from cards:
	public void stop() {
		this.humanTimer.stop();
		this.computerTimer.stop();
		this.computerTimer2.stop();
		this.cardTimer.stop();
		this.penaltyTimer.stop();
		for (int i = 0; i<this.gamemode.getCarddeck().getCardList().size(); i++) {
    			this.gamemode.getCarddeck().getCardList().get(i).setIcon(this.gamemode.getCarddeck().getCardList().get(i).getImg());
    			this.gamemode.getCarddeck().getCardList().get(i).removeActionListener(this);
    	}
    }
    
    public void updateScoreLabel(JLabel label, int score) {
    	label.setText("Score: "+score);
    }
	
	public void setGamemode(GameMode gamemode) {
		super.gamemode = gamemode;
	}
	
	public void checkName(Player player) {
		StringBuilder sb = new StringBuilder(player.getName());
		if (sb.charAt(0) == '(') {
			sb.deleteCharAt(sb.length()-1);
			sb.deleteCharAt(0);
		}
		player.setName(sb.toString());
	}
}

package memory;

import java.awt.BorderLayout;
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

public class GameManagerAlone extends GameManager implements ActionListener{

	private final GameManagerAlone self = this;		
	private ArrayList<NormalCard> cardBuffer;		// buffer to store cards there were just clicked and turned by the player
	private ArrayList<PenaltyCard> penaltyBuffer;	// buffer to store penaltycards that were turned
    private Timer cardTimer;						// timer for turning cards back on their back when there were not forming a pair. 
    private Timer penaltyTimer;						// timer for turning back a penaltycard
    private Timer timer;							// overall timer, stopwatch
    
    // timer variables:
    private int counter;
    private int initTime;
    private int bonusTime;
    private int penaltyTime;
    private boolean isTimerActive = false;
    private JLabel timeLeft;
    
    private int pairsFound;
    private int pairsToFind;
    private int endScore;
    

	public GameManagerAlone(GameMode gamemode) {
		super();
		this.setGamemode(gamemode);
		this.mainFrame.getContentPane().add(this.generateField(), BorderLayout.CENTER);
		this.pairsFound = 0;
		this.pairsToFind = this.gamemode.getCarddeck().getPairs();
		this.endScore = 0;
		this.setTimeSettings(gamemode.getDifficulty());
		
		
		this.timer = new  Timer(1000, this);
		this.timer.setActionCommand("timer");
		this.counter = 0;
		this.timeLeft = new JLabel();
		this.timeLeft.setText(timeFormat(counter));
		this.start();
		
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
		
		JLabel player = new  JLabel("Player name: ");
		JLabel playerName = new  JLabel(this.gamemode.getPlayer1().getName());
		JPanel up = new JPanel();
		up.setLayout(new BoxLayout(up, BoxLayout.PAGE_AXIS));
		up.add(Box.createRigidArea(new Dimension(5,30)));
		up.add(player);
		up.add(playerName);
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
		
		
//		side.setMinimumSize(new Dimension(150, 100));
//		side.setMaximumSize(new Dimension(150,1500));
		side.add(up, BorderLayout.NORTH);
		side.add(timeLeft, BorderLayout.CENTER);
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
						self.bonus(self.bonusTime);
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
		}
		else if (e.getActionCommand().equals("penaltytimer")) {
			PenaltyCard card = this.penaltyBuffer.get(this.penaltyBuffer.size()-1);
			card.setTurned(false);
			card.setIcon(card.getImgback());
		}
		else if (e.getActionCommand().equals("timer")) {
			if (isTimerActive) {
	            counter++;
	            this.timeLeft.setText(timeFormat(counter));
	            //start();
	            if (counter == initTime) {
	            	stop();
	            	JOptionPane.showMessageDialog(this.mainFrame, "Score: 0","Game Over", JOptionPane.ERROR_MESSAGE);
	            }
	        }
		}
		else if (e.getActionCommand().equals("newgame")) {
			String theme = this.gamemode.getTheme();
			this.checkName(this.gamemode.getPlayer1());
			Player p = this.gamemode.getPlayer1();
			p.setScore(0);
			int difficulty = this.gamemode.difficulty;
			GameMode gamemode = new GameMode(theme, p, difficulty);
			this.timer.stop();
			this.getMainFrame().dispose();
			new GameManagerAlone(gamemode);
		}
		else if (e.getActionCommand().equals("exit")) {
			this.timer.stop();
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
				this.checkGameEnd();
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
			this.endScore = this.initTime - this.counter;
			this.gamemode.player1.setScore(this.endScore);
			JOptionPane.showMessageDialog(this.mainFrame, "Score: "+this.endScore,"Game ended", JOptionPane.PLAIN_MESSAGE);
			HighScores.writeAloneScore(this.gamemode.player1);
			return true;
		}
		else return false;
	}
	
	// Method to remember which penaltycards are already turned once and which not. The first time a penaltycard is turned, there will be no 'penalty'
	public void penaltyTurnedUp(PenaltyCard c) {
		if (this.penaltyBuffer.contains(c)) {
			this.penalty(this.penaltyTime);
			// set c at the end of the list:
			this.penaltyBuffer.remove(c);
			this.penaltyBuffer.add(c);
			this.penaltyTimer.start();
		}
		else {
			this.penaltyBuffer.add(c);
			this.penaltyTimer.start();
		}
	}
	
	// Sets some parameters like the total amount of time the player has in order to find the pairs. 
	public void setTimeSettings(int difficulty) {
		switch (difficulty){
		case 1: this.initTime = 60; this.bonusTime = 0; this.penaltyTime = 0; break;
		case 2: this.initTime = 90; this.bonusTime = 0; this.penaltyTime = 0; break;
		case 3: this.initTime = 120; this.bonusTime = 0; this.penaltyTime = 0; break;
		case 4: this.initTime = 150; this.bonusTime = 10; this.penaltyTime = 40; break;
		case 5: this.initTime = 180; this.bonusTime = 10; this.penaltyTime = 40; break;
		case 6: this.initTime = 210; this.bonusTime = 10; this.penaltyTime = 40; break;
		case 7: this.initTime = 240; this.bonusTime = 10; this.penaltyTime = 60; break;
		case 8: this.initTime = 270; this.bonusTime = 10; this.penaltyTime = 60; break;
		case 9: this.initTime = 300; this.bonusTime = 10; this.penaltyTime = 60; break;
		default: this.initTime = 0; this.bonusTime = 0; this.penaltyTime = 0;
		}
	}
	
	// Removes all actionlisteners from the Cards so that no action can be performed when the game has ended. 
	public void stop() {
    	this.isTimerActive = false;
    	for (int i = 0; i<this.gamemode.getCarddeck().getCardList().size(); i++) {
    		//if (this.gamemode.getCarddeck().getCardList().get(i).isTurned() == false) {
    			this.gamemode.getCarddeck().getCardList().get(i).setIcon(this.gamemode.getCarddeck().getCardList().get(i).getImg());
    			this.gamemode.getCarddeck().getCardList().get(i).removeActionListener(this.gamemode.getCarddeck().getCardList().get(i).getActionListeners()[0]);
    		//}
    	}
    	this.timer.stop();
    }
    
	// Methods for the stopwatch:
    public void start() {
        this.isTimerActive = true;
        this.timer.start();
    }
    
    private String timeFormat(int c) {
    	int count = initTime - c;
        int hours = count / 3600;
        int minutes = (count-hours*3600)/60;
        int seconds = count-minutes*60;
        return " Time left: "+String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
    
    public void bonus(int bonus) {
    	this.counter -= bonus;
    }
    
    public void penalty(int penalty) {
    	if (this.initTime - this.counter <= penalty) {
    		this.timeLeft.setText(timeFormat(this.initTime));
    		this.stop();
    		JOptionPane.showMessageDialog(this.mainFrame, "Score: 0","Game Over", JOptionPane.ERROR_MESSAGE);
    	}
    	else this.counter += penalty;
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

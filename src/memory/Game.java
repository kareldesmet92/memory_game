package memory;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Game implements ActionListener, ChangeListener {
	
	// Different components on the menuframe:
	private JFrame menuFrame;
	private JLabel difficultyLabel;
	private JSlider difficultySlider;
	private JLabel opponents;
	private ButtonGroup opponentsGroup;
	private JLabel player1 = new JLabel("Player1's name:  ");
	private JTextField player1text = new JTextField();
	private JLabel player2 = new JLabel("Player2's name:  ");
	private JTextField player2text = new JTextField();
	private JPanel playersPanel;
	private JLabel themeLabel;
	private JComboBox<String> themeList;
	private JButton highScores;
	private JButton play;
	private JButton exit;	
	private int difficulty;
	private String opponent;
	private String theme;
		
	public Game() {
		this.menuFrame = new JFrame("Memory Game menu");
		this.menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.menuFrame.getContentPane().setLayout(new BoxLayout(this.menuFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		this.menuFrame.getContentPane().setPreferredSize(new Dimension(800,600));
		this.menuFrame.getContentPane().setMinimumSize(new Dimension(800,600));
		this.menuFrame.getContentPane().add(Box.createRigidArea(new Dimension(10,10)));
		this.opponent = "alone";
		this.difficulty = 1;
		this.theme = "Fruit";
		this.highScores = new JButton("High Scores");
		this.play = new JButton("Play");
		this.exit = new JButton("Exit");
		this.player1text.setText(null);
		this.player2text.setText(null);
		
		// Difficulty panel:
		this.difficultyLabel = new JLabel("Set difficulty:      "); 
		this.difficultySlider = new JSlider(SwingConstants.HORIZONTAL,1,9,1);
		this.difficultySlider.setLabelTable(this.difficultySlider.createStandardLabels(1));
		this.difficultySlider.setPaintLabels(true);
		this.difficultySlider.setPreferredSize(new Dimension(500,100));
		JPanel difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new FlowLayout());
		difficultyPanel.add(this.difficultyLabel);
		difficultyPanel.add(this.difficultySlider);
		difficultyPanel.setPreferredSize(new Dimension(700,80));
		difficultyPanel.setMaximumSize(difficultyPanel.getPreferredSize());
		this.menuFrame.getContentPane().add(difficultyPanel);
		
		this.menuFrame.getContentPane().add(Box.createRigidArea(new Dimension(20,50)));
		
		// Opponents panel
		this.opponents = new JLabel("Select your opponent:      ");
		JRadioButton alone = new JRadioButton("Alone", true);
		alone.setActionCommand("alone");
		JRadioButton computer = new JRadioButton("Computer");
		computer.setActionCommand("computer");
		JRadioButton human = new JRadioButton("Human");
		human.setActionCommand("human");
		this.opponentsGroup = new ButtonGroup();
		this.opponentsGroup.add(alone);
		this.opponentsGroup.add(computer);
		this.opponentsGroup.add(human);
		JPanel opponentsPanel = new JPanel(new FlowLayout());
		opponentsPanel.add(this.opponents);
		JPanel list = new JPanel(new GridLayout(0,1));
		list.add(alone);
		list.add(computer);
		list.add(human);
		opponentsPanel.add(list);
		opponentsPanel.setPreferredSize(new Dimension(700,100));
		opponentsPanel.setMaximumSize(difficultyPanel.getPreferredSize());
		this.menuFrame.getContentPane().add(opponentsPanel);
		
		this.menuFrame.getContentPane().add(Box.createRigidArea(new Dimension(20,30)));
		
		// Players panel
		this.playersPanel = makePlayersPanel(2);
		this.player2.setVisible(false);
		this.player2text.setVisible(false);
		this.menuFrame.getContentPane().add(playersPanel);;
		
		this.menuFrame.getContentPane().add(Box.createRigidArea(new Dimension(20,30)));
		
		// Theme panel:
		JPanel themePanel = new JPanel(new FlowLayout());
		this.themeLabel = new JLabel("Select theme:  ");
		themePanel.add(this.themeLabel);
		this.themeList = new JComboBox<String>(new String[] {"Fruit", "Presidents" ,"Landmarks"});
		this.themeList.setActionCommand("themeselect");
		themePanel.add(this.themeList);
		themePanel.setPreferredSize(new Dimension(700,100));
		themePanel.setMaximumSize(themePanel.getPreferredSize());
		this.menuFrame.getContentPane().add(themePanel);
		
		this.menuFrame.getContentPane().add(Box.createRigidArea(new Dimension(20,30)));
		
		// Bottom panel with buttons:
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 50,0));
		buttons.add(this.highScores);
		buttons.add(this.play);
		buttons.add(this.exit);
		this.highScores.setActionCommand("highScores");
		this.play.setActionCommand("play");
		this.exit.setActionCommand("exit");
		buttons.setPreferredSize(new Dimension(700,100));
		buttons.setMaximumSize(buttons.getPreferredSize());
		this.menuFrame.getContentPane().add(buttons);
		
		
		// Action Listeners:
		this.difficultySlider.addChangeListener(this);
		alone.addActionListener(this);
		computer.addActionListener(this);
		human.addActionListener(this);
		this.themeList.addActionListener(this);
		this.highScores.addActionListener(this);
		this.play.addActionListener(this);
		this.exit.addActionListener(this);
		
		this.menuFrame.pack();
		this.menuFrame.setVisible(true);
	}
	
	
	public void play() {	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("alone")) {
			this.player2text.setText(null);
			this.player2.setVisible(false);
			this.player2text.setVisible(false);
			this.opponent = "alone";
			System.out.println("Opponent: "+this.opponent);
		}
		else if (e.getActionCommand().equals("computer")){
			this.player2text.setText(null);
			this.player2.setVisible(false);
			this.player2text.setVisible(false);
			this.opponent = "computer";
			System.out.println("Opponent: "+this.opponent);		
			}
		else if (e.getActionCommand().equals("human")){
			this.player2.setVisible(true);
			this.player2text.setVisible(true);
			this.opponent = "human";
			System.out.println("Opponent: "+this.opponent);		
			}
		else if (e.getActionCommand().equals("themeselect")) {
			this.theme = (String) this.themeList.getSelectedItem();
			System.out.println("Selected theme: "+this.theme);
		}
		
		else if (e.getActionCommand().equals("highScores")) {
			new HighScores();
		}
		else if (e.getActionCommand().equals("play")) {
			String p1;
			String p2;
			try {	// to check if name of player 1 is filled in: 
				p1 = this.player1text.getText();	
				if (p1.equals("")) throw new Exception();
			}
			catch (NullPointerException n){
				JOptionPane.showMessageDialog(this.menuFrame, "Please provide players' names","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch (Exception e1) {
				JOptionPane.showMessageDialog(this.menuFrame, "Please provide players' names","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		
			if (this.opponent.equals("alone")) {
				Player p = new Player(p1, 0);
				GameMode gameAlone = new GameMode(this.theme, p, this.difficulty);
				new GameManagerAlone(gameAlone);
			}
			else if (this.opponent.equals("computer")) {
				Player pa = new Player(p1, 0);
				Player pb = new Player("Computer", 0);
				GameMode gameComputer = new GameMode(this.theme, pa, pb, this.difficulty);
				new GameManagerComputer(gameComputer);
			}
			else if (this.opponent.equals("human")){
				try {
					p2 = this.player2text.getText();
					if (p2.equals("")) throw new Exception();
				}
				catch (NullPointerException n){
					JOptionPane.showMessageDialog(this.menuFrame, "Please provide player's names","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this.menuFrame, "Please provide player's names","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Player pa = new Player(p1, 0);
				Player pb = new Player(p2, 0);
				GameMode gameHuman = new GameMode(this.theme, pa, pb , this.difficulty);
				new GameManagerHuman(gameHuman);
			}
		}
		else if (e.getActionCommand().equals("exit")) {
			this.menuFrame.dispose();
			System.exit(0);

		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		this.difficulty = this.difficultySlider.getValue();
		System.out.println("Difficulty: "+this.difficulty);
	}
	
	// Make the boxes for the names to be filled in: 
	public JPanel makePlayersPanel(int num) {
		this.player1.setHorizontalAlignment(JLabel.RIGHT);
		this.player2.setHorizontalAlignment(JLabel.RIGHT);
		JPanel playersPanel = new JPanel(new GridLayout(1,2*num));
		if (num == 1) {
			playersPanel.add(this.player1);
			playersPanel.add(this.player1text);
			playersPanel.setPreferredSize(new Dimension(300, 30));
			playersPanel.setMaximumSize(playersPanel.getPreferredSize());
		}
		else {
			playersPanel.add(this.player1);
			playersPanel.add(this.player1text);
			playersPanel.add(this.player2);
			playersPanel.add(this.player2text);
			playersPanel.setPreferredSize(new Dimension(600, 30));
			playersPanel.setMaximumSize(playersPanel.getPreferredSize());
		}
		return playersPanel;
	}
	
	
	// Start the game in main method:
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Game();
			}
		});
	}
}

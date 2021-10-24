package memory;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HighScores extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel aloneLabel = new JLabel("Top Scores Alone Game: ");
	private JLabel computerLabel = new JLabel("Top Scores Computer Game: ");
	
	public HighScores() {
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
		getContentPane().setPreferredSize(new Dimension(700,300));
		getContentPane().setMaximumSize(new Dimension(700, 300));
		JPanel alonePanel = new JPanel();
		alonePanel.setLayout(new BoxLayout(alonePanel, BoxLayout.PAGE_AXIS));
		JPanel computerPanel = new JPanel();
		computerPanel.setLayout(new BoxLayout(computerPanel, BoxLayout.PAGE_AXIS));
		ArrayList<Player> aloneScores = readAloneScores();
		ArrayList<Player> computerScores = readComputerScores();
		
		alonePanel.add(aloneLabel);
		alonePanel.add(Box.createRigidArea(new Dimension(20,30)));
		
		JPanel aloneGrid = new JPanel(new GridLayout(10,2));
		for(int i = 0; i<aloneScores.size(); i++) {
			JLabel place = new JLabel((i+1)+".    ", JLabel.RIGHT);
			aloneGrid.add(place);
			JLabel score = new JLabel(aloneScores.get(i).getScore()+"  "+aloneScores.get(i).getName(), JLabel.LEFT);
			aloneGrid.add(score);
			
		}
		alonePanel.add(aloneGrid);
		
		computerPanel.add(computerLabel);
		computerPanel.add(Box.createRigidArea(new Dimension(20,30)));
		
		JPanel computerGrid = new JPanel(new GridLayout(10,2));
		for(int i = 0; i<computerScores.size(); i++) {
			JLabel place = new JLabel((i+1)+".    ", JLabel.RIGHT);
			computerGrid.add(place);
			JLabel score = new JLabel(computerScores.get(i).getScore()+"  "+computerScores.get(i).getName(), JLabel.LEFT);
			computerGrid.add(score);
		}
		computerPanel.add(computerGrid);
		
		getContentPane().add(alonePanel);
		getContentPane().add(computerPanel);
		
		pack();
		setVisible(true);
	}
	
	// Read highscores from file
	public static ArrayList<Player> readAloneScores(){
		ArrayList<Player> topscores = new ArrayList<Player>(10);
		try(Scanner scanner = new Scanner(new FileReader("highscores\\highscoresAlone.txt"))){
			while (scanner.hasNext()) {
				int score = scanner.nextInt();
				String name = scanner.next();
				Player p = new Player(name, score);
				topscores.add(p);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalStateException i) {
			System.out.println("Read alonescores not succeeded");
		}
		return topscores;
	}
	
	// Read highscores from file
	public static ArrayList<Player> readComputerScores() {
		ArrayList<Player> topscores = new ArrayList<Player>(10);
		try(Scanner scanner = new Scanner(new FileReader("highscores\\highscoresComputer.txt"))){
			while (scanner.hasNext()) {
				int score = scanner.nextInt();
				String name = scanner.next();
				Player p = new Player(name, score);
				topscores.add(p);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalStateException i) {
			System.out.println("Read computerscores not succeeded");
		}
		return topscores;
	}
	
	// Write highscores to file
	public static void writeAloneScore(Player player) {
		player.setName("("+player.getName()+")");
		ArrayList<Player> scores = readAloneScores();
		for (int i=0; i<10; i++) {
			if (player.getScore() >= scores.get(i).getScore()) {
				scores.add(i, player);
				break;
			}
		}
		try {
			FileWriter writer = new FileWriter("highscores\\\\highscoresAlone.txt");
			for(int i=0; i<10; i++) {
				writer.write(scores.get(i).getScore()+"  "+scores.get(i).getName()+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Write highscores to file
	public static void writeComputerScore(Player player) {
		player.setName("("+player.getName()+")");
		ArrayList<Player> scores = readComputerScores();
		for (int i=0; i<10; i++) {
			if (player.getScore() >= scores.get(i).getScore()) {
				scores.add(i, player);
				break;
			}
		}
		try {
			FileWriter writer = new FileWriter("highscores\\highscoresComputer.txt");
			for(int i=0; i<10; i++) {
				writer.write(scores.get(i).getScore()+"  "+scores.get(i).getName()+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

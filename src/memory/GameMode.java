package memory;

public class GameMode {

	protected CardDeck carddeck;
	protected Player player1;
	protected Player player2;
	protected int rows;
	protected int columns;
	protected String theme;
	protected int difficulty;
	
	public GameMode(String theme, Player player1, int difficulty) {
		this.setTheme(theme);
		this.setPlayer1(player1);
		this.setDifficulty(difficulty);
		this.setPlayer2(null);
		this.setCarddeck(this.makeCardDeck(theme, difficulty));
	}
	
	public GameMode(String theme, Player player1, Player player2, int difficulty) {
		this(theme, player1, difficulty);
		this.setPlayer2(player2);
	}
		
	public CardDeck makeCardDeck(String theme, int difficulty) {
		CardDeck carddeck;
		switch (difficulty) {
		case 1: carddeck = new CardDeck(theme, 6, 0, 0); this.setRows(3); this.setColumns(4); break;
		case 2: carddeck = new CardDeck(theme, 10, 0, 0); this.setRows(4); this.setColumns(5); break;
		case 3: carddeck = new CardDeck(theme, 12, 0, 0); this.setRows(4); this.setColumns(6); break;
		case 4: carddeck = new CardDeck(theme, 12, 0, 1); this.setRows(5); this.setColumns(5); break;
		case 5: carddeck = new CardDeck(theme, 14, 1, 1); this.setRows(5); this.setColumns(6); break;
		case 6: carddeck = new CardDeck(theme, 16, 1, 2); this.setRows(5); this.setColumns(7); break;
		case 7: carddeck = new CardDeck(theme, 17, 0, 2); this.setRows(6); this.setColumns(6); break;
		case 8: carddeck = new CardDeck(theme, 22, 2, 3); this.setRows(7); this.setColumns(7); break;
		case 9: carddeck = new CardDeck(theme, 23, 0, 3); this.setRows(7); this.setColumns(7); break;
		default: carddeck = null;
		}
		return carddeck;
	}	
	
	// Choose randomly which player can start
	public void randomPlayerOrder() {
		double randomNumber = Math.random();
		if (randomNumber >= 0.5) {
			Player temp = this.player1;
			this.setPlayer1(this.player2);
			this.setPlayer2(temp);
		}
	}
	
	public CardDeck getCarddeck() {
		return carddeck;
	}

	public void setCarddeck(CardDeck carddeck) {
		this.carddeck = carddeck;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}

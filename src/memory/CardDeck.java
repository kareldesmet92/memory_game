package memory;

import java.util.ArrayList;

public class CardDeck {

	private int numCards; 				// total number of cards
	private int pairs;					// total pairs
	private int bonuscards;				// total bonus cards
	private int penaltycards;			// total penalty cards
	private ArrayList<Card> cardList;	// list of all cards
	private ArrayList<NormalCard> cardPairList;		// list of all pairs
	
	public CardDeck(String theme, int pairs, int bonuscards, int penaltycards) {
		this.cardList = new ArrayList<Card>();
		this.cardPairList = new ArrayList<NormalCard>();
		this.setNumCards(2*pairs+bonuscards+penaltycards);
		for (int i = 0; i< 2*pairs; i=i+2) {
			CardPair cardpair = new CardPair(theme, i, i+1);
			cardList.add(cardpair.getCard1());
			cardList.add(cardpair.getCard2());
			cardPairList.add(cardpair.getCard1());
			cardPairList.add(cardpair.getCard2());
		}
		for (int i = 0; i < bonuscards; i++) {
			cardList.add(new BonusCard(i+2*pairs));
		}
		for (int i = 0; i < penaltycards; i++) {
			cardList.add(new PenaltyCard(i+2*pairs+bonuscards));
		}
		this.setPairs(pairs);
		this.setBonuscards(bonuscards);
		this.setPenaltycards(penaltycards);
		this.shuffle();
	}

	// shuffle the card deck
	public void shuffle() {
		for (int i = this.numCards-1; i>0; i--) {
			int j = (int) Math.floor(Math.random() * (i+1));
			Card temp = this.cardList.get(i);
			this.cardList.set(i, this.cardList.get(j));
			this.cardList.set(j, temp);
		}
	}

	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}

	public ArrayList<Card> getCardList() {
		return cardList;
	}
	
	public void setCardList(ArrayList<Card> cardList) {
		this.cardList = cardList;
	}	
	
	public ArrayList<NormalCard> getCardPairList() {
		return cardPairList;
	}

	public void setCardPairList(ArrayList<NormalCard> cardPairList) {
		this.cardPairList = cardPairList;
	}

	public int getPairs() {
		return pairs;
	}

	public void setPairs(int pairs) {
		this.pairs = pairs;
	}

	public int getBonuscards() {
		return bonuscards;
	}

	public void setBonuscards(int bonuscards) {
		this.bonuscards = bonuscards;
	}

	public int getPenaltycards() {
		return penaltycards;
	}

	public void setPenaltycards(int penaltycards) {
		this.penaltycards = penaltycards;
	}
}

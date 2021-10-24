package memory;

public class CardPair {

	private NormalCard card1;
	private NormalCard card2;
	
	public CardPair(String location, int id1, int id2) {
		card1 = new NormalCard(location, id1);
		card2 = new NormalCard(location, id1);	// Making sure that both cards contain the same image -> id in constuctor must initially be the same
		card2.setId(id2);						// After creation, change id of second card to the correct id. 
		card1.setPairId(id2);
		card2.setPairId(id1);
	}

	public NormalCard getCard1() {
		return card1;
	}

	public void setCard1(NormalCard card1) {
		this.card1 = card1;
	}

	public NormalCard getCard2() {
		return card2;
	}

	public void setCard2(NormalCard card2) {
		this.card2 = card2;
	}
}
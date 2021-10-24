package memory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class NormalCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int pairId;			// Id of matching card
	private boolean matched;	// boolean to see if this card is already matched or not
	
	public NormalCard(String location, int id) {
		super(id);
		this.setImg(location);
//		this.setIcon(super.img);
		this.setMatched(false);
	}
	
	@Override
	public void setImg(String location) {
		BufferedImage i = null;
		try {
			File file = new File("images\\"+location+"\\"+location+"_"+super.id+".jpg");
			i = ImageIO.read(file);
		}
		catch (IOException io) {
			System.out.println("Image not loaded");
		}
		this.img = new ImageIcon(i);
		
	}
	
	public int getPairId() {
		return pairId;
	}

	public void setPairId(int pairid) {
		this.pairId = pairid;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}

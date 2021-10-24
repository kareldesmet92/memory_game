package memory;

//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

abstract public class Card extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int id;				// Id of this card
	protected boolean turned;		// boolean to see if this card is turned or not
	protected ImageIcon img;		// figure image
	protected ImageIcon imgback;	// back image
	
	public Card(int id) {
		this.setId(id);
		this.setTurned(false);
		this.setImgback();
		this.setIcon(imgback);
	}
	
	public ImageIcon getImgback() {
		return imgback;
	}
	
	// Sets the figure for this card
	public void setImg(String location) {
		BufferedImage i = null;
		try {
			i = ImageIO.read(new File("images\\"+location+"\\"+location+".jpg"));
		}
		catch (IOException io) {
			System.out.println("Image not loaded");
		}
		this.img = new ImageIcon(i);
	}

	// sets the back for this card
	public void setImgback() {
		BufferedImage i = null;
		try {
			i = ImageIO.read(new File("images\\background\\background_1.jpg"));
		}
		catch (IOException io) {
			System.out.println("Image not loaded");
		}
		this.imgback = new ImageIcon(i);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isTurned() {
		return turned;
	}

	public void setTurned(boolean turned) {
		this.turned = turned;
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
}

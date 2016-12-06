package agentville.games.wumpus.world.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Panel für die Anzeige eines Knotens der Welt.
 * 
 * @author Marco Steffens
 */
public class WorldNodePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

//	private Logger log = Logger.getMyLogger(this.getClass().getName());

    private List<BufferedImage> images = new ArrayList<BufferedImage>();

	public WorldNodePanel(BufferedImage image) {
		
		this.setLayout(new BorderLayout());
		this.images.add(image);
	}
	
	public void changeContent(BufferedImage image) {
		
		this.images.add(image);
		this.repaint();
	}
	
    @Override
    public void paintComponent(Graphics g) {

        //TODO: Liste sortiert!
        for (BufferedImage image : images)
        	g.drawImage(image, 0, 0, null);
    }
	
}

package agentville.games.wumpus.world.view;

import jade.util.Logger;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import agentville.games.wumpus.world.listener.WorldModelListener;
import agentville.games.wumpus.world.model.World;
import agentville.games.wumpus.world.model.WorldNode;

public class WorldPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getMyLogger(this.getClass().getName());

	World model;
	WorldNode[][] worldMap;
	WorldNodePanel[][] worldGUI;

	GridBagLayout gbl;
	GridBagConstraints gbc;
	JLabel label;

	BufferedImage gold = null;
	BufferedImage pit = null;
	BufferedImage wumpus = null;
	BufferedImage hunter = null;
	BufferedImage empty = null;

	public WorldPanel(World world) {
		
		this.model = world;
		this.worldMap = model.getWorldMap();
		this.worldGUI = new WorldNodePanel[4][4];

		try {                
			gold = ImageIO.read(new File("src/main/resources/images/gold2.png"));
			pit = ImageIO.read(new File("src/main/resources/images/pit2.png"));
			wumpus = ImageIO.read(new File("src/main/resources/images/wumpus2.png"));
			hunter = ImageIO.read(new File("src/main/resources/images/hunter2.png"));
			empty = ImageIO.read(new File("src/main/resources/images/default.png"));
		} catch (IOException ex) {
			// handle exception...
	    	if(log.isLoggable(Logger.WARNING))
	    		log.log(Logger.WARNING, ex.getMessage().toString());
		}
		
		this.setBorder(new TitledBorder(null, "World", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//		this.setBounds(10, 10, 350, 350);
		this.setBounds(0, 0, 350, 350);
		this.setPreferredSize(new Dimension(350,350));

		gbl = new GridBagLayout();
		
		gbl.columnWidths = new int[]{25, 75, 75, 75, 75, 0};
		gbl.rowHeights = new int[]{80, 75, 75, 75, 14, 0};
		gbl.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl);
		
		label = new JLabel("x");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 5, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.add(label, gbc);
		
		//x-Label (horizontal)
		for (int i=0; i<4;i++) {
			label = new JLabel(""+(i+1));
			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 0, 0);
			gbc.gridx = i+1;
			gbc.gridy = 4;
			this.add(label, gbc);
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}

		label = new JLabel("y");
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(label, gbc);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		//y-Label (vertical)
		for (int j=0; j<4;j++) {
			label = new JLabel(""+(j+1));
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			if (j==0)
				gbc.insets = new Insets(10, 5, 0, 0);
			else
				gbc.insets = new Insets(5, 5, 0, 0);
			gbc.gridx = 0;
			gbc.gridy = ((j+4)-(j*2)-1);
			this.add(label, gbc);
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				worldGUI[i][j] = new WorldNodePanel(gold);

				gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;
				if(j==3)
					gbc.insets = new Insets(10, 5, 0, 0);
				else
					gbc.insets = new Insets(5, 5, 0, 0);
				gbc.gridx = i+1;//1;
				gbc.gridy = ((j+4)-(j*2)-1);
				this.add(worldGUI[i][j], gbc);
			}
		}

		setValues(worldMap);
		
		model.addWorldModelListener(new WorldModelListener() {
			public void worldModelChanged(final WorldNode[][] world) {
				setValues(world);
			}
		});
	}

	protected void setValues(WorldNode[][] world) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "World-Panel wird neu gezeichnet.");
    	
    	/*
    	 * TODO: komische Exception, nicht unmittelbar reproduzierbar: 
    	 * Exception in thread "AWT-EventQueue-0" java.util.ConcurrentModificationException
		 *		at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:819)
		 *		at java.util.ArrayList$Itr.next(ArrayList.java:791)
		 *		at jade.game.wumpus.view.WorldNodePanel.paintComponent(WorldNodePanel.java:40)
		 *		at javax.swing.JComponent.paint(JComponent.java:1054)
		 *      ...
		 * Das ganze "paint" nochmal überarbeiten.
    	 */

		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				
				worldGUI[i][j].changeContent(empty);
				if(worldMap[i][j].hasPit())
					worldGUI[i][j].changeContent(pit);
				if(worldMap[i][j].hasWumpus())
					worldGUI[i][j].changeContent(wumpus);					
				if(worldMap[i][j].hasGold())
					worldGUI[i][j].changeContent(gold);
				if(worldMap[i][j].hasHunter())
					worldGUI[i][j].changeContent(hunter);

				gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;
				if(j==3)
					gbc.insets = new Insets(10, 5, 0, 0);
				else
					gbc.insets = new Insets(5, 5, 0, 0);
				gbc.gridx = i+1;
				gbc.gridy = ((j+4)-(j*2)-1);
				this.add(worldGUI[i][j], gbc);
			}
		}
	}
}

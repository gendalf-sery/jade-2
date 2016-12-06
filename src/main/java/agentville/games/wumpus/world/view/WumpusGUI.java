package agentville.games.wumpus.world.view;

import jade.util.Logger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import agentville.games.wumpus.world.WumpusAgent;

public class WumpusGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getMyLogger(this.getClass().getName());

	private WumpusAgent model;
	
	//Event-Type für das GuiEvent-Objekt
	public static final int EVENT_CLOSE = 0;

	public WumpusGUI(WumpusAgent wumpus) {
		
		this.model = wumpus;
		final Toolkit tk = Toolkit.getDefaultToolkit();
		final Dimension screensize = tk.getScreenSize();
		Dimension windowsize;// = new Dimension(680, 575);
		
		this.setTitle(model.getLocalName() + " GUI - Version 1.0");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			doClose();}});
	
		int hgap = 20;
		int vgap = 10;

		this.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, hgap, vgap));

		pack(); //ohne "pack" sind die insets 0
		Insets insets = this.getInsets(); //Rahmendicke des Fensters, incl. evtl. Menü
		
		WorldPanel worldPanel = new WorldPanel(model.getWorld());

		this.getContentPane().add(worldPanel);

		HunterPanel hunterPanel = new HunterPanel(model.getWorld());
		this.getContentPane().add(hunterPanel);

		windowsize = new Dimension(
				insets.left+10+worldPanel.getWidth()+40+hunterPanel.getWidth()+10+insets.right, 
				insets.top+10+worldPanel.getHeight()+10+insets.bottom+10);

		this.setPreferredSize(windowsize);
		this.setSize(windowsize);
		this.setLocation(screensize.width/2-windowsize.width/2,
				screensize.height/2-windowsize.height/2);
		
		this.pack();
	}
	
	public void doClose() {

		dispose(); 
	}
}

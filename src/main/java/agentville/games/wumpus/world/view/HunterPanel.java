package agentville.games.wumpus.world.view;

import jade.util.Logger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import agentville.games.wumpus.world.listener.HunterListListener;
import agentville.games.wumpus.world.model.Hunter;
import agentville.games.wumpus.world.model.World;

public class HunterPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getMyLogger(this.getClass().getName());

	List<Hunter> model;

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc;

	JTable table = null;
	TableModel tableModel = null;

	String[][] rowData = null;
	String[] columnNames = {"Nr.", "Name", "Performance", "State"};

	JScrollPane scrollpane = null;
	
	public HunterPanel(World world) {
		
		this.model = world.getHunterList();

		this.setBorder(new TitledBorder("Hunter"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));//new BorderLayout());
		this.setPreferredSize(new Dimension(250,350));
		this.setBounds(0, 0, 250, 350);
		//this.setSize(250,350);

		rowData = new String[10][4];
		for (int i=0; i<10; i++) {
			rowData[i][0] = "";
			rowData[i][1] = "";
			rowData[i][2] = "";
			rowData[i][3] = "";
		}

		tableModel = new DefaultTableModel(rowData, columnNames);
		table = new JTable(tableModel);
		table.setPreferredSize(new Dimension(100,100));
		table.setRowHeight(20);
		table.setRowMargin(4);
		table.setShowGrid(false);

		scrollpane = new JScrollPane(table);
//		scrollpane.setSize(100,100);
//		scrollpane.setBounds(50,50,50,50);
		scrollpane.setPreferredSize(new Dimension(230,320));
		this.add(scrollpane);//, BorderLayout.CENTER);
		
		world.addHunterListListener(new HunterListListener() {
			public void hunterListModelChanged(List<Hunter> hunters) {

		    	updateList(hunters);
			}
		});
	}

	protected void updateList(List<Hunter> hunters) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "updateList(), size: " + hunters.size());
		
		this.model = hunters;
		
		rowData = new String[10][4];
		for (int i=0; i<10; i++) {
			tableModel.setValueAt("", i, 0);
			tableModel.setValueAt("", i, 1);
			tableModel.setValueAt("", i, 2);
			tableModel.setValueAt("", i, 3);
		}

		Hunter hunter = null;
		for (int i=0; i<this.model.size() && i<10; i++) {
			hunter = hunters.get(i);
			tableModel.setValueAt(String.valueOf(hunter.getId()), i, 0);
			tableModel.setValueAt(hunter.getName(), i, 1);
			tableModel.setValueAt(String.valueOf(hunter.getPerformance()), i, 2);
			tableModel.setValueAt(hunter.getState(), i, 3);
		}
	}

}

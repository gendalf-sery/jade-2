package agentville.games.wumpus.world.model;

import java.util.ArrayList;
import java.util.List;

public class WorldNode {

//	private Logger log = Logger.getMyLogger(this.getClass().getName());

	int xCoord;
	int yCoord;
	boolean pit;
	boolean wumpus;
	boolean isWumpusAlive;
	boolean gold;
	List<Hunter> hunters;

	public WorldNode(int xCoord, int yCoord) {
		
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.pit = false;
		this.wumpus = false;
		this.isWumpusAlive = false;
		this.gold = false;
		this.hunters  = new ArrayList<Hunter>();
	}

	public boolean hasPit() {
		return pit;
	}

	public boolean hasWumpus() {
		return wumpus;
	}

	public boolean hasGold() {
		return gold;
	}

	public boolean hasNoPit() {
		return !pit;
	}

	public boolean hasNoWumpus() {
		return !wumpus;
	}

	public boolean hasNoGold() {
		return !gold;
	}
	
	public boolean isEmpty() {
		if (hasNoGold() && hasNoWumpus() && hasNoPit())
			return true;

		return false;
	}

	public int getxCoord() {
		return xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}
	
	/**
	 * 
	 * @return <b>true</b> wenn Wumpus vorhanden und am leben, <br>
	 * <b>false</b> wenn Wumpus tot oder gar nicht vorhanden.
	 */
	public boolean isWumpusAlive() {
		
		return this.isWumpusAlive;
	}

	public void setWumpus(boolean wumpus) {
		this.wumpus = wumpus;
		this.isWumpusAlive = true;
	}

	public void setGold(boolean gold) {
		this.gold = gold;
	}

	public boolean hasHunter() {
		if (hunters.size() > 0)
			return true;
		
		return false;
	}

	public void setWumpusAlive(boolean state) {
		
		this.isWumpusAlive = state;
	}
}

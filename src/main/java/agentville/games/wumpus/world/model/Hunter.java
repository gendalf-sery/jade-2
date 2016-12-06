package agentville.games.wumpus.world.model;

import jade.core.AID;
import agentville.games.wumpus.world.WumpusConsts;
import agentville.games.wumpus.world.WumpusPerception;

public class Hunter {
	
	private int id;
	private String name;
	private AID hunterAID;
	private int xCoord;
	private int yCoord;
	private int xDirection;
	private int yDirection;
	private boolean hasArrow;
	private String state;
	private int performance;
	private WumpusPerception perception;
	
	public Hunter(AID hunter) {
		
		this.hunterAID = hunter;
		this.name = this.hunterAID.getLocalName();
		this.xCoord = 0;
		this.yCoord = 0;
		this.xDirection = 1;
		this.yDirection = 0;
		this.hasArrow = true;
		this.state = WumpusConsts.STATE_ALIVE;
		this.performance = 0;
		this.perception = new WumpusPerception();
		
	}
	
	public Hunter turnLeft() {
		//Drehung um 90 Grad nach links, aufgelöste Drehmatrix und so.
		
		int x = xDirection;
		int y = yDirection;
		
		this.xDirection = y * (-1); 
		this.yDirection = x * 1;
		this.performance = this.performance -1;
		
		return this;
	}

	public void turnRight() {
		//Drehung um 90 Grad nach rechts.
		int x = xDirection;
		int y = yDirection;

		this.xDirection = y * 1; 
		this.yDirection = x * (-1); 
		this.performance = this.performance -1;
	}
	
	public Hunter move() {
		
		int newX = this.xCoord + this.xDirection;
		int newY = this.yCoord + this.yDirection;
		
		if (0 > newX || newX >= 4 || 0 > newY || newY >= 4){ 
			//--> bump, exit}
		} else { 
			this.xCoord = this.xCoord + this.xDirection;
			this.yCoord = this.yCoord + this.yDirection;
		}
		this.performance = this.performance -1;
		
		return this;
	}

	public void shoot() {
		this.performance = this.performance -1;

		if (this.hasArrow){
			this.setArrow(false);
			this.performance = this.performance -10;
		}
	}

	public void grab(Boolean hasGold) {
		
		this.performance = this.performance -1;

		if (hasGold) {
			this.performance = this.performance + 1000;
			this.setState(WumpusConsts.STATE_GOAL);
		}	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hunterAID == null) ? 0 : hunterAID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Hunter))
			return false;
		Hunter other = (Hunter) obj;
		if (hunterAID == null) {
			if (other.hunterAID != null)
				return false;
		} else if (!hunterAID.equals(other.hunterAID))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AID getHunterAID() {
		return hunterAID;
	}

	public void setHunterAID(AID hunter) {
		this.hunterAID = hunter;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public int getxDirection() {
		return xDirection;
	}

	public void setxDirection(int xDirection) {
		this.xDirection = xDirection;
	}

	public int getyDirection() {
		return yDirection;
	}

	public void setyDirection(int yDirection) {
		this.yDirection = yDirection;
	}

	public boolean hasArrow() {
		return hasArrow;
	}

	public void setArrow(boolean hasArrow) {
		this.hasArrow = hasArrow;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPerformance() {
		return performance;
	}

	public void setPerformance(int performance) {
		this.performance = performance;
	}

	public WumpusPerception getPerception() {
		return this.perception;
	}
}

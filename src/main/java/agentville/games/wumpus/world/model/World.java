package agentville.games.wumpus.world.model;

import jade.core.AID;
import jade.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agentville.games.wumpus.world.WumpusConsts;
import agentville.games.wumpus.world.WumpusPerception;
import agentville.games.wumpus.world.listener.HunterListListener;
import agentville.games.wumpus.world.listener.WorldModelListener;

public class World {
	
	private Logger log = Logger.getMyLogger(this.getClass().getName());

	private WorldNode[][] worldMap = null;
	private List<WorldModelListener> listenersWorld;

	private List<Hunter> hunters;
	private List<HunterListListener> listenersHunter;

	public World() {
		
		worldMap = new WorldNode[4][4];
		listenersWorld = new ArrayList<WorldModelListener>();

		hunters = new ArrayList<Hunter>();
		listenersHunter = new ArrayList<HunterListListener>();

		initWorld();
	}

	private void initWorld() {
		/*
		 * Die Knoten der Welt werden erzeugt
		 */
		for (int i = 0; i < worldMap.length; i++ ) {
			for (int j = 0; j < worldMap[i].length; j++) {
				worldMap[i][j] = new WorldNode(i, j);
			}
		}
		
		/*
		 * Wumpus und Gold sind jeweils einmalig vorhanden
		 * und zufällig verteilt, aber niemals auf dem gleichen
		 * Feld und niemals an Position 0,0.
		 */
		Random r = new Random();
		int x, y;
		boolean placed = false;
		while(!placed){
			//Wumpus zufällig plazieren
			x = r.nextInt(worldMap.length);
			y = r.nextInt(worldMap[1].length);
			if(x != 0 || y != 0){
				worldMap[x][y].setWumpus(true);
				placed = true;
			}
		}

		placed = false;
		while (!placed) {
			//Gold zufällig plazieren (aber nicht wo der Wumpus ist)
			x = r.nextInt(worldMap.length);
			y = r.nextInt(worldMap[1].length);
			if ((x != 0 || y != 0) && worldMap[x][y].hasNoWumpus()) {//isEmpty()) {
				worldMap[x][y].setGold(true);
				placed = true;
			}
		}
		
		//Fallen überall mit einer Wahrscheinlichkeit von 0.2 plazieren,
		//aber nicht wo Wumpus oder Gold sind.
		for (x = 0; x < worldMap.length; x++ ) {
			for (y = 0; y < worldMap[x].length; y++) {
				if ((x != 0 || y != 0) && worldMap[x][y].isEmpty()){
					if (Math.random() <= 0.2) {
						worldMap[x][y].setPit(true);
					}
				}
			}
		}
		
		notifyWorldModelListeners();
	}

	public WorldNode[][] getWorldMap() {
		return this.worldMap;
	}

	public WumpusPerception eventShoot(AID hunterAID) {
		
		Hunter hunter = getHunter(hunterAID);
				
		int xCoord = hunter.getxCoord();
		int yCoord = hunter.getyCoord();
		int xDir = hunter.getxDirection();
		int yDir = hunter.getyDirection();
		
		WumpusPerception perception = getPerception(worldMap[xCoord][yCoord]);
		
		if (hunter.hasArrow()) {
			for (int x=xCoord, y=yCoord; (0<=x && x<4) && (0<=y && y<4); x=x+xDir, y=y+yDir){
				if (worldMap[x][y].hasWumpus()) {
					if (worldMap[x][y].isWumpusAlive()){
						worldMap[x][y].setWumpusAlive(false);
						perception.setScream();
						break;
					}
				}
			}
		}
		
		hunter.setPerformance(hunter.getPerformance() - 10);
		
		notifyHunterListListeners();
		notifyWorldModelListeners();
		return perception;
	}
	
	public WumpusPerception eventGrab(AID hunterAID){
		
		Hunter hunter = getHunter(hunterAID);
		
		int xCoord = hunter.getxCoord();
		int yCoord = hunter.getyCoord();
		
		WumpusPerception perception = getPerception(worldMap[xCoord][yCoord]);
		
		if (worldMap[xCoord][yCoord].hasGold()) {
			perception.setState(WumpusConsts.STATE_GOAL);
			//hunter.setState(WumpusConsts.STATE_GOAL);
			hunter.grab(true);
		} else {
			hunter.grab(false);
		}
		
		notifyHunterListListeners();
		notifyWorldModelListeners();
		return perception;
	}

	public WumpusPerception eventMove(AID hunterAID) {
		
		Hunter hunter = getHunter(hunterAID);

		int previousXCoord = hunter.getxCoord();
		int previousYCoord = hunter.getyCoord();

		hunter = hunter.move();

		int currentXCoord = hunter.getxCoord();
		int currentYCoord = hunter.getyCoord();
		
		WumpusPerception perception = getPerception(worldMap[currentXCoord][currentYCoord]);
		
		if ((previousXCoord == currentXCoord) && (previousYCoord == currentYCoord)) {
			perception.setBump();
		} else {
			worldMap[previousXCoord][previousYCoord].hunters.remove(hunter);
			worldMap[currentXCoord][currentYCoord].hunters.add(hunter);
		}
		
		if (worldMap[currentXCoord][currentYCoord].hasPit()) {
			perception.setState(WumpusConsts.STATE_DEAD);
			hunter.setState(WumpusConsts.STATE_DEAD);
			hunter.setPerformance(hunter.getPerformance() - 1000);
			//hunter.deathByPit();
		}
		
		if ((worldMap[currentXCoord][currentYCoord].hasWumpus()) && (worldMap[currentXCoord][currentYCoord].isWumpusAlive())) {
			perception.setState(WumpusConsts.STATE_DEAD);
			hunter.setState(WumpusConsts.STATE_DEAD);
			hunter.setPerformance(hunter.getPerformance() - 1000);
			//hunter.deathByWumpus();
		}
		
		if (worldMap[currentXCoord][currentYCoord].hasGold()) {
			perception.setGlitter(WumpusConsts.SENSOR_GLITTER);
		}

		notifyHunterListListeners();
		notifyWorldModelListeners();
		return perception;
	}

	public WumpusPerception eventTurnLeft(AID hunterAID) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "eventTurnLeft()");

		Hunter hunter = getHunter(hunterAID);
		hunter = hunter.turnLeft();

		int xCoord = hunter.getxCoord();
		int yCoord = hunter.getyCoord();
		
		WumpusPerception perception = getPerception(worldMap[xCoord][yCoord]);

		notifyHunterListListeners();
		notifyWorldModelListeners();
		return perception;
	}

	public WumpusPerception eventTurnRight(AID hunterAID) {

		Hunter hunter = getHunter(hunterAID);

		hunter.turnRight();
		int xCoord = hunter.getxCoord();
		int yCoord = hunter.getyCoord();
		
		WumpusPerception perception = getPerception(worldMap[xCoord][yCoord]);

		notifyHunterListListeners();
		notifyWorldModelListeners();
		return perception;
	}

	public WumpusPerception addHunter(final AID senderAID) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "addHunter()");
    	
    	WumpusPerception perception = null;
		
		if (senderAID != null) {
			Hunter hunter = new Hunter(senderAID);
			if (!hunters.contains(hunter)) {
				this.hunters.add(hunter);
				worldMap[hunter.getxCoord()][hunter.getyCoord()].hunters.add(hunter);
			}

			notifyHunterListListeners();
			notifyWorldModelListeners();
			perception = getPerception(worldMap[hunter.getxCoord()][hunter.getyCoord()]);
			return perception;
		} else {
			//TODO: sinnvoll ändern
			throw new RuntimeException();
		}
	}
	
	public void removeHunter(final AID hunterAID) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "removeHunter()");

		if (hunterAID != null) {
			Hunter hunter = new Hunter(hunterAID);
			if (this.hunters.contains(hunter)) {
				for (Hunter h : hunters) {
					if (h.equals(hunter)) {
						worldMap[h.getxCoord()][h.getyCoord()].hunters.remove(hunter);
						break;
					}
				}
				this.hunters.remove(hunter);

				notifyHunterListListeners();
				notifyWorldModelListeners();
			}

		} else {
			//TODO: sinnvoll ändern
			throw new RuntimeException();
		}
	}
	
	public Hunter getHunter(AID senderAID) {
		Hunter hunter;
		for (int i=0; i<hunters.size();i++){
			hunter = hunters.get(i);
			if (hunter.getHunterAID().equals(senderAID))
				return hunter;
		}
		
		return null;
	}


	public List<Hunter> getHunterList() {

		return hunters;
	}
	
	public boolean isRegisteredHunter(AID hunterAID) {
		Hunter hunter;
		for (int i=0; i<hunters.size();i++){
			hunter = hunters.get(i);
			if (hunter.getHunterAID().equals(hunterAID))
				return true;
		}
		return false;
	}
	
	public WumpusPerception getPerception(WorldNode node) {
		
		WumpusPerception perception = new WumpusPerception();

		if (node.hasPit())
			perception.setBreeze();
		if (node.hasWumpus())
			perception.setStench();
		if (node.hasGold())
			perception.setGlitter();
		
		List<WorldNode> neighbours = getNeighbours(node);
		
		if (neighbours != null && neighbours.size() > 0){
			
			for (WorldNode n : getNeighbours(node)) {
				if (n.hasPit())
					perception.setBreeze();
				if (n.hasWumpus())
					perception.setStench();
			}
		}
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "Perception: " + perception.toString() + " at [" + node.getxCoord() + "," + node.getyCoord() + "]");
		
		return perception;
	}
	
	/**
	 * Gibt die orthogonal benachbarten Knoten eines des als Parameter
	 * übergebenen Knoten zurück.
	 * @param node ist der Knoten, dessen Kindknoten zurückgegeben werden sollen
	 * @return Eine Liste aus WorldNode mit den Kindknoten
	 */
	public List<WorldNode> getNeighbours(WorldNode node) {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "getNeighbours() for node: " + node.getxCoord() + ", " + node.getyCoord());

		List<WorldNode> result = new ArrayList<WorldNode>();
		
		int[] offset = {1, -1};
		WorldNode child = null;

		for (int i : offset) {

			if (0<=(node.getxCoord()+i) && (node.getxCoord()+i) <4) {
				child = worldMap[node.getxCoord()+i][node.getyCoord()];
				if (child != null)
					result.add(child);
			}

			if (0<=(node.getyCoord()+i) && (node.getyCoord()+i) <4) {
				child = worldMap[node.getxCoord()][node.getyCoord()+i];
				if (child != null)
					result.add(child);
			}
		}
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "Anzahl Neighbours: " + result.size());

		return result;
	}
	

	/*
	 * Listener fürs GUI
	 */
	public void addWorldModelListener(final WorldModelListener listener) {
		if (listener != null && !listenersWorld.contains(listener)) {
			this.listenersWorld.add(listener);
		}
	}
	
	public void removeWorldModelListener(final WorldModelListener listener) {
		this.listenersWorld.remove(listener);
	}
	
	public void notifyWorldModelListeners() {
		for (final WorldModelListener listener : this.listenersWorld) {
			notifyWorldModelListener(listener);
		}
	}

	public void notifyWorldModelListener(final WorldModelListener listener) {
		listener.worldModelChanged(this.getWorldMap());
	}

	public void addHunterListListener(final HunterListListener listener) {
		if (listener != null && !listenersHunter.contains(listener)) {
			this.listenersHunter.add(listener);
		}
	}
	
	public void removeHunterListListener(final HunterListListener listener) {
		this.listenersHunter.remove(listener);
	}
	
	public void notifyHunterListListeners() {
		for (final HunterListListener listener : this.listenersHunter) {
			listener.hunterListModelChanged(this.hunters);
		}
	}

	public boolean isHunterAlive(AID senderAID) {
		
		Hunter hunter = getHunter(senderAID);
		
		if (hunter.getState().equals(WumpusConsts.STATE_ALIVE))
			return true;
		else
			return false;
		
	}
}

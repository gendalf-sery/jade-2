package agentville.games.wumpus.world;

import jade.content.Predicate;

/**
 * [stench, breeze, none, none, none, state]
 * [stench, breeze, glitter, bump, scream, alive/dead/goal]
 * @author Marco
 *
 */
public class WumpusPerception implements Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 623497765435278445L;

	//perception fields
	private String stench;
	private String breeze;
	private String glitter;
	private String bump;
	private String scream;
	private String state;
	
	public WumpusPerception() {
		this.stench = WumpusConsts.SENSOR_DEFAULT;
		this.breeze = WumpusConsts.SENSOR_DEFAULT;
		this.glitter = WumpusConsts.SENSOR_DEFAULT;
		this.bump = WumpusConsts.SENSOR_DEFAULT;
		this.scream = WumpusConsts.SENSOR_DEFAULT;
		this.state = WumpusConsts.STATE_ALIVE;
	}
	
	public WumpusPerception(String perceptionString) {
	
		this.toObject(perceptionString);
	}
	
	public void setScream(){
		this.scream = WumpusConsts.SENSOR_SCREAM;
	}
	
	public String getScream(){
		return this.scream;
	}
	
	@Override
	public String toString() {
		return "[" + stench + "," 
				+ breeze + "," 
				+ glitter + "," 
				+ bump + ","
				+ scream + "," 
				+ state + "]";
	}
	
	public void toObject(String perceptionString) {
		perceptionString = perceptionString.substring(1, perceptionString.length()-1);
		String[] result = perceptionString.split(",");
		if (result.length != 6)
			throw new RuntimeException("Bloede Fehlerbehandlung, aber so gehts "
					+ "halt nicht. Länge ist: " + result.length);
			
		this.stench = (result[0].equals(WumpusConsts.SENSOR_STENCH) ? result[0] : WumpusConsts.SENSOR_DEFAULT);
		this.breeze = (result[1].equals(WumpusConsts.SENSOR_BREEZE) ? result[1] : WumpusConsts.SENSOR_DEFAULT);
		this.glitter = (result[2].equals(WumpusConsts.SENSOR_GLITTER) ? result[2] : WumpusConsts.SENSOR_DEFAULT);
		this.bump = (result[3].equals(WumpusConsts.SENSOR_BUMP) ? result[3] : WumpusConsts.SENSOR_DEFAULT);
		this.scream = (result[4].equals(WumpusConsts.SENSOR_SCREAM) ? result[4] : WumpusConsts.SENSOR_DEFAULT);
	
		this.state = (isValidState(result[5]) ? result[5] : "");
	}

	public void setState(String state) {
		if(isValidState(state)) {
			this.state = state;
		} else {
			//TODO: geeignet ändern
			throw new RuntimeException();
		}
	}

	public String getState() {
		if(isValidState(this.state)){
			return this.state;
		}

		return null;
	}
	
	private boolean isValidState(String state){
		
		if(state.equals(WumpusConsts.STATE_ALIVE) 
				|| state.equals(WumpusConsts.STATE_DEAD) 
				|| state.equals(WumpusConsts.STATE_GOAL)) {
			return true;
		}
		
		return false;
	}
	
	public void setBump() {
		this.bump = WumpusConsts.SENSOR_BUMP;
	}

	public void setBump(String param) {
		this.bump = param;
	}

	public String getBump() {
		return this.bump;
	}

	public void setStench() {
		this.stench = WumpusConsts.SENSOR_STENCH;
	}

	public void setStench(String param) {
		this.stench = param;
	}

	public String getStench() {
		return this.stench;
	}

	public void setBreeze() {
		this.breeze = WumpusConsts.SENSOR_BREEZE;
	}

	public void setBreeze(String param) {
		this.breeze = param;
	}

	public String getBreeze() {
		return this.breeze;
	}

	public void setGlitter() {
		this.glitter = WumpusConsts.SENSOR_GLITTER;
	}

	public void setGlitter(String param) {
		this.glitter = param;
	}

	public String getGlitter() {
		return this.glitter;
	}
}
package agentville.games.wumpus.world;

public class WumpusConsts {
	
	static final String GAME_NAME = "Wumpus World";
	static final String GAME_VERSION = "1.0";

	public static final String GAME_TOPIC_NAME = "Wumpus Broadcast";
	public static final String GAME_SERVICE_NAME = "Wumpus World";
	public static final String GAME_SERVICE_TYPE = "wumpus-world";
	public static final String GAME_CONTAINER_NAME = "Games";
	
	public static final String STATE_ALIVE = "ALIVE";
	public static final String STATE_DEAD = "DEAD";
	public static final String STATE_GOAL = "GOAL";
	
	public static final String SENSOR_BUMP = "bump";
	public static final String SENSOR_STENCH = "stench";
	public static final String SENSOR_BREEZE = "breeze";
	public static final String SENSOR_GLITTER = "glitter";
	public static final String SENSOR_SCREAM = "scream";
	public static final String SENSOR_DEFAULT ="none";

	public static final String ACTION_REGISTER = "register";
	public static final String ACTION_DEREGISTER = "deregister";

	public static final String ACTION_MOVE = "move";
	public static final String ACTION_TURN_LEFT = "turnLeft";
	public static final String ACTION_TURN_RIGHT = "turnRight";
	public static final String ACTION_SHOOT = "shoot";
	public static final String ACTION_GRAB = "grab";
	
	static final int GUI_EVENT_CLOSE = 0;
	static final int GUI_EVENT_AGENT_DELETE = 1;
	static final int GUI_EVENT_AGENT_DELETE_ALL = 2;
	static final int GUI_EVENT_WORLD_CREATE = 3;
	static final int GUI_EVENT_WORLD_LOAD = 4;
	static final int GUI_EVENT_WORLD_SAVE = 5;
	static final int GUI_EVENT_GUI_CLOSE = 6;
	static final int GUI_EVENT_GAME_TAKEDOWN = 7;
}
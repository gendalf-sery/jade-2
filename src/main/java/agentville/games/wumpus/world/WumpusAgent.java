package agentville.games.wumpus.world;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import agentville.games.wumpus.world.behaviours.PlayGameBehaviour;
import agentville.games.wumpus.world.model.World;
import agentville.games.wumpus.world.view.WumpusGUI;

/**
 * @author Marco Steffens
 * @version 0.1
 */
public class WumpusAgent extends Agent {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getMyLogger(this.getClass().getName());
	
	private transient World world; //TODO: 'World' serialisierbar machen!
//	private List<Hunter> hunters;
//	private List<HunterListListener> listenersHunter;
	
	private DFAgentDescription gameAgent = null;
	private DFAgentDescription dfd = null;
	private AID topic = null;
	private ServiceDescription sd = null;
	
    protected void setup() {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "Hello World! This is Agent '" + getLocalName() + "'!");
    	
    	world = new World();
    	
		new WumpusGUI(this).setVisible(true);

    	dfd = new DFAgentDescription();
    	dfd.setName(this.getAID());

    	/*
    	 * Der Agent wird in den Yellow Pages registriert
    	 */
		sd = new ServiceDescription();
		sd.setName(WumpusConsts.GAME_SERVICE_NAME);
		sd.setType(WumpusConsts.GAME_SERVICE_TYPE);
		dfd.addServices(sd);
		try {
	    	if (DFService.search(this, gameAgent).length > 0) {
		    	if(log.isLoggable(Logger.INFO))
		    		log.log(Logger.INFO, "DEregister game '" + WumpusConsts.GAME_SERVICE_TYPE + "'");
	    		DFService.deregister(this, gameAgent);
	    	}
	    	if(log.isLoggable(Logger.INFO))
	    		log.log(Logger.INFO, "Register game '" + WumpusConsts.GAME_SERVICE_TYPE + "'");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
	    	if(log.isLoggable(Logger.WARNING))
	    		log.log(Logger.WARNING, "ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		
		/*
		 * Der Topic-Kanal für die Kommunikation mit allen spielenden Agenten
		 * wird erzeugt. (Um allen den Tod des Wumpus verkünden zu können.)
		 */
		TopicManagementHelper hlp;
		try {
			hlp = (TopicManagementHelper) this.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.topic = hlp.createTopic(WumpusConsts.GAME_TOPIC_NAME);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}

//		/*
//		 * Das Finite State Machine Behaviour, in dem sie
//		 * für das Spiel benötigten Behaviour zusammengefasst
//		 * sind, wird erzeugt und zum Agenten hinzugefügt.
//		 */
//		GameBehaviour fsm = new GameBehaviour(this);
//		this.addBehaviour(fsm);
		
		PlayGameBehaviour pgb = new PlayGameBehaviour();
		this.addBehaviour(pgb);
   	}
    
	protected void takeDown() {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "takeDown() WumpusAgent");
		
		try {
	    	if(log.isLoggable(Logger.INFO))
	    		log.log(Logger.INFO, "DEregister game '" + WumpusConsts.GAME_SERVICE_TYPE + "'");
			DFService.deregister(this, dfd);
		} catch (FIPAException e) {
	    	if(log.isLoggable(Logger.WARNING))
	    		log.log(Logger.WARNING, "ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public WumpusPerception processAction(AID agentAID, String action) {
		WumpusPerception perception = null;
		
//		switch(action) {
//		
//		case WumpusConsts.ACTION_REGISTER:
//			
//			perception = null;
//			if (isRegisteredHunter(agentAID))
//				world.removeHunter(agentAID);
//			break;
//			
//		case WumpusConsts.ACTION_DEREGISTER:
//		
//			world.removeHunter(agentAID);
//			break;
//			
//		}
		

		if (action.equals(WumpusConsts.ACTION_REGISTER)) {
			
			perception = null;
			
			if (isRegisteredHunter(agentAID))
				world.removeHunter(agentAID);
				
			perception = world.addHunter(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_DEREGISTER)) {
			
			world.removeHunter(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_TURN_LEFT)) {
			
			perception = world.eventTurnLeft(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_TURN_RIGHT)) {
			
			perception = world.eventTurnRight(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_MOVE)) {
			
			perception = world.eventMove(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_GRAB)) {
			
			perception = world.eventGrab(agentAID);
			
		} else if (action.equals(WumpusConsts.ACTION_SHOOT)) {
			
			perception = world.eventShoot(agentAID);
			
			if (perception.getScream() == WumpusConsts.SENSOR_SCREAM) {
				
				/*
				 * Wenn der Wumpus erlegt wurde, erhalten alle Agenten
				 * im Spiel einen entsprechenden Hinweis, und zwar über
				 * den Topic-Kanal.
				 * TODO: Gedanken machen über die sinnvollste Verarbeitung
				 * im Spiel-Agenten, vermutlich besser eine richtige
				 * Perception mit einem 'scream' verschicken. (Kann das 
				 * halbwegs normal verarbeitet werden?)
				 */
				ACLMessage msgTopic = new ACLMessage(ACLMessage.INFORM);
				msgTopic.setSender(this.getAID());
				msgTopic.addReceiver(this.topic);
				msgTopic.setContent(WumpusConsts.SENSOR_SCREAM);
				this.send(msgTopic);			
			}
			
		} else {
			throw new RuntimeException();
		}
		
		return perception;
	}
	
    public World getWorld() {
    	return this.world;
    }

	public boolean isRegisteredHunter(AID hunterAID) {

		return world.isRegisteredHunter(hunterAID);
	}

	public boolean maximumHuntersReached() {
		
		if (world.getHunterList().size()<=10)
			return false;
		else
			return true;
	}

	public boolean isHunterAlive(AID senderAID) {
		
		return world.isHunterAlive(senderAID);
	}
}

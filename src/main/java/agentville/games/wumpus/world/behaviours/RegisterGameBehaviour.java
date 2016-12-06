package agentville.games.wumpus.world.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

public class RegisterGameBehaviour extends OneShotBehaviour {

//	DFAgentDescription dfd = null;
//	ServiceDescription description = null;
	
	private int transition = GameBehaviour.TRANS_GAME_REGISTERED;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getMyLogger(this.getClass().getName());

	public RegisterGameBehaviour(Agent agent) {

		super(agent);
	}

	@Override
	public void action() {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "action()");

//    	dfd = new DFAgentDescription();
//		description = new ServiceDescription();
//		description.setName("Wumpus' Wumpus-Game");
//		description.setType("wumpus-game");
//		dfd.addServices(description);
//		try {
//	    	if(log.isLoggable(Logger.WARNING))
//	    		log.log(Logger.WARNING, "Register game 'wumpus-game'");
//	    	if (DFService.search(myAgent,  dfd).length > 0) {
//	    		DFService.deregister(myAgent, dfd);
//	    	}
//			DFService.register(myAgent, dfd);
//		} catch (FIPAException e) {
//	    	if(log.isLoggable(Logger.WARNING))
//	    		log.log(Logger.WARNING, "ERROR: " + e.getMessage());
//			e.printStackTrace();
//		}

	}

	@Override
	public int onEnd() {
		// TODO Auto-generated method stub
		return transition;
	}

}

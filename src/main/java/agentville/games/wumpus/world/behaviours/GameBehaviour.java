package agentville.games.wumpus.world.behaviours;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

/**
 * Das Finite State Machine Behaviour sorgt für den eigentlichen Ablauf des 
 * Spiels bzw. des Agenten. Es besteht seinerseits wiederrum aus drei Behaviours:
 * - SearchGameBehaviour (TickerBehaviour)
 * - RegisterAgentBehaviour (OneShotBehaviour)
 * - PlayGameBehaviour (SimpleBehaviour)
 * De-registriert wird im RegisterAgentBehaviour vor dem registrieren, da ja im 
 * Spiel nicht de-registriert werden soll und das so also nur für die Testläufe 
 * notwendig ist. Ein De-registrieren ohne vorheriges Registrieren ist problemlos
 * möglich.
 */
public class GameBehaviour extends FSMBehaviour {

	private static final long serialVersionUID = 1L;

	//FSM state values:
	private static String STATE_REGISTER 	= "Register";
	private static String STATE_PLAY 		= "Play";
//	private static String STATE_DEREGISTER 	= "Deregister";
	
	//FSM transition values:
	protected static int TRANS_GAME_REGISTERED 	= 0;
	protected static int TRANS_GAME_OVER 		= 1;
	protected static int TRANS_GAME_DEREGISTERED 	= 2;
	
	public GameBehaviour(Agent a) {
		super(a);
		
		//Register states
		registerFirstState(new RegisterGameBehaviour(a), STATE_REGISTER);
		registerLastState(new PlayGameBehaviour(), STATE_PLAY);
//		registerState(new PlayGameBehaviour(), STATE_PLAY);
		
		//Register Transition
		registerTransition(STATE_REGISTER, STATE_PLAY, TRANS_GAME_REGISTERED);
		
		scheduleFirst();
	}
	
	public void onStart() {

	}
	
	public int onEnd() {
		
//		try {
//			DFService.deregister(myAgent);
//		} catch (FIPAException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return 0;
	}
}

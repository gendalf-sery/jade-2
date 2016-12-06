package agentville.games.wumpus.world.behaviours;

import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import agentville.games.wumpus.world.WumpusAgent;
import agentville.games.wumpus.world.WumpusConsts;
import agentville.games.wumpus.world.WumpusPerception;
import agentville.games.wumpus.world.WumpusPerceptionOntology;

public class PlayGameBehaviour extends SimpleBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getMyLogger(this.getClass().getName());
	
	private int transition = GameBehaviour.TRANS_GAME_OVER;

	ACLMessage reply = null;
	WumpusPerception perception = null;
	String content = null;
	AID senderAID = null;

	private Ontology ontology = null;
	private ContentManager man = null;
	private SLCodec codec = null;

	public void onStart() {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "onStart()");

		ontology = WumpusPerceptionOntology.getInstance();
		
		man = myAgent.getContentManager();
		codec = new SLCodec();
		man.registerOntology(ontology);
		man.registerLanguage(codec);
	}
	
	public void action() {
    	if(log.isLoggable(Logger.INFO))
    		log.log(Logger.INFO, "action()");
		
//		ACLMessage msg;
//
//		msg = new ACLMessage(ACLMessage.REQUEST);
//		msg.setSender(myAgent.getAID());
//		msg.addReceiver(((WumpusAgent) myAgent).getGameAgentDescription().getName());
//		/*
//		 * Der Agent wird zunaechst von Spiel AB-...
//		 */
//		msg.setContent(WumpusConsts.ACTION_DEREGISTER);
//		myAgent.send(msg);
//		
//		/*
//		 * ... und dann ANgemeldet.
//		 */
//		msg.setContent(WumpusConsts.ACTION_REGISTER);
//		myAgent.send(msg);

    	do {
    		ACLMessage msg = myAgent.blockingReceive();
    		
			//msgPerception.getContent();
	    	if(log.isLoggable(Logger.INFO)) {
	    		log.log(Logger.INFO, "Message-Content: " + msg.getContent());
	    		log.log(Logger.INFO, "Message-Performative: " + msg.getPerformative());
	    		log.log(Logger.INFO, "Message-Perfomrative (geraten): " + ACLMessage.REQUEST);
	    		log.log(Logger.INFO, "Message-Sender: " + msg.getSender().toString());
	    		log.log(Logger.INFO, "Message-Sender-Name: " + msg.getSender().getName());
	    		log.log(Logger.INFO, "Message-Sender-LocalName: " + msg.getSender().getLocalName());
	    	}
	    	
    		content = msg.getContent().trim();
    		senderAID = msg.getSender();
			reply = msg.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
	    	reply.setOntology(ontology.getName());
	    	reply.setLanguage(codec.getName());
			perception = null;
	    	
	    	if ((msg.getPerformative() == ACLMessage.REQUEST) 
	    			&& (content.equals(WumpusConsts.ACTION_REGISTER))
	    			&& (!((WumpusAgent) myAgent).maximumHuntersReached())
	    			//&& (!((WumpusAgent) myAgent).isRegisteredHunter(senderAID))
	    			) {
    			//Register Hunter and send back first perception.
    			/*
    			 * Der Name des Agenten zusammen mit der Rechner-IP ist unique
    			 */
    			perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
    			reply.setPerformative(ACLMessage.INFORM);
    			
    		} else if ((((WumpusAgent) myAgent).isRegisteredHunter(senderAID)) 
    				&& (msg.getPerformative() == ACLMessage.REQUEST) 
    				&& (isContentValid(content))
    				&& (((WumpusAgent) myAgent).isHunterAlive(senderAID))){
    			
		    	if(log.isLoggable(Logger.INFO))
		    		log.log(Logger.INFO, "Hunter wants to: " + content);

		    	if (content.equals(WumpusConsts.ACTION_DEREGISTER)) {

        			((WumpusAgent) myAgent).processAction(senderAID, content);
        			reply.setPerformative(ACLMessage.AGREE);

        		} else if (content.equals(WumpusConsts.ACTION_MOVE)) {
    				
        			reply.setPerformative(ACLMessage.INFORM);
    				perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
	    			
	    		} else if (content.equals(WumpusConsts.ACTION_TURN_LEFT)) {
	    			
        			reply.setPerformative(ACLMessage.INFORM);
	    			perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
	    			
	    		} else if (content.equals(WumpusConsts.ACTION_TURN_RIGHT)) {
	    			
        			reply.setPerformative(ACLMessage.INFORM);
	    			perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
	    			
	    		} else if (content.equals(WumpusConsts.ACTION_SHOOT)) {
	    			
        			reply.setPerformative(ACLMessage.INFORM);
	    			perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
	    			
	    		} else if (content.equals(WumpusConsts.ACTION_GRAB)) {
	    			
        			reply.setPerformative(ACLMessage.INFORM);
	    			perception = ((WumpusAgent) myAgent).processAction(senderAID, content);
	    			
	    		}
    		}
    			
    		if (perception == null) {
    	    	if(log.isLoggable(Logger.WARNING))
    	    		log.log(Logger.WARNING, "Perception-Content == null");
    			
    			reply.setContent("");
    		} else {
    	    	if(log.isLoggable(Logger.INFO))
    	    		log.log(Logger.INFO, "Perception-Content = " + perception.toString());
    	    	reply.setContent(perception.toString()); //TODO: den folgenden (auskommentierten) Block wieder aktivieren (man.fillContent() ist der richtige Weg!)
//				try {
//					man.fillContent(reply, perception);
//					reply.setContentObject(perception);
//				} catch (CodecException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (OntologyException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
    		}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
	    	
    		myAgent.send(reply);
    		
    	} while (true);
		
	}

	private boolean isContentValid(String content) {
		
		if (content.equals(WumpusConsts.ACTION_DEREGISTER) 
				|| content.equals(WumpusConsts.ACTION_REGISTER)
				|| content.equals(WumpusConsts.ACTION_GRAB)
				|| content.equals(WumpusConsts.ACTION_MOVE)
				|| content.equals(WumpusConsts.ACTION_SHOOT)
				|| content.equals(WumpusConsts.ACTION_TURN_LEFT)
				|| content.equals(WumpusConsts.ACTION_TURN_RIGHT))
			return true;
		
		return false;
	}

	public int onEnd() {

		return transition; 
	}

	public boolean done() {
		return true;
	}
}

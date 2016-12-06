package agentville.games.wumpus.world;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;

/**
 * Der JADE-Agent an sich ist ja nicht direkt ausführbar, sondern wird über eine
 * Klasse des Frameworks gestartet. Die Alternative dazu besteht in einer Main-
 * Methode wie hier, die den Agenten dann doch direkt ausführbar macht. Die Main-
 * Methode startet eine Singleton-Instanz von JADE und setzt die Einstellungen
 * um, die sonst als Parameter übergeben werden: Container-Name, Services, Agenten-
 * Name etc.
 * Die Main-Methode ist ausgelagert, weil es Probleme mit der Eclipse-Run-Konfiguration
 * gab. (In der Form, dass Eclipse immer automatisch eine neue Run-Konfiguration
 * erstellt und verwendet hat, die dann nicht den Erfordernissen dieses Projektes
 * entsprach und also nicht funktioniert hat.)
 * 
 * Damit der Agent ausgeführt werden kann, muss eine Plattform (extern) gestartet sein!
 * 
 * @author Marco Steffens
 * @version 1.0
 */
public class Wumpus {

	private static HashMap<String, ContainerController> containerList = new HashMap<String, ContainerController>();// container's name - container's ref

	public static void main(String[] args){
		
		String host;
		int port;
		String platform = null;		//default name
		boolean main = false;		//'normal'-container

		host = "localhost";
		port = -1;				//default-port 1099
		
		//Runtime runtime = emptyPlatform(containerList);
		Runtime runtime = Runtime.instance();
		
		Profile profile = null;
		AgentContainer container = null;
		

		profile = new ProfileImpl(host, port, platform, main);
		//Notwendig fuer den topic-Service:
		profile.setParameter(Profile.SERVICES, 
				"jade.core.event.NotificationService; " +
				"jade.core.messaging.TopicManagementService");
		profile.setParameter(Profile.CONTAINER_NAME, WumpusConsts.GAME_CONTAINER_NAME);
		
		//Container erzeugen
		container = runtime.createAgentContainer(profile);
		
		 // Agenten erzeugen und startet - oder aussteigen.
		try {

			AgentController agent = container.createNewAgent(
					WumpusConsts.GAME_NAME, 
					WumpusAgent.class.getName(), 
					args);
			agent.start();

		} catch(StaleProxyException e) {
			//Wenn das schon nicht geht, ist eh alles egal.
			throw new RuntimeException(e);
		}			
    	
    }
	
	private static Runtime emptyPlatform(HashMap<String, ContainerController> containerList){

		Runtime rt = Runtime.instance();

		// 1) create a platform (main container+DF+AMS)
		Profile pMain = new ProfileImpl("localhost", 8888, null);
		System.out.println("Launching a main-container..."+pMain);
		AgentContainer mainContainerRef = rt.createMainContainer(pMain); //DF and AMS are include

		// 2) create the containers
		containerList.putAll(createContainers(rt));

		// 3) create monitoring agents : rma agent, used to debug and monitor the platform; sniffer agent, to monitor communications; 
		createMonitoringAgents(mainContainerRef);

		System.out.println("Plaform ok");
		return rt;

	}
	
	private static HashMap<String,ContainerController> createContainers(Runtime rt) {
		String containerName;
		ProfileImpl pContainer;
		ContainerController containerRef;
		HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();//bad to do it here.


		System.out.println("Launching containers ...");

		//create the container0	
		containerName="container0";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		//create the container1	
		containerName="container1";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		//create the container2	
		containerName="container2";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		System.out.println("Launching containers done");
		return containerList;
	}


	/**
	 * create the monitoring agents (rma+sniffer) on the main-container given in parameter and launch them.
	 *  - RMA agent's is used to debug and monitor the platform;
	 *  - Sniffer agent is used to monitor communications
	 * @param mc the main-container's reference
	 * @return a ref to the sniffeur agent
	 */
	private static void createMonitoringAgents(ContainerController mc) {

		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;

		try {
			rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Launching of rma agent failed");
		}

		System.out.println("Launching  Sniffer agent on the main container...");
		AgentController snif=null;

		try {
			snif= mc.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);
			snif.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("launching of sniffer agent failed");

		}		


	}
}

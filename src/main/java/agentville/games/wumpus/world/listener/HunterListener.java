package agentville.games.wumpus.world.listener;

import java.util.List;

import agentville.games.wumpus.world.model.Hunter;

public class HunterListener implements HunterListListener, Runnable {

//	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

//	@Override
	public void hunterListModelChanged(List<Hunter> hunters) {

		updateList(hunters);
	}

	private void updateList(List<Hunter> hunters) {
		// TODO Auto-generated method stub
		
	}

}

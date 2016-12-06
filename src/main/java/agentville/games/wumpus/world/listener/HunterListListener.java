package agentville.games.wumpus.world.listener;

import java.util.List;

import agentville.games.wumpus.world.model.Hunter;

public interface HunterListListener {

	void hunterListModelChanged(List<Hunter> hunters);
}

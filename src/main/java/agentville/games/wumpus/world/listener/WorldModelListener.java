package agentville.games.wumpus.world.listener;

import agentville.games.wumpus.world.model.WorldNode;

public interface WorldModelListener {

	void worldModelChanged(WorldNode[][] node);
}

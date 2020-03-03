package net.avdw.spyfall.game.memory;

import java.util.List;

public class SpyfallPlayerSpecification implements MemorySpecification<SpyfallPlayer> {
    @Override
    public List<SpyfallPlayer> toMemoryResultList(final GameMemory gameMemory) {
        return gameMemory.spyfallPlayerList;
    }
}

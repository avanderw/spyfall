package net.avdw.spyfall.game.memory;

import java.util.List;
import java.util.stream.Collectors;

public class SpyfallPlayerNotReadySpecification implements MemorySpecification<SpyfallPlayer> {
    @Override
    public List<SpyfallPlayer> toMemoryResultList(final GameMemory gameMemory) {
        return gameMemory.spyfallPlayerList.stream().filter(SpyfallPlayer::isNotReady).collect(Collectors.toList());
    }
}

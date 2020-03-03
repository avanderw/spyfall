package net.avdw.spyfall.game.memory;

import java.util.List;
import java.util.stream.Collectors;

public class SpyfallPlayerByIdSpecification implements MemorySpecification<SpyfallPlayer> {
    private final String id;

    public SpyfallPlayerByIdSpecification(final String id) {
        this.id = id;
    }

    @Override
    public List<SpyfallPlayer> toMemoryResultList(final GameMemory gameMemory) {
        List<SpyfallPlayer> spyfallPlayerList = gameMemory.spyfallPlayerList.stream()
                .filter(player->player.getId().equals(id))
                .collect(Collectors.toList());

        if (spyfallPlayerList.size() > 1) {
            throw new UnsupportedOperationException();
        }
        return spyfallPlayerList;
    }
}

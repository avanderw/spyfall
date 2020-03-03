package net.avdw.spyfall.game.memory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.repository.Repository;
import net.avdw.repository.Specification;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class SpyfallPlayerMemoryRepository implements Repository<SpyfallPlayer> {
    private final GameMemory gameMemory;

    @Inject
    SpyfallPlayerMemoryRepository(final GameMemory gameMemory) {
        this.gameMemory = gameMemory;
    }

    @Override
    public void add(final SpyfallPlayer item) {
        add(Collections.singletonList(item));
    }

    @Override
    public void add(final Collection<SpyfallPlayer> items) {
        gameMemory.spyfallPlayerList.addAll(items);
    }

    @Override
    public void update(final SpyfallPlayer item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(final SpyfallPlayer item) {
        gameMemory.spyfallPlayerList.remove(item);
    }

    @Override
    public void remove(final Specification specification) {
        if (specification instanceof MemorySpecification) {
            MemorySpecification<SpyfallPlayer> memorySpecification = (MemorySpecification<SpyfallPlayer>) specification;
            gameMemory.spyfallPlayerList.removeAll(memorySpecification.toMemoryResultList(gameMemory));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<SpyfallPlayer> query(final Specification specification) {
        if (specification instanceof MemorySpecification) {
            MemorySpecification<SpyfallPlayer> memorySpecification = (MemorySpecification) specification;
            return memorySpecification.toMemoryResultList(gameMemory);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

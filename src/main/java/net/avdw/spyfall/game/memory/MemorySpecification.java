package net.avdw.spyfall.game.memory;

import net.avdw.repository.Specification;
import org.tinylog.Logger;

import java.util.List;

public interface MemorySpecification<T> extends Specification {
    List<T> toMemoryResultList(final GameMemory gameMemory);
}

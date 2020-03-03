package net.avdw.repository;

import java.util.Collection;
import java.util.List;

public interface Repository<T> {
    void add(T item);
    void add(Collection<T> items);
    void update(T item);
    void remove(T item);
    void remove(Specification specification);
    List<T> query(Specification specification);
}

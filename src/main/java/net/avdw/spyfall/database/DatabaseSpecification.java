package net.avdw.spyfall.database;

import net.avdw.repository.Specification;

public interface DatabaseSpecification extends Specification {
    String toSqlQuery();
}

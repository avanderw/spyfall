package net.avdw.spyfall.db;

import org.pmw.tinylog.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationTable {
    private Integer pk;
    private String name;

    public LocationTable(final ResultSet resultSet) {
        try {
            pk = resultSet.getInt("PK");
            name = resultSet.getString("Name");
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }

    public Integer getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LocationTable{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                '}';
    }
}

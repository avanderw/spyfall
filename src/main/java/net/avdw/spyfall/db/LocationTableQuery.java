package net.avdw.spyfall.db;

import com.google.inject.Inject;
import net.avdw.liquibase.DbConnection;
import org.pmw.tinylog.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationTableQuery {
    private final String allQuery = "SELECT * FROM Location";
    private final Connection connection;

    @Inject
    LocationTableQuery(@DbConnection final Connection connection) {
        this.connection = connection;
    }

    public List<LocationTable> queryAll() {
        return query(allQuery);
    }

    private List<LocationTable> query(final String sql) {
        Logger.debug("Query: {}", sql);
        List<LocationTable> personTableList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                personTableList.add(new LocationTable(resultSet));
            }
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
        return personTableList;
    }
}

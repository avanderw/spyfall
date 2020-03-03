package net.avdw.spyfall.cli;

import com.google.inject.Inject;
import net.avdw.database.LiquibaseRunner;
import net.avdw.spyfall.database.LocationTable;
import net.avdw.spyfall.database.LocationTableQuery;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "spyfall", description = "The spyfall cli", version = "1.0-SNAPSHOT", mixinStandardHelpOptions = true,
        subcommands = {
        ConfigCli.class,
        ServerCli.class,
        ConnectCli.class})
public class MainCli implements Runnable {

    @Inject
    private LiquibaseRunner liquibaseRunner;
    @Inject
    private LocationTableQuery locationTableQuery;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        liquibaseRunner.update();
        List<LocationTable> locationTableList = locationTableQuery.queryAll();
        locationTableList.forEach(System.out::println);
    }
}

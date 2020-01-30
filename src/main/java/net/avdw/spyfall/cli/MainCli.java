package net.avdw.spyfall.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "spyfall", description = "The spyfall cli", version = "1.0-SNAPSHOT", mixinStandardHelpOptions = true,
        subcommands = {ConfigCli.class})
public class MainCli implements Runnable {
    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {

    }
}
